package types

import cats.{Functor, Monoid}

final case class Last[+T](value: Option[T])

object Last {

  implicit def lastMonoid[T]: Monoid[Last[T]] = new Monoid[Last[T]] {
    override def empty: Last[T] = Last(None)

    override def combine(x: Last[T], y: Last[T]): Last[T] =
      Last(y.value.orElse(x.value))
  }

  implicit val lastFunctor: Functor[Last] = new Functor[Last] {
    def map[A, B](fa: Last[A])(f: A => B): Last[B] =
      Last(fa.value.map(f))
  }

}
