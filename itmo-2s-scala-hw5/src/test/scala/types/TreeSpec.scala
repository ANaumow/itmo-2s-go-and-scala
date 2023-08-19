package types

import cats.implicits.{catsSyntaxSemigroup, toFunctorOps}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TreeSpec extends AnyFlatSpec with Matchers {

  val A: Tree[Int] = Branch(1, Leaf(2), Nil)
  val B: Tree[Int] = Leaf(3)
  val C: Tree[Int] = Branch(1, Branch(2, Leaf(10), Leaf(3)), Leaf(4))
  val D: Tree[Int] = Branch(5, Branch(6, Leaf(7), Leaf(8)), Leaf(9))
  val Z: Tree[Int] = Nil

  "Last |+|" should "combine correctly" in {
    C |+| D shouldBe Branch(
      1,
      Branch(2, Branch(10, Branch(5, Branch(6, Leaf(7), Leaf(8)), Leaf(9)), Nil), Leaf(3)),
      Leaf(4)
    )
  }

  "Last monoid" should "satisfy associativity law" in {
    (A |+| Z) |+| B shouldBe Branch(1, Branch(2, Leaf(3), Nil), Nil)
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
