package application

import cats.effect.implicits.concurrentParTraverseOps
import cats.effect.std.{AtomicCell, Console}
import cats.effect.{Async, ExitCode, IO, IOApp, Sync}
import cats.implicits.{toFlatMapOps, toFunctorOps}

import scala.annotation.tailrec

object Application extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    application[IO](args.flatMap(_.toLongOption), 5).attempt
      .map {
        case Left(_)  => ExitCode.Error
        case Right(_) => ExitCode.Success
      }

  // Разрешается менять context bound функции
  def application[F[_]: Async: Console](nums: List[Long], n: Int): F[Unit] = {
    for {
      c <- AtomicCell[F].of(1)
      _ <- nums
        .parTraverseN(n) { num =>
          for {
            ds <- Sync[F].delay(factorize(num))
            _ <-
              c.evalUpdate(i =>
                Console[F].println(s"line $i, $num = ${ds.mkString(" * ")}").as(i + 1)
              )
          } yield ()
        }
    } yield ()
  }

  def factorize(n: Long): List[Long] = {
    if (n == Long.MinValue) factorize(-(n / 2), 2, List(2, -1))
    else if (n == 0) List(0)
    else factorize(n, 2, List())
  }

  @tailrec
  def factorize(n: Long, divisor: Long, acc: List[Long]): List[Long] = {
    if (n < 0) factorize(-n, divisor, -1 :: acc)
    else if (n == 1) acc.reverse
    else if (n % divisor == 0) factorize(n / divisor, divisor, divisor :: acc)
    else factorize(n, divisor + 1, acc)
  }

}
