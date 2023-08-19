package types

import cats.implicits.{catsSyntaxSemigroup, toFunctorOps}
import cats.kernel.Monoid
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PointSpec extends AnyFlatSpec with Matchers {

  val A: Point[Int] = Point(1, 2, 3)
  val B: Point[Int] = Point(2, 4, 6)
  val Z: Point[Int] = Monoid[Point[Int]].empty

  "Last monoid" should "satisfy associativity law" in {
    (A |+| Z) |+| B shouldBe A |+| (Z |+| B)
    (A |+| B) |+| Z shouldBe A |+| (B |+| Z)
  }

  it should "satisfy identity law" in {
    (A |+| Z) shouldBe A
    (Z |+| A) shouldBe A
  }

  "Last functor" should "satisfy composition law" in {
    val f: Int => Int = _ * 2
    val g: Int => Int = _ * 2
    A.map(f).map(g) shouldBe A.map(v => g(f(v)))
    Z.map(f).map(g) shouldBe Z.map(v => g(f(v)))
  }

  it should "satisfy identity law" in {
    A.map(identity) shouldBe A
    Z.map(identity) shouldBe Z
  }
}
