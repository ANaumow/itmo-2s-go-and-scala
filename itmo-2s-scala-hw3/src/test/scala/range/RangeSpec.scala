package range

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RangeSpec extends AnyFlatSpec with Matchers {

  val range1: Range = Range.fromInterval(1, 5)
  val range2: Range = Range.fromInterval(3, 7)

  "Range" should "return correct length" in {
    range1.length shouldBe 5
  }

  it should "intersect two ranges correctly" in {
    range1.intersect(range2).toList shouldBe List(3, 4, 5)
    range1.intersect(Range.fromInterval(6, 9)) shouldBe EmptyRange
  }

  it should "return correct union of two ranges" in {
    range1.union(range2) shouldBe Some(Range.fromInterval(1, 7))
    range1.union(Range.empty) shouldBe Some(range1)
  }

  it should "return empty range when intersect with empty range" in {
    range1.intersect(Range.empty) shouldBe Range.empty
  }

  it should "return the correct answer if a point is in the range" in {
    range1.contains(3) shouldBe true
    range1.contains(6) shouldBe false
  }

  it should "return the correct answer if a range is inside another range" in {
    range1.contains(Range.fromInterval(2, 4)) shouldBe true
    range1.contains(Range.fromInterval(0, 4)) shouldBe false
  }

  it should "return the correct answer if two ranges intersect" in {
    range1.isIntersect(range2) shouldBe true
    range1.isIntersect(Range.fromInterval(6, 9)) shouldBe false
  }

  it should "return a sorted list of all points in the range" in {
    range1.toList shouldBe List(1, 2, 3, 4, 5)
  }

  it should "return the minimum value in the range" in {
    range1.minimum shouldBe Some(1)
    Range.empty.minimum shouldBe None
  }

  it should "return the maximum value in the range" in {
    range1.maximum shouldBe Some(5)
    Range.empty.maximum shouldBe None
  }

  it should "return the correct string representation of the range" in {
    range1.toString shouldBe "[1; 5]"
    Range.empty.toString shouldBe "∅"
  }

  it should "return the correct answer for equality of two ranges" in {
    range1.equals(Range.fromInterval(1, 5)) shouldBe true
    range1.equals(Range.fromInterval(1, 6)) shouldBe false
  }

}
