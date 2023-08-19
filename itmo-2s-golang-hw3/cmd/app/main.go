package main

import (
	b "hw3/pkg/bank"
	r "hw3/pkg/range"
)

func main() {
	r.NewRangeInt(1, 2)
	b.NewAccount(b.NewRealTime())
}
