package env

import cats.Applicative

trait Env[F[_]] {
  def find(key: String): F[Option[String]]
}

// Разрешается менять context bound функций
object Env {
  def apply[F[_]](implicit env: Env[F]): Env[F] = env

  def stub[F[_]: Applicative]: Env[F] =
    _ => Applicative[F].pure(Some("scala"))

  def fromMap[F[_]: Applicative](context: Map[String, String]): Env[F] =
    (key: String) => Applicative[F].pure(context.get(key))

  implicit def system[F[_]: Applicative]: Env[F] =
    (key: String) => Applicative[F].pure(sys.env.get(key))
}
