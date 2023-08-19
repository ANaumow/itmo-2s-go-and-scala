package types

import cats.implicits.catsSyntaxSemigroup
import cats.{Functor, Monoid, Semigroup}

final case class Point[+T](x: T, y: T, z: T)

object Point {
  implicit val pointFunctor: Functor[Point] = new Functor[Point] {
    def map[A, B](fa: Point[A])(f: A => B): Point[B] =
      Point(f(fa.x), f(fa.y), f(fa.z))
  }

  implicit def pointMonoid[T: Monoid]: Monoid[Point[T]] = new Monoid[Point[T]] {

    private val emptyValue: T = Monoid[T].empty

    override def empty: Point[T] = new Point[T](emptyValue, emptyValue, emptyValue)

    override def combine(a: Point[T], b: Point[T]): Point[T] =
      Point(a.x |+| b.x, a.y |+| b.y, a.z |+| b.z)
  }

  implicit def pointSemigroup[T: Semigroup]: Semigroup[Point[T]] = new Semigroup[Point[T]] {

    override def combine(a: Point[T], b: Point[T]): Point[T] =
      Point(a.x |+| b.x, a.y |+| b.y, a.z |+| b.z)
  }

}
