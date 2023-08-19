package env

import cats.implicits.*
import cats.effect.*

trait LocalEnv[F[_]] extends Env[F] {
  def set(key: String, value: String): F[Unit]
}

case class LocalEnvImpl[F[_]: Sync](env: Env[F], mapRef: Ref[F, Map[String, String]])
    extends LocalEnv[F] {

  override def set(key: String, value: String): F[Unit] =
    mapRef.update(_ + (key -> value))

  override def find(key: String): F[Option[String]] = for {
    map         <- mapRef.get
    alternative <- env.find(key)
  } yield map.get(key).orElse(alternative)

}

// Разрешается менять context bound функций
object LocalEnv {
  def apply[F[_]](implicit env: LocalEnv[F]): LocalEnv[F] = env

  def fromEnv[F[_]: Sync](env: Env[F])(implicit mapRef: Ref[F, Map[String, String]]): LocalEnv[F] =
    LocalEnvImpl(env, mapRef)

  implicit final class ToSetEnvOps[F[_]](private val key: String) extends AnyVal {
    // Example: "HTTP_PORT".setEnv("8080")
    // Разрешается добавлять implicit аргументы функции
    def setEnv(
        value: String
    )(implicit env: Env[F], f: Sync[F], mapRef: Ref[F, Map[String, String]]): F[Unit] =
      fromEnv(env).set(key, value)
  }
}
