package range

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// Don't change this file
class RangeUpstreamSpec extends AnyFlatSpec with Matchers with MockFactory {
  "Empty range" should "be empty" in {
    Range.empty.isEmpty shouldBe true
  }

  it should "not have maximum" in {
    Range.empty.maximum shouldBe None
  }

  it should "not have minimum" in {
    Range.empty.minimum shouldBe None
  }

  it should "eq to Empty" in {
    Range.empty == Range.empty shouldBe true
  }

  it should "eq to other Empty" in {
    Range.empty == testEmptyRange shouldBe true
  }

  it should "same Empty" in {
    Range.empty shouldBe Range.empty
  }

  it should "same as other Empty" in {
    Range.empty shouldBe testEmptyRange
  }

  it should "contains Empty range" in {
    Range.empty.contains(Range.empty) shouldBe true
  }

  it should "not contains non Empty range" in {
    Range.empty.contains(testRange) shouldBe false
  }

  it should "not contains 0" in {
    Range.empty.contains(0) shouldBe false
  }

  it should "intersect with Empty range" in {
    Range.empty.intersect(Range.empty) shouldBe Range.empty
  }

  it should "union with Empty range" in {
    Range.empty.union(Range.empty) shouldBe Some(Range.empty)
  }

  it should "have zero len" in {
    Range.empty.length shouldBe 0
  }

  it should "intersects with non empty range" in {
    Range.empty.intersect(testRange) shouldBe Range.empty
  }

  it should "intersects with empty range" in {
    Range.empty.intersect(testEmptyRange) shouldBe Range.empty
  }

  it should "return false for intersects with non empty range" in {
    Range.empty.isIntersect(testRange) shouldBe false
  }

  it should "return false for intersects with empty range" in {
    Range.empty.isIntersect(testEmptyRange) shouldBe false
  }

  it should "have union with non empty range" in {
    Some(Range.fromInterval(0, 1)) shouldBe Range.empty.union(testRange)
  }

  it should "have union with empty range" in {
    Some(Range.empty) shouldBe Range.empty.union(testEmptyRange)
  }

  "Empty range toList" should "be empty" in {
    Range.empty.toList shouldBe List.empty
  }

  "Empty range toString" should "be empty" in {
    Range.empty.toString shouldBe "∅"
  }

  it should "be same as ∅" in {
    s"${Range.empty}" shouldBe "∅"
  }

  "Non empty range" should "have zero len for dot" in {
    Range.fromInterval(0, 0).length shouldBe 1
  }

  "Non empty range [0, 1]" should "have non zero len" in {
    Range.fromInterval(0, 1).length shouldBe 2
  }

  it should "contains 0 and 1" in {
    Range.fromInterval(0, 1).contains(0) shouldBe true
    Range.fromInterval(0, 1).contains(1) shouldBe true
  }

  it should "be same as List(0, 1)" in {
    Range.fromInterval(0, 1).toList shouldBe List(0, 1)
  }

  it should "have union with empty Range" in {
    Range.fromInterval(0, 1).union(Range.empty) shouldBe Some(Range.fromInterval(0, 1))
  }

  it should "have empty intersect with empty Range" in {
    Range.fromInterval(0, 1).intersect(Range.empty) shouldBe Range.empty
  }

  "Range [-1, 3] toList" should "be same as (-1 to 3)" in {
    Range.fromInterval(-1, 3).toList shouldBe (-1 to 3).toList
  }

  "Range [-1, 3]" should "have non zero len" in {
    Range.fromInterval(-1, 3).length shouldBe (-1 to 3).toList.length
  }

  it should "not be empty" in {
    Range.fromInterval(-1, 3).isEmpty shouldBe false
  }

  it should "intersects with non empty range" in {
    Range.fromInterval(-1, 3).intersect(testRange) shouldBe Range.fromInterval(0, 1)
  }

  it should "intersects with range [-2, -1]" in {
    Range.fromInterval(-1, 3).intersect(Range.fromInterval(-2, -1)) shouldBe Range.fromInterval(
      -1,
      -1
    )
  }

  it should "intersects with empty range" in {
    Range.fromInterval(-1, 3).intersect(testEmptyRange) shouldBe Range.empty
  }

  it should "return true for intersects with non empty range" in {
    Range.fromInterval(-1, 3).isIntersect(testRange) shouldBe true
  }

  it should "return true for intersects with empty range" in {
    Range.fromInterval(-1, 3).isIntersect(testEmptyRange) shouldBe false
  }

  it should "return true for intersects with range [-2, -1]" in {
    Range.fromInterval(-1, 3).isIntersect(Range.fromInterval(-2, -1)) shouldBe true
  }

  it should "return true for intersects with range [-1, 3]" in {
    Range.fromInterval(-1, 3).isIntersect(Range.fromInterval(-1, 3)) shouldBe true
  }

  it should "return false for intersects with range [-3, -2]" in {
    Range.fromInterval(-1, 3).isIntersect(Range.fromInterval(-3, -2)) shouldBe false
  }

  it should "contains Empty range" in {
    Range.fromInterval(-1, 3).contains(testEmptyRange) shouldBe true
  }

  it should "contains range [0, 1]" in {
    Range.fromInterval(-1, 3).contains(testRange) shouldBe true
  }

  it should "not contains range [-2, 1]" in {
    Range.fromInterval(-1, 3).contains(Range.fromInterval(-2, 1)) shouldBe false
  }

  it should "contains 0" in {
    Range.fromInterval(-1, 3).contains(0) shouldBe true
  }

  it should "not contains -2" in {
    Range.fromInterval(-1, 3).contains(-2) shouldBe false
  }

  it should "have union with range [0, 1]" in {
    Range.fromInterval(-1, 3).union(testRange) shouldBe Some(Range.fromInterval(-1, 3))
  }

  it should "have union with empty range" in {
    Range.fromInterval(-1, 3).union(testEmptyRange) shouldBe Some(Range.fromInterval(-1, 3))
  }

  it should "have minimum -1" in {
    Range.fromInterval(-1, 3).minimum shouldBe Some(-1)
  }

  it should "have maximum 3" in {
    Range.fromInterval(-1, 3).maximum shouldBe Some(3)
  }

  "Range [-1, 3] toString" should "be [-1; 3]" in {
    Range.fromInterval(-1, 3).toString shouldBe "[-1; 3]"
  }

  it should "be same as [-1; 3]" in {
    s"${Range.fromInterval(-1, 3)}" shouldBe "[-1; 3]"
  }

  "Range [-3, -1]" should "not have intersects with range [0, 1]" in {
    Range.fromInterval(-3, -1).isIntersect(testRange) shouldBe false
  }

  it should "not intersects with [0, 1]" in {
    Range.fromInterval(-3, -1).intersect(testRange) shouldBe Range.empty
  }

  it should "not have union with range [0, 1]" in {
    Range.fromInterval(-3, -1).union(testRange) shouldBe None
  }

  it should "have union with range [-1, 0]" in {
    val range = TestRange(isEmpty = false, Some(-1), Some(0))

    Range.fromInterval(-3, -1).union(range) shouldBe Some(Range.fromInterval(-3, 0))
  }

  private lazy val testRange      = TestRange(isEmpty = false, Some(0), Some(1))
  private lazy val testEmptyRange = TestRange(isEmpty = true, None, None)

  private final case class TestRange(
      override val isEmpty: Boolean,
      override val minimum: Option[Int],
      override val maximum: Option[Int]
  ) extends Range {
    override def length: Int                        = ???
    override def intersect(other: Range): Range     = ???
    override def union(other: Range): Option[Range] = ???
    override def contains(i: Int): Boolean          = ???
    override def contains(other: Range): Boolean    = ???
    override def isIntersect(other: Range): Boolean = ???
    override def toList: List[Int]                  = ???
  }
}
