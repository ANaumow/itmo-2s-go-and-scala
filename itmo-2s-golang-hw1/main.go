package main

import (
	"fmt"
)

func Foo(a, b int) int {
	return a + b
}

func main() {
	fmt.Printf("Hello! 5 + 6 = %d\n", Foo(5, 6))
}
