package bank

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{Clock, Instant, LocalDateTime, ZoneId}

// Don't change this file
class AccountUpstreamSpec extends AnyFlatSpec with Matchers with MockFactory {
  "Account" should "create account with balance 0" in {
    val clock = mock[Clock]

    Account.createAccount(clock).balance shouldBe 0
  }

  "Account topUp" should "change the balance" in {
    val clock = mock[Clock]
    (clock.instant _).expects().returns(Instant.parse("2000-01-01T17:23:12.00Z")).once()
    (clock.getZone _).expects().returns(ZoneId.of("Z"))

    val account = Account.createAccount(clock)

    account.topUp(1).map(_.balance) shouldBe Some(1)
  }

  it should "not accept negative amount" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    account.topUp(-1) shouldBe None
  }

  "Account withdraw" should "not accept negative amount" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    account.withdraw(-1) shouldBe None
  }

  it should "not accept negative balance" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.instant _).expects().returns(Instant.parse("2000-01-01T17:23:12.00Z")).once()
    (clock.getZone _).expects().returns(ZoneId.of("Z"))

    account
      .topUp(42)
      .flatMap(_.withdraw(43))
      .map(_.balance) shouldBe None
  }

  it should "change balance" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.instant _).expects().returns(Instant.parse("2000-01-01T17:23:12.00Z")).twice()
    (clock.getZone _).expects().returns(ZoneId.of("Z")).anyNumberOfTimes()

    account
      .topUp(42)
      .flatMap(_.withdraw(41))
      .map(_.balance) shouldBe Some(1)
  }

  it should "allow zero balance" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.instant _).expects().returns(Instant.parse("2000-01-01T17:23:12.00Z")).twice()
    (clock.getZone _).expects().returns(ZoneId.of("Z")).anyNumberOfTimes()

    account
      .topUp(42)
      .flatMap(_.withdraw(42))
      .map(_.balance) shouldBe Some(0)
  }

  "Account statement" should "have correct format" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:00.00Z")).once()
    (clock.getZone _).expects().returns(ZoneId.of("Z"))

    account
      .topUp(500)
      .map(_.statement) shouldBe Some(
      """Time Amount Balance
        |28.09.2021 13:37:00 +500 500""".stripMargin
    )
  }

  it should "have correct format for many operations" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.getZone _).expects().returns(ZoneId.of("Z")).anyNumberOfTimes()
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:00.00Z")).once()
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:01.00Z")).once()
    (clock.instant _).expects().returns(Instant.parse("2022-02-11T00:12:57.00Z")).once()

    account
      .topUp(500)
      .flatMap(_.withdraw(100))
      .flatMap(_.withdraw(400))
      .map(_.statement) shouldBe Some(
      """Time Amount Balance
        |28.09.2021 13:37:00 +500 500
        |28.09.2021 13:37:01 -100 400
        |11.02.2022 00:12:57 -400 0""".stripMargin
    )
  }

  it should "contains three operations" in {
    val clock   = mock[Clock]
    val account = Account.createAccount(clock)

    (clock.getZone _).expects().returns(ZoneId.of("Z")).anyNumberOfTimes()
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:00.00Z")).once()
    (clock.instant _).expects().returns(Instant.parse("2021-09-28T13:37:01.00Z")).once()
    (clock.instant _).expects().returns(Instant.parse("2022-02-11T00:12:57.00Z")).once()

    val operations = account
      .topUp(500)
      .flatMap(_.withdraw(100))
      .flatMap(_.withdraw(400))
      .map(_.operations)
      .getOrElse(List.empty)

    operations should have length 3
    operations should contain(Operation(LocalDateTime.parse("2021-09-28T13:37:00.00"), 500))
    operations should contain(Operation(LocalDateTime.parse("2021-09-28T13:37:01.00"), -100))
    operations should contain(Operation(LocalDateTime.parse("2022-02-11T00:12:57.00"), -400))
  }
}
