package bank

import java.time.format.DateTimeFormatter._
import java.time.{Clock, LocalDateTime}
import scala.annotation.tailrec

class AccountImpl(override val balance: Int, override val operations: List[Operation], clock: Clock)
    extends Account {

  override def topUp(amount: Int): Option[Account] = {
    Option.when(amount >= 0)(
      new AccountImpl(
        balance + amount,
        Operation(LocalDateTime.now(clock), amount) :: operations,
        clock
      )
    )
  }

  override def withdraw(amount: Int): Option[Account] = {
    Option.when(!(amount < 0) && !(amount > balance))(
      new AccountImpl(
        balance - amount,
        operations :+ Operation(LocalDateTime.now(clock), -amount),
        clock
      )
    )
  }

  override def statement: String = "Time Amount Balance" + statement("", operations, 0)

  @tailrec
  private def statement(cache: String, operations: List[Operation], balance: Int): String = {
    operations match {
      case Nil => cache
      case op :: ops =>
        val time       = op.time.format(ofPattern("dd.MM.yyyy HH:mm:ss"))
        val amount     = (if (op.amount > 0) "+" else "") + op.amount
        val newBalance = balance + op.amount
        statement(cache + s"\n$time $amount $newBalance", ops, newBalance)
    }
  }

}
