package range

class ClosedRange(val min: Int, val max: Int) extends Range {

  // Размер интервала
  override def length: Int = max - min + 1

  // Пересечение интервалов
  override def intersect(other: Range): Range = {
    if (other.isEmpty) EmptyRange
    else {
      val intersectMin = Math.max(min, other.minimum.get)
      val intersectMax = Math.min(max, other.maximum.get)
      if (intersectMin <= intersectMax) new ClosedRange(intersectMin, intersectMax)
      else Range.empty
    }
  }

  // Объединение пересекающихся интервалов
  override def union(other: Range): Option[Range] = {
    if (other.isEmpty) {
      Some(this)
    } else if (isIntersect(other)) {
      val unionMin = Math.min(min, other.minimum.get)
      val unionMax = Math.max(max, other.maximum.get)
      Some(new ClosedRange(unionMin, unionMax))
    } else None
  }

  // Проверка интервала на пустоту
  override def isEmpty: Boolean = false

  // Проверка точки на вхождение в интервал
  override def contains(i: Int): Boolean = i >= min && i <= max

  // Проверка интервала на вхождение в другой
  override def contains(other: Range): Boolean = {
    if (other.isEmpty) true
    else other.minimum.get >= min && other.maximum.get <= max
  }

  // Проверка двух интервалов на пересечение
  override def isIntersect(other: Range): Boolean = {
    if (other.isEmpty) false
    else min <= other.maximum.get && max >= other.minimum.get
  }

  // Перечисление всех точек интервала в порядке возрастания
  override def toList: List[Int] = List.range(min, max + 1)

  // Вычисление минимума интервала
  override def minimum: Option[Int] = Some(min)

  // Вычисление максимума интервала
  override def maximum: Option[Int] = Some(max)

  // Вычисление строкового представления интервала в формате [-3; 7], для пустого используйте ∅
  override def toString: String = s"[$min; $max]"

  // Проверка двух интервалов на равенство
  override def equals(obj: Any): Boolean = obj match {
    case r: ClosedRange => min == r.min && max == r.max
    case _              => false
  }

}
