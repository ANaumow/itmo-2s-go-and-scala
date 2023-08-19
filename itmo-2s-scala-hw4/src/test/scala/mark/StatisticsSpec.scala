package mark

import cats.data.NonEmptyList
import org.scalamock.scalatest.MockFactory
import org.scalatest.Inside.inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.FileNotFoundException
import scala.io.Source
import scala.util.{Try, Using}

class StatisticsSpec extends AnyFlatSpec with Matchers with MockFactory {

  "Student.fromLine" should "return correct Student on correct input" in {
    Student.fromLine("Пример\t10") shouldBe Some(Student("Пример", 10))
  }

  it should "return None on incorrect input" in {
    Student.fromLine("Пример10\t") shouldBe None
  }

  "Statistics.apply on inputFileName" should "throw exception on nonexistent file" in {
    inside(Statistics.apply("asd")) { case Left(e) =>
      e shouldBe a[FileNotFoundException]
    }
  }

  "Statistics" should "get sumOpt correctly" in {
    statistics.sumOpt("A") shouldBe Some(3)
    statistics.sumOpt("B") shouldBe Some(5)
    statistics.sumOpt("C") shouldBe None
  }

  it should "get avgOpt correctly" in {
    statistics.avgOpt("A").get shouldBe 1.5 +- 0.01
    statistics.avgOpt("B").get shouldBe 2.5 +- 0.01
    statistics.avgOpt("C") shouldBe None
  }

  it should "get median correctly" in {
    statistics.median shouldBe 4
  }

  it should "get mostFrequent correctly" in {
    statistics.mostFrequent shouldBe 7
  }

  it should "get student correctly" in {
    statistics.students shouldBe NonEmptyList.fromList(List("D", "B", "A")).get
  }

  "Statistics.calculate" should "work properly" in {
    Statistics.calculate("input_1.tsv", "output_1_got.tsv")

    val i = Using.Manager { use =>
      {
        val lines1 = use(Source.fromFile("output_1_got.tsv")).getLines()
        val lines2 = use(Source.fromFile("output_1.tsv")).getLines()

        (lines1 zip lines2).count { case (a, b) => a != b }
      }
    }

    i shouldBe Try(0)
  }

  private lazy val statistics: Statistics = Statistics
    .apply(
      List(
        Student("A", 1),
        Student("A", 2),
        Student("B", 1),
        Student("B", 4),
        Student("D", 7),
        Student("D", 7)
      )
    )
    .toOption
    .get

}
