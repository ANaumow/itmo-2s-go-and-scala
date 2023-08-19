package bank

import (
	"strings"
)

type AccountImpl struct {
	clock      Clock
	operations []Operation
	balance    int
}

func (a *AccountImpl) TopUp(amount int) (Account, bool) {
	if amount <= 0 {
		return nil, false
	}

	a.balance = a.balance + amount

	a.operations = append(a.operations, Operation{
		OpTime:   a.clock.Now(),
		OpType:   TopUpOp,
		OpAmount: amount,
		Balance:  a.balance,
	})
	return a, true
}

func (a *AccountImpl) Withdraw(amount int) (Account, bool) {
	if amount <= 0 || amount > a.balance {
		return nil, false
	} else {
		a.balance = a.balance - amount
		a.operations = append(a.operations, Operation{
			OpTime:   a.clock.Now(),
			OpType:   WithdrawOp,
			OpAmount: amount,
			Balance:  a.balance,
		})
		return a, true
	}
}

func (a *AccountImpl) Operations() []Operation {
	return a.operations
}

func (a *AccountImpl) Statement() string {
	var lines []string

	for _, operation := range a.operations {
		lines = append(lines, operation.ToString())
	}
	return strings.Join(lines, "\n")
}

func (a *AccountImpl) Balance() int {
	return a.balance
}
