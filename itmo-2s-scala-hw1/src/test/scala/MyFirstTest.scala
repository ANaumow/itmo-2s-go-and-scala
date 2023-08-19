import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyFirstTest extends AnyFlatSpec with Matchers {
  "A string" should "have length equal to its size" in {
    val str = "Hello, world!"
    str.length shouldEqual str.size
  }
}
