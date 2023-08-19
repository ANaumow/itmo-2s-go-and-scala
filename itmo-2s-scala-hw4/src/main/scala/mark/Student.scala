package mark

import scala.util.matching.Regex

final case class Student(
    name: String,
    mark: Int
)

object Student {
  val regex: Regex = "^((?=.)[^\t]*)\t([1-9]|10)$".r

  def fromLine(line: String): Option[Student] = line match {
    case regex(name, rawMark) => Some(Student(name, rawMark.toInt))
    case _                    => None
  }

  /*def fromLine(line: String): Option[Student] = Option
    .when(regex.matches(line))(line)
    .map(line => {
      val Array(name, rawAge: String) = line.split("\t")
      Student(name, rawAge.toInt)
    })*/
}
