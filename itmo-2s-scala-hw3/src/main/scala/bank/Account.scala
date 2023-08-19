package bank

import java.time.Clock

trait Account {
  def topUp(amount: Int): Option[Account]

  def withdraw(amount: Int): Option[Account]

  def operations: List[Operation]

  def statement: String

  def balance: Int
}

object Account {
  def createAccount(clock: Clock): Account = new AccountImpl(0, List(), clock)
}
