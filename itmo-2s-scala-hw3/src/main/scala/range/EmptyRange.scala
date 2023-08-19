package range

object EmptyRange extends Range {
  override def length: Int = 0

  override def intersect(other: Range): Range = EmptyRange

  override def union(other: Range): Option[Range] =
    if (other.isEmpty) Some(Range.empty)
    else Some(Range.fromInterval(other.minimum.get, other.maximum.get))

  override def isEmpty: Boolean = true

  override def contains(i: Int): Boolean = false

  override def contains(other: Range): Boolean = other.isEmpty

  override def isIntersect(other: Range): Boolean = false

  override def toList: List[Int] = List.empty

  override def minimum: Option[Int] = None

  override def maximum: Option[Int] = None

  override def toString: String = "∅"

  override def equals(obj: Any): Boolean = obj match {
    case r: Range => r.isEmpty
    case _        => false
  }
}
