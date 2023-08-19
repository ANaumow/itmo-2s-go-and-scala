package application

import bank.{BusinessLimitBank, LagBusinessLimitBank}
import cats.Monad
import cats.effect.{Async, ExitCode, IO, IOApp, Ref}
import cats.effect.kernel.Fiber
import cats.effect.std.{Console, Random, Semaphore, Supervisor}
import cats.implicits.{catsSyntaxFlatMapOps, toFlatMapOps, toFunctorOps}

import scala.util.matching.Regex

object Application extends IOApp {

  // Запрещается менять
  override def run(args: List[String]): IO[ExitCode] =
    service[IO].attempt
      .map {
        case Left(_)  => ExitCode.Error
        case Right(_) => ExitCode.Success
      }

  // Разрешается менять
  private def service[F[_]: Async: Console]: F[Unit] =
    Random
      .scalaUtilRandomSeedInt[F](42)
      .map { implicit random => LagBusinessLimitBank[F](9, 10) }
      .flatMap(application(_, 2))

  // Разрешается менять context bound функции
  def application[F[_]: Monad: Console: Async](
      bank: BusinessLimitBank[F],
      maxConnections: Int
  ): F[Unit] = {
    Supervisor[F](await = true).use { supervisor =>
      for {
        mapRef    <- Ref.of(Map.empty[String, Fiber[F, Throwable, Unit]])
        semaphore <- Semaphore[F](maxConnections)

        setLimit = (id: String, amount: String) => {
          supervisor
            .supervise(semaphore.permit.use { _ =>
              bank.setLimit(id, amount) >> mapRef.update(map => map - id)
            })
            .flatTap(f => mapRef.update(_ + ((id, f))))
            .void
        }

        _ <- Monad[F].foreverM {
          Console[F].readLine.flatMap {
            case set(id, amount) => setLimit(id, amount)
            case cancel(id) =>
              mapRef.access >>= { case (map, setter) =>
                map.get(id) match {
                  case Some(fiber) => fiber.cancel >> setter(map - id).void
                  case None        => setLimit(id, "0")
                }
              }
            case command => Console[F].println(s"Unknown command '$command'")
          }
        }.void
      } yield ()
    }

  }

  private val set: Regex    = raw"set\s+(\d+)\s+(\d+)".r
  private val cancel: Regex = raw"cancel\s+(\d+)".r
}
