package env

import application.Application.application
import bank.BusinessLimitBank
import cats.effect.std.{AtomicCell, Console, CountDownLatch, Queue}
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.effect.{Async, IO, Temporal}
import cats.implicits.{catsSyntaxApplicativeId, toFlatMapOps, toFunctorOps}
import cats.{Applicative, Monad, MonadError, Show}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.charset.Charset
import scala.concurrent.duration.DurationInt

class ApplicationSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  "application" should "request bank no more than N times" in {
    val N = 2
    for {
      atomicSet <- AtomicCell[IO].of(Set.empty[String])
      latch     <- CountDownLatch[IO](3)
      bank = StrictOnRequestsBusinessLimitBank[IO](atomicSet, latch, N)
      queue <- Queue.bounded[IO, String](10)
      implicit0(console: Console[IO]) = new ConsoleMock[IO](queue)

      _ <- Async[IO].start(application(bank, N))

      _ <- console.offerReadLine("set 1 1")
      _ <- console.offerReadLine("set 2 2")
      _ <- console.offerReadLine("set 3 3")

      attempt <- bank.awaitAllRequests().timeout(10.second).attempt

    } yield attempt shouldBe a[Right[_, _]]

  }

  case class StrictOnRequestsBusinessLimitBank[F[_]: Temporal: Console: Monad](
      atomicSet: AtomicCell[F, Set[String]],
      latch: CountDownLatch[F],
      maxInTime: Int
  )(implicit
      ME: MonadError[F, Throwable]
  ) extends BusinessLimitBank[F] {

    override def setLimit(id: String, amount: String): F[Unit] = for {
      _ <- Console[F].println(s"new req $id")
      _ <- atomicSet
        .evalUpdate(set => {
          if (set.size < maxInTime) Monad[F].pure(set + id)
          else ME.raiseError[Set[String]](new IllegalStateException("too many requests"))
        })
      _ <- Temporal[F].sleep(3.second)
      _ <- atomicSet.evalUpdate(set => (set - id).pure)
      _ <- latch.release
      _ <- Console[F].println(s"end req $id")
    } yield ()

    override def getLimit(id: String): F[Int] = ???

    def awaitAllRequests(): F[Unit] = {
      latch.await
    }

  }

  private class ConsoleMock[F[_]: Applicative](
      queue: Queue[F, String]
  ) extends Console[F] {
    override def readLineWithCharset(charset: Charset): F[String] =
      queue.take

    override def println[A](a: A)(implicit S: Show[A]): F[Unit] =
      scala.Console.println(a).pure[F]

    def offerReadLine(offer: String): F[Unit] = queue.offer(offer)

    override def print[A](a: A)(implicit S: Show[A]): F[Unit] = ???

    override def error[A](a: A)(implicit S: Show[A]): F[Unit] = ???

    override def errorln[A](a: A)(implicit S: Show[A]): F[Unit] = ???
  }

}
