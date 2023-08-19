package bank

import cats.Applicative
import cats.implicits.catsSyntaxApplicativeId

final class StubBusinessLimitBank[F[_]: Applicative] extends BusinessLimitBank[F] {
  def setLimit(id: String, amount: String): F[Unit] = ().pure[F]

  def getLimit(id: String): F[Int] = 0.pure[F]
}
