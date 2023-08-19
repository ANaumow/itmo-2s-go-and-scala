package main

import (
	"hw7/pkg/fact"
	"os"
)

func main() {
	nums := []int{100, -17, 25, 38}
	n := 2
	fact.FactorizationImpl{}.Work(fact.NewInputImpl(n, nums), os.Stdout)
}
