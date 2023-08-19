package env
// Разрешается менять данный файл как угодно

trait Env[F[_]] {
  def getProperty(name: String): Option[String]
}

case class EnvImpl[F[_]](map: Map[String, String]) extends Env[F] {
  override def getProperty(name: String): Option[String] = map.get(name)
}

object Env {
  implicit final class EnvMapSyntax(val self: Map[String, String]) extends AnyVal {
    def toEnv[F[_]]: Env[F] = EnvImpl[F](self)
  }

  def stub[F[_]]: Env[F] = (_: String) => Some("scala")

  def sysEnv: EnvImpl[Nothing] = EnvImpl[Nothing](sys.env)
}
