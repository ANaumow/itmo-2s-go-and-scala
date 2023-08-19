package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UtilsSpec extends AnyFlatSpec with Matchers {

  "TupleSyntax" should "tuple with int" in {
    Option(12).tupled(_ * 2) shouldBe Some((12, 24))
  }

  it should "tuple with string" in {
    Option("12").tupled(_.toInt) shouldBe Some(("12", 12))
  }

  "FstSyntax and SndSyntax" should "work correctly" in {
    Option(("12", 12)).fst() shouldBe Option("12")
    Option(("12", 12)).snd() shouldBe Option(12)
  }

  "ConstSyntax" should "work correctrly" in {
    Option("ab").const(32).map(_ * 2) shouldBe Some(64)
  }

}
