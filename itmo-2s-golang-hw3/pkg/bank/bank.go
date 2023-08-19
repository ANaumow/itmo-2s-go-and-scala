package bank

import (
	"fmt"
	"time"
)

const (
	TopUpOp OperationType = iota
	WithdrawOp
)

type OperationType int64

type Clock interface {
	Now() time.Time
}

func NewRealTime() *RealClock {
	return &RealClock{}
}

type RealClock struct{}

func (c *RealClock) Now() time.Time {
	return time.Now()
}

type Operation struct {
	OpTime   time.Time
	OpType   OperationType
	OpAmount int
	Balance  int
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
	TopUp(amount int) (Account, bool)
	Withdraw(amount int) (Account, bool)
	Operations() []Operation
	Statement() string
	Balance() int
}

// amount should me 0
func NewAccount(clock Clock) *AccountImpl {
	return &AccountImpl{clock, []Operation{}, 0}
}
