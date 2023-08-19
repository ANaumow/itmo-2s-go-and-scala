package main

import "testing"

func TestFoo(t *testing.T) {
	a := 5
	b := 6
	got := Foo(a, b)
	want := a + b

	if got != want {
		t.Errorf("Foo is bad, want %d got %d", want, got)
	}
}
