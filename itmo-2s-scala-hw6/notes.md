




Applicative:

def unit: F[Unit] = pure(())
def whenA[A](cond: Boolean)(f: => F[A]): F[Unit] = if (cond) void(f) else unit
def unlessA[A](cond: Boolean)(f: => F[A]): F[Unit] = if (cond) unit else void(f)
def void[A]   (fa: F[A])                 : F[Unit] = as(fa, ())
def as[A, B]  (fa: F[A], b: B)           : F[B]    = map(fa)(_ => b)

val opt = Option(println("Hello world"))
opt.whenA(cond = false) // same as Applicative[Option].whenA(cond = false)(opt)
out>: Hello world // здесь пишется потомучто Option(println("Hello world")) не лези

lazy val opt1 = Option(println("Hello world 1"))
opt1.whenA(cond = false)
out>:

(println("Hello world 2")).pure[Option].whenA(false)
out>: // здесь не напишется потому что whenA[A](cond: Boolean)(f: => F[A]): F[Unit] принимает как лези


Apply:

trait Apply[F[_]] extends Functor[F] {
    def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] // Have alias <*>
    def productR[A, B](fa: F[A])(fb: F[B]): F[B] // Have alias *>
    def productL[A, B](fa: F[A])(fb: F[B]): F[A] // Have alias <*
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}



val someF: Option[Int => Long] = Some(_.toLong + 1L)
val noneF: Option[Int => Long] = None
val someInt: Option[Int] = Some(3)
val noneInt: Option[Int] = None

scala> Apply[Option].ap(someF)(someInt)
res0: Option[Long] = Some(4)

scala> Apply[Option].ap(noneF)(someInt)
res1: Option[Long] = None

scala> Apply[Option].ap(someF)(noneInt)
res2: Option[Long] = None

scala> Apply[Option].ap(noneF)(noneInt)
res3: Option[Long] = None

# Реализации Apply[Option]:

Apply[Option].ap[Int, Int](Some(_ + 3))(Some(4))
ответ Some(7)

def productR[A, B](fa: F[A])(fb: F[B]): F[B] = // Have alias *>
    ap(as(fa, { (b: B) => b }))(fb)

def productL[A, B](fa: F[A])(fb: F[B]): F[A] = // Have alias <*
    map2(fa, fb)((a, _) => a)
    
def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
    map(product(fa, fb))(f.tupled)
    
override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    ap(map(fa)(a => (b: B) => (a, b)))(fb)


# Это productL:
Option(3) <* Option(2)
fa = Some(3)
fb = Some(2)

map2(Some(3), Some(2))((a, _) => a)
map2(Some(3), Some(2))((a, _) => a)
map(product(Some(3), Some(2)))(((a, _) => a).tupled)
map(ap(map(Some(3))(a => (b: B) => (a, b)))(Some(2)))(((a, _) => a).tupled)

map(product(Some(3), Some(2)))(((a, _) => a).tupled)
map(ap(map(Some(3))(a => (b: B) => (a, b)))(Some(2)))(((a, _) => a).tupled)

map(ap(map(Some(3))(a => (b: B) => (a, b)))(Some(2)))(((a, _) => a).tupled)
- map(Some(3))(a => (b: B) => (a, b)) = Some((b: B) => (3, b))
map(ap(Some((b: B) => (3, b)))(Some(2)))(((a, _) => a).tupled)
- ap(Some((b: B) => (3, b)))(Some(2)) = Some(3, 2)
map(Some((3, 2)))(((a, _) => a).tupled)
Some(3)

# Это productR:
fa = Some(4)
fb = Some(7)

as(fa, { (b: B) => b })
Some(4) -> Some(B => B)
ap(Some(B => B))(Some(7))
Some(7)

Option(3) *> Option(2) // res: Option[Int] = Some(2)
Option(3) *> None      // res: Option[Int] = None
None *> Option(2)      // res: Option[Int] = None !!!!!!
Option(3) <* Option(2) // res: Option[Int] = Some(3)
Option(3) <* None      // res: Option[Int] = None
None <* Option(2)      // res: Option[Int] = None

Подробнее про None *> Option(2)

def productR[A, B](fa: F[A])(fb: F[B]): F[B] =
    ap(as(fa, { (b: B) => b }))(fb) // Have alias *>

fa = None
fb = Option(2)

as(fa, { (b: B) => b })
None -> None
ap(None)(Some(7))
Apply[Option].ap[Int, Int](None)(Some(4))
ответ - None


def unit: F[Unit] = pure(())
def unlessA[A](cond: Boolean)(f: => F[A]): F[Unit] = if (cond) unit else void(f)
def void[A]   (fa: F[A])                 : F[Unit] = as(fa, ())
def as[A, B]  (fa: F[A], b: B)           : F[B]    = map(fa)(_ => b)
final def map[B](f: A => B): Option[B] =
    if (isEmpty) None else Some(f(this.get))


fy <*> Applicative[Option].pure(y) ==
Applicative[Option].pure[(String => Int) => Int](f => f(y)) <*> fy


Monad:

def >>=[B] (f: A => F[B])(implicit F: FlatMap[F]): F[B] = F.flatMap(fa)(f)
def >>[B](fb: => F[B])(implicit F: FlatMap[F]): F[B] = F.flatMap(fa)(_ => fb)

r1 >> r2 = r2
r1 >> l1 = l1
l1 >> r1 = l1
l1 >> l2 = l1

def flatMap[A1 >: A, B1](f: B => Either[A1, B1]): Either[A1, B1] = this match {
   case Right(b) => f(b)
   case _        => this.asInstanceOf[Either[A1, B1]]
}



Wrap side effect to effect:
• Synchronous (returns or throws)
    • F.delay(...)  для неблокирующих операций (изменение AtomicRef)
    • F.blocking(...) для блокирующих (чтение из файла, запрос по сети)
    • F.interruptible(...)
    • F.interruptibleMany(...)
• Asynchronous (invokes a callback)
    • F.async or F.async_

def wrapped[F[_] : Sync]: F[Unit] =
    Sync[F].delay(System.out.println("Hello, World"))

Sync позволяет оборачивать сайд эффекты



import cats.effect.IO
import cats.effect.kernel.Sync
import cats.effect.unsafe.implicits.global
import cats.implicits.{toFlatMapOps, toFunctorOps}
def example[F[_]: Sync]: F[Unit] = {
val helloWorld1 = Sync[F].delay(println("Hello world 1"))
val helloWorld3 = Sync[F].pure(println("Hello world 3"))
for {
_ <- helloWorld1
_ <- Sync[F].pure(println("Hello world 2"))
_ <- helloWorld3
} yield ()
}
example[IO].unsafeRunSync()


