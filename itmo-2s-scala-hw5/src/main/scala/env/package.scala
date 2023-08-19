import cats.Functor
import cats.implicits.toFunctorOps

package object env {
  // В данном задании нужно реализовать хоть какую-то (но обязательно не бросающую исключения) реализацию для env,
  // в следующих дз сделаем её правильной.
  def getEnvs[F[_]: Functor: Env](envName: F[String]): F[Option[String]] = {
    val env: Env[F] = implicitly[Env[F]]
    envName.map(env.getProperty)
  }
}
