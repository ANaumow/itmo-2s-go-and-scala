package bank

import cats.Functor
import cats.effect.kernel.Temporal
import cats.effect.std.Random
import cats.implicits.{toFlatMapOps, toFunctorOps}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

private final class LagBusinessLimitBank[F[_]: Functor](sleep: F[Unit])
    extends BusinessLimitBank[F] {
  def setLimit(id: String, amount: String): F[Unit] = sleep

  def getLimit(id: String): F[Int] = sleep.as(0)
}

object LagBusinessLimitBank {
  def apply[F[_]: Temporal: Random](maxSecondsLag: Int): BusinessLimitBank[F] =
    new LagBusinessLimitBank(sleep(0, maxSecondsLag))

  def apply[F[_]: Temporal: Random](
      minSecondsLag: Int,
      maxSecondsLag: Int
  ): BusinessLimitBank[F] =
    new LagBusinessLimitBank(sleep(minSecondsLag, maxSecondsLag))

  def apply[F[_]: Temporal](lag: FiniteDuration): BusinessLimitBank[F] =
    new LagBusinessLimitBank(Temporal[F].sleep(lag))

  private def sleep[F[_]: Temporal: Random](minSleep: Int, maxSleep: Int): F[Unit] =
    Random[F]
      .betweenInt(minSleep, maxSleep)
      .map(_.seconds)
      .flatMap(Temporal[F].sleep)
}
