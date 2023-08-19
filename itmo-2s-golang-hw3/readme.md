# HW3 (Golang) - Коллекции

## Часть 1 - Range

### Дан фрагмент кода

```golang
package rangeI

type RangeInt interface {
  Length() int
  Intersect(other RangeInt) RangeInt
  Union(other RangeInt) (RangeInt, bool)
  IsEmpty() bool //считаем пустым, если [a, b], где a > b
  ContainsInt(i int) bool
  ContainsRange(other RangeInt) bool
  IsIntersect(other RangeInt) bool
  ToSlice() []int // отсортирован
  Minimum() (int, bool)
  Maximum() (int, bool)
  ToString() string // "" если пусто, иначе [4,7]
}

// также реализуйте конструктор
func NewRangeInt(a, b int) /*your type*/ {}
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

### Задание

Реализуйте `RangeInt`, представляющий замкнутый целочисленный интервал.

Напишите тесты покрывающие все методы.

## Часть 2 - Bank

### Дан следующий фрагмент кода

```golang
package bank

import (...)

const (
  TopUpOp OperationType = iota
  WithdrawOp
)

type OperationType int64

type Clock interface {
  Now() time.Time
}

type Operation struct {
  OpTime   time.Time
  OpType   OperationType
  OpAmount int
  Balance  int // баланс на конец операции
}

func (o Operation) ToString() string {
  var format string
  if o.OpType == TopUpOp {
    format = `%s +%d %d`
  } else {
    format = `%s -%d %d`
  }
  return fmt.Sprintf(format, o.OpTime.String()[:19], o.OpAmount, o.Balance)
}

type Account interface {
  TopUp(amount int) (Account, bool) //false если amount <= 0
  Withdraw(amount int) (Account, bool) //false если amount >= 0
  Operations() []Operation // храним все TopUp и Withdraw. Не сохранять, если вернули false
  Statement() string // Для переводу в строку исспользуем метод ToString()
  Balance() int
}

// конструктор
func NewAccount(clock Clock) /*yourtype*/ {}
```

Запрещается в приведённом фрагменте кода:

* Менять сигнатуры;
* Менять модификаторы доступа;
* Менять пакеты;

### Задание

Реализуйте `Account`, представляющий модель банковского расчетного счета. Счет ведется в условных
единицах и не допускает достижения отрицательного баланса.

Пример формата выписки:

```text
2023-03-13 11:19:07 +100 100
2023-03-13 11:22:07 -50 50
```

Напишите тесты покрывающие все методы.

### StyleCode:

* Тесты пишите в соотвестввующих файлах, ниже специального комментария;
* Используйте стандартные пакеты

### Как сдавать:

* Сделайте fork репозитория `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/golang/hw3` (через интерфейс gitlab)
    в `https://itmo-b.gitlab.yandexcloud.net/itmo-spring-2023/students/<your-handle>/golang/hw3`;
* В качестве родительской ветки для решения используйте ветку `tests` (**важно!**);
* Добавьте ваше решение в ветку `hw`;
* Открыть _Merge request_ из ветки `hw` в ветку `master`;
* Дождаться когда пайплайн станет зелёным, после этого можно назначить в ревьюеры _Merge request_ `@YaroslavZhurba`;
* `18.03.2023` на практике будут выложены тесты, потребуется подтянуть изменения из `upstream` репозитория,
    сделать `rebase` на ветку `tests`, и запушить изменения в ваш репозиторий (если вы всё сделали правильно, новые тесты
    автоматически запустятся на CI);
* После успешного прохождения тестов (pipeline станет зелёным) работа считается сданной;
* Если будут вопросы по времени сдачи дз - мы будем ориентироваться на время последнего вашего действия в _Merge
    request_.

