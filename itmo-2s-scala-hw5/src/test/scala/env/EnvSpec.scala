package env

import env.Env.EnvMapSyntax
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnvSpec extends AnyFlatSpec with Matchers {

  "EnvImpl" should "correct get properties from map" in {
    val value1: Map[String, String] = Map(("a", "1"), ("b", "2"))
    val env: Env[Nothing]           = value1.toEnv
    env.getProperty("a") shouldBe Some("1")
    env.getProperty("c") shouldBe None
  }

}
