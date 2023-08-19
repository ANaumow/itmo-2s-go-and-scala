package fact

import (
	"math"
	"reflect"
	"testing"
)

func TestFactorize25(t *testing.T) {
	input := 25
	want := []int{5, 5}
	got := Factorize(input)
	if !reflect.DeepEqual(want, got) {
		t.Errorf("want %v, got %v", want, got)
	}
}

func TestFactorize100(t *testing.T) {
	input := 100
	want := []int{2, 2, 5, 5}
	got := Factorize(input)
	if !reflect.DeepEqual(want, got) {
		t.Errorf("want %v, got %v", want, got)
	}
}

func TestFactorizeNegative25(t *testing.T) {
	input := -25
	want := []int{-1, 5, 5}
	got := Factorize(input)
	if !reflect.DeepEqual(want, got) {
		t.Errorf("want %v, got %v", want, got)
	}
}

func TestFactorizeMinInt(t *testing.T) {
	input := math.MinInt
	want := []int{-1}
	intSize := 32 << (^uint(0) >> 63)
	for i := 0; i < intSize-1; i++ {
		want = append(want, 2)
	}
	got := Factorize(input)
	if !reflect.DeepEqual(want, got) {
		t.Errorf("want %v, got %v", want, got)
	}
}
