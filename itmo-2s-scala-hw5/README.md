# HW5 (Scala) - FP (Monoid, Semigroup, Functor)

## Играемся с Monoid, Semigroup и Functor

### Дан следующие описания типов

```scala
package types

final case class Last[+T](value: Option[T])

final case class Point[+T](x: T, y: T, z: T)

sealed trait Tree[+T]

final case class Leaf[+T](value: T) extends Tree[T]

final case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

object Nil extends Tree[Nothing]
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

### Задание

* Реализуйте для типов `Last`, `Three` и `Point` инстансы `Semigroup`, `Monoid` и `Functor` (не забудьте что должны
  выполняться соответствующие _законы_).

### Даны следующие сигнатуры функций

```scala
package object utils {
  def const[F[_] : Functor, T](const: T)(other: F[_]): F[T] = ???

  def fst[F[_] : Functor, T](other: F[(T, _)]): F[T] = ???

  def snd[F[_] : Functor, T](other: F[(_, T)]): F[T] = ???

  def tupled[F[_] : Functor, A, B](other: F[A], f: A => B): F[(A, B)] = ???

  def times[F[_] : Functor, A: Semigroup](other: F[A], repeat: Int): F[A] = ???
}
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

**PS**:

* Для использования синтаксиса сделайте нужный импорт:
    * `import cats.implicits.toFunctorOps`
    * `import cats.implicits.catsSyntaxSemigroup`
    * `import cats.implicits.catsSyntaxMonoid`

### Задание

Напишите реализацию функций исходя из их сигнатуры.

## Создаём свой typeclass

### Даны следующие сигнатуры функций

```scala
package object env {
  def getEnvs[F[_] : Functor : Env](envName: String): F[Option[String]] = ???
}
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

### Задание

* Напишите свой typeclass `Env` (умеет безопасно доставать переменные окружения по имени), и добавьте три реализации:
    * `Env` который возвращает только переменные из Map переданной при инициализации;
    * `Env` который возвращает всегда "scala";
    * `Env` который возвращает переменные из окружения;
* Добавьте синтаксис для `Map[String, String]` - метод `toEnv`;

### StyleCode:

* Тест классы именуются `<ClassName>Spec`, где `<ClassName>` - класс к которому пишутся тесты;
* Тест классы находятся в том же пакете, что и класс к которому пишутся тесты;
* Каждый тест должен быть в отдельном test suite;
* Использовать java коллекции запрещается;
* Использовать `mutable` коллекции запрещается;
* Использовать `var` запрещается;
* Использование `this` запрещается;
* Использование `return` запрещается.

### Как сдавать:

* Сделайте fork репозитория `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/scala/hw5` (через интерфейс gitlab)
  в `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/students/<your-handle>/scala/hw5`;
* В качестве родительской ветки для решения используйте ветку `tests` (**важно!**);
* Добавьте ваше решение в ветку `hw`;
* Открыть _Merge request_ из ветки `hw` в ветку `tests`;
* Дождаться когда пайплайн станет зелёным, после этого можно назначить в ревьюеры _Merge request_ `@geny200`;
* После дедлайна будут выложены тесты, потребуется подтянуть изменения из `upstream` репозитория,
  сделать `rebase` на ветку `tests`, и запушить изменения в ваш репозиторий (если вы всё сделали правильно, новые тесты
  автоматически запустятся на CI); не забудьте обновить так же ветку `tests` в вашем репозитории - проверьте что _diff_
  в вашем _Merge request_ содержит только ваши изменения и не содержит изменений из upstream репозитория;
* После успешного прохождения тестов (pipeline станет зелёным) работа считается сданной;
* Если будут вопросы по времени сдачи дз - мы будем ориентироваться на время последнего вашего действия в _Merge
  request_.
