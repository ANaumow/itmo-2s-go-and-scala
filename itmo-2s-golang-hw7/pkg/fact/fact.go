package fact

import "io"

type Input interface {
	N() int       // n - число горутин
	Slice() []int // слайс чисел, которые необходимо факторизовать
}

type Factorization interface {
	Work(Input, io.Writer) error
}

func NewInput(goroutinesNum int, numbers []int) Input {
	return NewInputImpl(goroutinesNum, numbers)
}

func NewFactorization() Factorization {
	return FactorizationImpl{}
}
