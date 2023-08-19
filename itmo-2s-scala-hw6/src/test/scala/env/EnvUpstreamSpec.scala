package env

import cats.Id
import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

// Don't change this file
class EnvUpstreamSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {
  "Env" should "always return scala" in {
    implicit val env: Env[IO] = Env.stub

    getEnv("HTTP_PORT").map(_ shouldBe Some("scala"))
  }

  it should "always return scala (Id)" in {
    implicit val env: Env[Id] = Env.stub

    getEnv("HTTP_PORT") shouldBe Id(Some("scala"))
  }

  it should "be created from map" in {
    implicit val env: Env[Id] = Env.fromMap(
      Map(
        "HTTP_PORT"                                                   -> "8080",
        "The Ultimate Question of Life, the Universe, and Everything" -> "42"
      )
    )

    getEnv(Id("HTTP_PORT")) shouldBe Id(Some("8080"))
  }

  it should "use exist env variable (JAVA_HOME)" in {
    implicit val env: Env[IO] = Env.system[IO]

    getEnv("JAVA_HOME")
      .map(_ shouldNot be(None))
  }

  it should "use exist env variable (from ci)" in {
    implicit val env: Env[IO] = Env.system[IO]

    getEnv("some_global_var")
      .map(_ shouldBe Some("some_value"))
  }

  it should "return None for unknown env variable" in {
    implicit val env: Env[IO] = Env.system[IO]

    getEnv("some_unknown_key")
      .map(_ shouldBe None)
  }

  private def getEnv[F[_]: Env](
      key: String
  ): F[Option[String]] = Env[F].find(key)
}
