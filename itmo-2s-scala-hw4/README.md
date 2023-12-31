# HW4 (Scala) - I/O

## Анализ оценок студентов

### Дан следующий фрагмент кода

`mark/Statistics.scala`

```scala
package mark

import cats.data.NonEmptyList

final case class Student(name: String, mark: Int)

trait Statistics {
  def sumOpt(student: String): Option[Int]

  def avgOpt(student: String): Option[Double]

  def students: NonEmptyList[String]

  def sum: Int

  def median: Int

  def mostFrequent: Int
}

object Statistics {
  // students - сырые (не обработанные) данные из файла.
  def apply(students: List[Student]): Either[Throwable, Statistics] = ???

  def apply(inputFileName: String): Either[Throwable, Statistics] = ???

  def calculate(
    inputFileName: String,
    outputFileName: String
  ): Either[Throwable, Unit] = ???
}
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

### Задание

На вход программе подаётся путь к файлу формата `tsv`, строки файла содержат студента и его балл (пример строки
`Примеров Пример Примерович\t5` - `\t` в данном примере табуляция); Студент - любая последовательность символов
(кроме служебных и управляющих, таких как `\n\r\t`); Оценка - целое число от 1 до 10. Считается что файл содержит
корректные имена и оценки; требуется игнорировать строки не подходящие под заданный формат
(пример нарушения формата `Примеров Пример Примерович5\t`).

Требуется посчитать:

- Сумму баллов по студенту (считать что полных тёзок нет);
- Средний балл по студенту (с точностью 2 знака после запятой);
- Отсортированный список студентов по убыванию суммы баллов;
- Сумму всех баллов;
- Медиану по выставленным баллам (при подсчёте индекса используем округление вверх);
- Самый часто выставленный балл (если несколько - берём наибольший);

Напишите тесты покрывающие функциональность.

#### Input:

* `inputFileName` путь к файлу в формате `tsv`;
* `outputFileName` путь к файлу в формате `tsv`;

#### Output

Если программа завершилась успешно, в файле `outputFileName` содержится вывод, иначе программа должна вернуть ошибку.

Формат вывода:

* Первая строка содержит 3 числа записанных через разделитель `\t`, в порядке:
    - Сумма всех баллов;
    - Медиана по выставленным баллам;
    - Самый часто выставленный балл;
* Начиная со второй строки выводится список студентов в порядке убывания суммы баллов, каждая строка содержит данные
  записанные через разделитель `\t`, в порядке:
    - Студент;
    - Сумма баллов по студенту;
    - Средний балл по студенту;
* Допускается перевод строки в конце файла

#### Пример

* [Пример файла с оценками](./input_1.tsv)
* [Пример файла содержащий вывод](./output_1.tsv)

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

* Сделайте fork репозитория `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/scala/hw4` (через интерфейс gitlab)
  в `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/students/<your-handle>/scala/hw4`;
* В качестве родительской ветки для решения используйте ветку `tests` (**важно!**);
* Добавьте ваше решение в ветку `hw`;
* Открыть _Merge request_ из ветки `hw` в ветку `tests`;
* Дождаться когда пайплайн станет зелёным, после этого можно назначить в ревьюеры _Merge request_ `@geny200`;
* После дедлайна будут выложены тесты, потребуется подтянуть изменения из `upstream` репозитория,
  сделать `rebase` на ветку `tests`, и запушить изменения в ваш репозиторий (если вы всё сделали правильно, новые тесты
  автоматически запустятся на CI);
* После успешного прохождения тестов (pipeline станет зелёным) работа считается сданной;
* Если будут вопросы по времени сдачи дз - мы будем ориентироваться на время последнего вашего действия в _Merge
  request_.
