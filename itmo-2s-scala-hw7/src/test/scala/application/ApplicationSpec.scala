package application

import application.Application.factorize
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ApplicationSpec extends AnyFlatSpec with Matchers {

  "factorize" should "work with 25" in {
    factorize(25) shouldBe List(5, 5)
  }

  it should "work with 17" in {
    factorize(17) shouldBe List(17)
  }

  it should "work with 38" in {
    factorize(38) shouldBe List(2, 19)
  }

  it should "work with 100" in {
    factorize(100) shouldBe List(2, 2, 5, 5)
  }

}
