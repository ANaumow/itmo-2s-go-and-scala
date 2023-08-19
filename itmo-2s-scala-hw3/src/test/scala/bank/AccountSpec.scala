package bank

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalamock.scalatest.MockFactory

import java.time.{Clock, Instant, ZoneId}

class AccountSpec extends AnyFlatSpec with Matchers with MockFactory {

  "Account" should "return correct statement" in {
    val clock = mock[Clock]
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:00.00Z")).once()
    (clock.getZone _).expects().returns(ZoneId.of("Z"))
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:01.00Z")).once()
    (clock.getZone _).expects().returns(ZoneId.of("Z"))

    val account = Account.createAccount(clock)
    val actual  = account.topUp(500).flatMap(_.withdraw(100)).map(_.statement).get
    actual shouldBe "Time Amount Balance\n28.09.2021 13:37:00 +500 500\n28.09.2021 13:37:01 -100 400"
  }

  "Account balance" should "return correct balance" in {
    val clock = Clock.systemDefaultZone()

    val account = Account.createAccount(clock)
    val actual  = account.topUp(500).flatMap(_.withdraw(100)).map(_.balance)
    actual shouldBe Some(400)
  }

  "Account withdraw" should "return None when money is not enough" in {
    val clock = Clock.systemDefaultZone()

    val account = Account.createAccount(clock)
    val actual  = account.withdraw(500)
    actual shouldBe None
  }

}
