package env

import cats.Monad
import cats.effect.*
import cats.effect.std.Console
import cats.implicits.*

import scala.util.matching.Regex

object Application extends IOApp {
  // Метод run запрещается менять
  override def run(args: List[String]): IO[ExitCode] =
    application[IO].attempt
      .map {
        case Left(_)  => ExitCode.Error
        case Right(_) => ExitCode.Success
      }

  // Разрешается менять context bound функции
  def application[F[_]: Monad: Console: Sync]: F[Unit] = {
    val SET: Regex = "set\\s*((?=.)\\S+)\\s*((?=.)\\S+)".r
    val GET: Regex = "get\\s*((?=.)\\S+)".r

    def loop(env: LocalEnv[F]): F[String] = Monad[F].iterateWhile {
      for {
        input <- Console[F].readLine
        output <- input match {
          case SET(key, value) => env.set(key, value).as(Some(s"$key $value"))
          case GET(key)        => env.find(key)
          case _               => Monad[F].pure(None)
        }
        _ <- Monad[F].whenA(input != "exit") {
          Console[F].println(output.getOrElse("None"))
        }
      } yield input
    }(_ != "exit")

    for {
      given Ref[F, Map[String, String]] <- Ref.of[F, Map[String, String]](Map.empty[String, String])
      env = LocalEnv.fromEnv[F](Env.system)
      _ <- loop(env)
    } yield ()

  }

}
