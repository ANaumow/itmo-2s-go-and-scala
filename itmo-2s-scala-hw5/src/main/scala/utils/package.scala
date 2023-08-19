import cats.Functor
import cats.implicits.{catsSyntaxSemigroup, toFunctorOps}
import cats.kernel.Semigroup

import scala.annotation.tailrec

package object utils {

  implicit final class RepeatSyntax[F[_], A](val other: F[A]) extends AnyVal {
    def times(repeat: Int)(implicit F: Functor[F], S: Semigroup[A]): F[A] =
      utils.times(other, repeat)
  }

  def times[F[_]: Functor, A: Semigroup](other: F[A], repeat: Int): F[A] = {
    other.map(value => times(value, value, repeat))
  }

  implicit final class SndSyntax[F[_], L, R](val other: F[(L, R)]) extends AnyVal {
    def fst()(implicit F: Functor[F]): F[L] =
      utils.fst(other.asInstanceOf[F[(L, _)]])
    def snd()(implicit F: Functor[F]): F[R] =
      utils.snd(other.asInstanceOf[F[(_, R)]])
  }

  def snd[F[_]: Functor, T](other: F[(_, T)]): F[T] = other.map(_._2)
  def fst[F[_]: Functor, T](other: F[(T, _)]): F[T] = other.map(_._1)

  @tailrec
  def times[F[_]: Functor, A: Semigroup](acc: A, init: A, repeat: Int): A =
    if (repeat <= 1) acc else times(acc |+| init, init, repeat - 1)

  implicit final class TupledSyntax[F[_], A](val other: F[A]) extends AnyVal {
    def tupled[B](f: A => B)(implicit F: Functor[F]): F[(A, B)] =
      utils.tupled(other, f)
  }

  def tupled[F[_]: Functor, A, B](other: F[A], f: A => B): F[(A, B)] =
    other.map(v => (v, f(v)))

  implicit final class ConstSyntax[F[_], A](val other: F[_]) extends AnyVal {
    def const(const: A)(implicit F: Functor[F]): F[A] = utils.const(const)(other)
  }

  def const[F[_]: Functor, T](const: T)(other: F[_]): F[T] =
    Functor[F].map(other)(_ => const)

}
