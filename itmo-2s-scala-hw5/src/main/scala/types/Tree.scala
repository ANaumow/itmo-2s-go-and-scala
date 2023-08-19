package types

import cats.implicits.{catsSyntaxSemigroup, toFunctorOps}
import cats.{Functor, Monoid}

sealed trait Tree[+T]

final case class Leaf[+T](value: T)                                  extends Tree[T]
final case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]
object Nil                                                           extends Tree[Nothing]

object Tree {

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      fa match {
        case Nil             => Nil
        case Leaf(v)         => Leaf(f(v))
        case Branch(v, l, r) => Branch(f(v), l.map(f), r.map(f))
      }
    }
  }

  implicit def treeMonoid[T]: Monoid[Tree[T]] = new Monoid[Tree[T]] {
    override def empty: Tree[T] = Nil

    override def combine(x: Tree[T], y: Tree[T]): Tree[T] = {
      x match {
        case Leaf(value) =>
          y match {
            case Nil => x
            case _   => Branch(value = value, left = y, right = Nil)
          }
        case Nil => y
        case Branch(value, left, right) =>
          Branch(value, left |+| y, right)
      }
    }
  }

}
