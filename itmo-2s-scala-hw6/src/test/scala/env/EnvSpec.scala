package env

import cats.Id
import cats.effect.{IO, Ref}
import cats.effect.unsafe.implicits.global
import env.LocalEnv.ToSetEnvOps
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnvSpec extends AnyFlatSpec with Matchers {
  // your tests here
  private val initMap: Map[String, String] = Map("a" -> "1", "b" -> "2")

  "Env.fromMap" should "work correctly" in {
    implicit val env: Env[Id] = Env.fromMap(initMap)

    env.find("a") shouldBe Id(Some("1"))
    env.find("b") shouldBe Id(Some("2"))
    env.find("c") shouldBe Id(None)
  }

  "Env.stub" should "work correctly" in {
    implicit val env: Env[Id] = Env.stub

    env.find("a") shouldBe Id(Some("scala"))
    env.find("b") shouldBe Id(Some("scala"))
    env.find("c") shouldBe Id(Some("scala"))
  }

  "LocalEnv" should "work with changes" in {
    implicit val env: Env[IO] = Env.fromMap(initMap)

    val storage = Ref.of[IO, Map[String, String]](Map.empty[String, String])
    val run = for {
      given Ref[IO, Map[String, String]] <- storage
      env                                <- IO.pure(LocalEnv.fromEnv(env))

      a1 <- env.find("a")
      _  <- env.set("a", "11")
      a2 <- env.find("a")
    } yield {
      a1 shouldBe Some("1")
      a2 shouldBe Some("11")
    }

    run.unsafeRunSync()
  }

  "str.setEnv" should "work" in {
    implicit val env: Env[IO] = Env.fromMap(initMap)

    val storage = Ref.of[IO, Map[String, String]](Map.empty[String, String])
    val run = for {
      given Ref[IO, Map[String, String]] <- storage
      env                                <- IO.pure(LocalEnv.fromEnv(env))
      _                                  <- "a".setEnv("11")
      a2                                 <- env.find("a")
    } yield {
      a2 shouldBe Some("11")
    }

    run.unsafeRunSync()
  }

}
