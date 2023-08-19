package bank

trait BusinessLimitBank[F[_]] {
  def setLimit(id: String, amount: String): F[Unit]
  def getLimit(id: String): F[Int]
}
