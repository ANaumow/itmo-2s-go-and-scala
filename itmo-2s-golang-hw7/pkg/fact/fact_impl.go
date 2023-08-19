package fact

import (
	"fmt"
	"golang.org/x/sync/errgroup"
	"io"
	"math"
	"strconv"
	"strings"
	"sync"
)

type FactorizationImpl struct {
}

func (f FactorizationImpl) Work(input Input, writer io.Writer) error {
	var mutex sync.Mutex

	n := input.N()
	line := 1
	in := make(chan int, 1)
	g := new(errgroup.Group)
	wg := sync.WaitGroup{}
	wg.Add(n)

	for i := 0; i < n; i++ {
		g.Go(func() error {
			for x := range in {
				ds := Factorize(x)
				strSlice := make([]string, len(ds))
				for i, v := range ds {
					strSlice[i] = strconv.Itoa(v)
				}

				mutex.Lock()

				str := fmt.Sprintf("line %d, %d = %s\n", line, x, strings.Join(strSlice, " * "))
				line += 1
				_, err := io.WriteString(writer, str)
				if err != nil {
					return err
				}

				mutex.Unlock()
			}
			wg.Done()
			return nil
		})
	}

	for _, i := range input.Slice() {
		in <- i
	}
	close(in)

	wg.Wait()
	return g.Wait()
}

func Factorize(n int) []int {
	if n == 0 {
		return []int{0}
	} else if n == 1 {
		return []int{1}
	} else if n == -1 {
		return []int{-1, 1}
	} else if n == math.MinInt {
		return append([]int{-1, 2}, factorizeSafe(-(n / 2))...)
	} else if n < 0 {
		return append([]int{-1}, factorizeSafe(-n)...)
	}

	return factorizeSafe(n)
}

// n >= 2
func factorizeSafe(n int) []int {
	var acc []int
	x := n

	for divisor := 2; divisor <= n && x != 1; divisor++ {
		for x%divisor == 0 {
			acc = append(acc, divisor)
			x = x / divisor
		}
	}

	return acc
}
