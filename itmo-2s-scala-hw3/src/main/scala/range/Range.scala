package range

trait Range {
  def length: Int

  def intersect(other: Range): Range

  def union(other: Range): Option[Range]

  def isEmpty: Boolean

  def contains(i: Int): Boolean

  def contains(other: Range): Boolean

  def isIntersect(other: Range): Boolean

  def toList: List[Int]

  def minimum: Option[Int]

  def maximum: Option[Int]
}

object Range {
  def empty: Range = EmptyRange

  def fromInterval(min: Int, max: Int): Range = new ClosedRange(min, max)
}
