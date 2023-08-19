import cats.effect.Sync
import cats.{Applicative, ApplicativeError, Apply, FlatMap, Functor, Monad, MonadError, Monoid}
import cats.implicits.*

import scala.util.matching.Regex
//import cats.implicits.catsStdBimonadForFunction0.pure
//import cats.implicits.catsSyntaxApplicativeId
//import cats.implicits.catsSyntaxApplicativeByName
//import cats.implicits.catsSyntaxApply

Apply[Option].ap[Int, Int](None)(Some(4))

Functor[Option].map[Int, String => (Int, String)](Some(3))(a => (b: String) => (a, b))

val a: (Int, Int) => Int              = _ + _
val a0: ((Int, Int), Int, Int) => Int = (a, b, c) => a._1 + a._2 + b + c

val b = a.tupled

Apply[Option].map2(Some(2), Some(3))((a, b) => (a, b))

Apply[Option].product(Some[Int => Int](_ + 2), Some(3))

4.pure[Option]

val fy: Option[String => Int] = Some(_ => 2)
val y: String                 = "123"

val inl: (String => Int) => Int = f => f(y)
fy <*> Applicative[Option].pure(y) ==
  Applicative[Option].pure[(String => Int) => Int](inl) <*> fy

fy <*> Applicative[Option].pure(y) ==
  Applicative[Option].pure[(String => Int) => Int](f => f(y)) <*> fy

import cats.implicits.catsSyntaxFlatMapOps
Option(3) >> Option(2) // res: Option[Int] = Some(2)
Option(3) >> None      // res: Option[Int] = None
None >> Option(2)      // res: Option[Int] = None
Option(3) >>= (x => Some(x + 2))
import cats.implicits.{catsSyntaxEitherId, catsSyntaxFlatMapOps}
3.asRight[String] >> 4.asRight[String]
3.asRight[String] >> "sdf".asLeft[Int]
3.asRight[String] >> 2.asRight[String]
println("Hello left").asLeft[Unit] *> println("Hello world").asRight[Unit]

// e[u, u].flatMap()
// def >>[B](fb: => F[B])(implicit F: FlatMap[F]): F[B] = F.flatMap(fa)(_ => fb)

//

/*def flatMap[A1 >: A, B1](f: B => Either[A1, B1]): Either[A1, B1] = this match {
     case Right(b) => f(b)
     case _ => this.asInstanceOf[Either[A1, B1]]
}*/

// f(a => b) <*> f(a) == f(_ => _(a)) <*> f(a => b)
println("Hello world").asRight[String]
/*
def divide[F[_]](dividend: Int, divisor: Int)(implicit F: ApplicativeError[F, String]): F[Int] =
  if (divisor === 0) F.raiseError("division by zero")
  else F.pure(dividend / divisor)
// a / b + c / d
def eval[F[_]](a: Int, b: Int, c: Int, d: Int)(implicit F: MonadError[F, String]): F[Int] =
  for {
    first  <- divide(a, b)
    second <- divide(c, d)
  } yield first + second

def wrapped[F[_]: Sync]: F[Unit] =
  Sync[F].delay(System.out.println("Hello, World"))

eval(6, 2, 9, 3)

import cats.effect.SyncIO.syncForSyncIO*/
//wrapped.apply()

val setRegex: Regex = "set ((?=.)\\S*) ((?=.)\\S*)".r
val input = Some("set a b")

val ans = input match {
  case Some(setRegex(key, value)) => (key + value)
  case _ => "gowno"
}
