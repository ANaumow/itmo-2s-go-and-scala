package mark

import cats.data.NonEmptyList

case class StatisticsImpl(studentList: NonEmptyList[Student]) extends Statistics {
  private val sourceTable: Map[String, List[Int]] = studentList
    .groupBy(_.name)
    .map { case (name, students) => (name, students.toList.map(_.mark)) }

  private val sumTable: Map[String, Int] = sourceTable.view.mapValues(_.sum).toMap

  private val avgTable: Map[String, Double] = sourceTable.map { case (name, marks) =>
    (
      name,
      marks.sum.toDouble / marks.length
    )
  }

  override def sumOpt(student: String): Option[Int] = sumTable.get(student)

  override def avgOpt(student: String): Option[Double] = avgTable.get(student)

  override def students: NonEmptyList[String] =
    NonEmptyList
      .fromList(
        studentList.toList.view
          .map(_.name)
          .distinct
          .toList
          .sortBy(-sumTable(_))
      )
      .get

  override lazy val sum: Int = sumTable.values.sum

  override lazy val median: Int = {
    val allMarksSorted = sourceTable.values.flatten.toList.sorted
    allMarksSorted(allMarksSorted.length / 2)
  }

  override lazy val mostFrequent: Int = {
    val flatten = sourceTable.values.flatten
    flatten
      .groupBy(identity)
      .map { case (k, v) => (k, v.toList.length) }
      .toList
      .sortBy(_.swap)
      .reverse
      .headOption
      .map(_._1)
      .getOrElse(throw new NoSuchElementException("mostFrequent"))
  }
}
