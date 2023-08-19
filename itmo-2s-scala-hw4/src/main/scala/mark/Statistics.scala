package mark

import cats.data.NonEmptyList

import java.io.FileWriter
import scala.io.Source
import scala.util.Using
import java.io.File
import java.nio.file.FileAlreadyExistsException

trait Statistics {
  def sumOpt(student: String): Option[Int]

  def avgOpt(student: String): Option[Double]

  def students: NonEmptyList[String]

  def sum: Int

  def median: Int

  def mostFrequent: Int
}

object Statistics {
  // students - сырые (не обработанные) данные из файла.
  def apply(students: List[Student]): Either[Throwable, Statistics] = NonEmptyList
    .fromList(students)
    .map(l => StatisticsImpl(l))
    .toRight(new IllegalArgumentException("empty list"))

  def apply(inputFileName: String): Either[Throwable, Statistics] = {
    val students: Either[Throwable, List[Student]] = Using(Source.fromFile(inputFileName)) {
      source =>
        source
          .getLines()
          .flatMap(s => Student.fromLine(s))
          .toList
    }.toEither

    students.flatMap(apply)
  }

  def calculate(
      inputFileName: String,
      outputFileName: String
  ): Either[Throwable, Unit] = {
    if (new File(outputFileName).exists()) {
      Left(new FileAlreadyExistsException(outputFileName))
    } else {
      Statistics
        .apply(inputFileName)
        .flatMap(state =>
          Using(new FileWriter(outputFileName)) { writer =>
            {
              val sum      = state.sum
              val median   = state.median
              val mostFreq = state.mostFrequent

              writer.write(f"$sum\t$median\t$mostFreq\n")

              state.students.toList.foreach(name => {
                val sum         = state.sumOpt(name).get
                val avg         = state.avgOpt(name).get
                val preparedAvg = (f"$avg%.2f").replace(",", ".")
                writer.write(s"$name\t$sum\t$preparedAvg\n")
              })

            }
          }.toEither
        )
    }

  }

}
