package application

import cats.effect.IO
import cats.effect.kernel.Temporal
import cats.effect.std.{Console, Queue, Semaphore}
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.catsSyntaxFlatMapOps
import cats.{Applicative, Monad, Show}
import org.scalatest.Assertion
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.charset.Charset
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.matching.Regex

// Don't change this file
class ApplicationUpstreamSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {
  "Application" should "work for simple numbers" in {
    test(
      List(17, 103, 11),
      List(
        "17 = 17",
        "103 = 103",
        "11 = 11"
      ),
      4
    )
  }

  it should "work for negative" in {
    test(
      List(-17),
      List("-17 = -1 * 17"),
      1
    )
  }

  it should "work for min value" in {
    test(
      List(Long.MinValue),
      List(s"${Long.MinValue} = -1${" * 2".repeat(63)}"),
      1
    )
  }

  it should "work for zero" in {
    test(
      List(0),
      List(s"0 = 0"),
      10
    )
  }

  it should "work for max value" in {
    test(
      List(Long.MaxValue),
      List(s"${Long.MaxValue} = 7 * 7 * 73 * 127 * 337 * 92737 * 649657"),
      1
    )
  }

  it should "work for example" in {
    test(
      List(100, -17, 25, 38),
      List(
        "-17 = -1 * 17",
        "25 = 5 * 5",
        "100 = 2 * 2 * 5 * 5",
        "38 = 2 * 19"
      ),
      4
    )
  }

  it should "work for different numbers" in {
    test(
      List(Long.MaxValue, 1234, -1234, 4321, -4321, 65471, 97841, 898231),
      List(
        s"${Long.MaxValue} = 7 * 7 * 73 * 127 * 337 * 92737 * 649657",
        "97841 = 97841",
        "-1234 = -1 * 2 * 617",
        "-4321 = -1 * 29 * 149",
        "1234 = 2 * 617",
        "4321 = 29 * 149",
        "65471 = 7 * 47 * 199",
        "898231 = 898231"
      ),
      3
    )
  }

  private def test(
      input: List[Long],
      expectedResult: List[String],
      maxFibers: Int,
      timeout: FiniteDuration = 5.seconds
  ): IO[Assertion] =
    for {
      output    <- Queue.bounded[IO, String](input.size)
      semaphore <- Semaphore[IO](1)
      implicit0(consoleMock: Console[IO]) = new ConsoleMock[IO](output, semaphore)
      applicationFiber <-
        Application
          .application[IO](input, maxFibers)
          .timeout(timeout)
          .start

      _          <- applicationFiber.joinWithNever
      outputList <- output.tryTakeN(None)
    } yield {
      outputList.zipWithIndex.foreach {
        case (lineIndex(lineNum), index) => lineNum shouldBe s"line ${index + 1}"
        case (line, index) =>
          fail(s"Unexpected prefix in line '$line'; expected 'line ${index + 1},''")
      }

      val result = outputList.map {
        case lineResult(line) => line
        case other            => fail(s"Unexpected format in line '$other'")
      }

      result.sorted shouldBe expectedResult.sorted
    }

  private class ConsoleMock[F[_]: Temporal](
      output: Queue[F, String],
      semaphore: Semaphore[F]
  ) extends Console[F] {
    override def println[A](a: A)(implicit S: Show[A]): F[Unit] =
      Monad[F].ifM(semaphore.tryAcquire)(
        output.offer(S.show(a)) >> Temporal[F].sleep(100.milliseconds) >> semaphore.release,
        Applicative[F].pure(fail(s"Race condition in console operation"))
      )

    override def readLineWithCharset(charset: Charset): F[String] = ???

    override def print[A](a: A)(implicit S: Show[A]): F[Unit] = ???

    override def error[A](a: A)(implicit S: Show[A]): F[Unit] = ???

    override def errorln[A](a: A)(implicit S: Show[A]): F[Unit] = ???
  }

  private val lineIndex: Regex  = raw"^(line \d+).*".r
  private val lineResult: Regex = raw"^line \d+, ([0-9\s=\-*]+)".r
}
