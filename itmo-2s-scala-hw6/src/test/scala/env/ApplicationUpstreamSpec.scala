package env

import cats.Show
import cats.effect.IO
import cats.effect.std.{Console, Queue}
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.Assertion
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.charset.Charset
import scala.concurrent.duration.{DurationInt, FiniteDuration}

// Don't change this file
class ApplicationUpstreamSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {
  "Application" should "exit" in {
    test(
      List("exit"),
      List.empty
    )
  }

  it should "save vars" in {
    test(
      List("get HTTP_PORT", "set HTTP_PORT 8080", "get HTTP_PORT", "exit"),
      List("None", "HTTP_PORT 8080", "8080")
    )
  }

  it should "work correct for example" in {
    test(
      List(
        "get HTTP_PORT",
        "set HTTP_PORT 8080",
        "get HTTP_PORT",
        "set HTTP_PORT  80",
        "get HTTP_PORT",
        "exit"
      ),
      List(
        "None",
        "HTTP_PORT 8080",
        "8080",
        "HTTP_PORT 80",
        "80"
      )
    )
  }

  it should "not save local env var in global context" in {
    test(
      List("set HTTP_PORT 8080", "exit"),
      List("HTTP_PORT 8080")
    ) >> test(
      List("get HTTP_PORT", "exit"),
      List("None")
    )
  }

  it should "get env from global context" in {
    test(
      List("get some_global_var", "exit"),
      List("some_value")
    )
  }

  it should "rewrite env from global context" in {
    test(
      List("get some_global_var", "set some_global_var   42", "get some_global_var", "exit"),
      List("some_value", "some_global_var 42", "42")
    )
  }

  private def test(
      inputList: List[String],
      expectedList: List[String],
      timeout: FiniteDuration = 5.seconds
  ): IO[Assertion] =
    for {
      input  <- Queue.bounded[IO, String](inputList.size)
      output <- Queue.bounded[IO, String](expectedList.size)
      consoleMock: Console[IO] = new ConsoleMock[IO](input, output)
      fiber <- {
        implicit val console: Console[IO] = consoleMock
        Application
          .application[IO]
          .timeout(timeout)
          .productR(output.tryTakeN(None))
          .start
      }
      _      <- input.tryOfferN(inputList)
      output <- fiber.joinWithNever
    } yield output shouldBe expectedList

  private class ConsoleMock[F[_]](
      input: Queue[F, String],
      output: Queue[F, String]
  ) extends Console[F] {
    override def readLineWithCharset(charset: Charset): F[String] =
      input.take

    override def println[A](a: A)(implicit S: Show[A]): F[Unit] =
      output.offer(S.show(a))

    override def print[A](a: A)(implicit S: Show[A]): F[Unit]   = ???
    override def error[A](a: A)(implicit S: Show[A]): F[Unit]   = ???
    override def errorln[A](a: A)(implicit S: Show[A]): F[Unit] = ???
  }
}
