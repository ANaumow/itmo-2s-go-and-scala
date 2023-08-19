package rangeI

import (
	"strconv"
)

type ClosedRange struct {
	min int
	max int
}

func (r ClosedRange) Length() int {
	return r.max - r.min + 1
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func (r ClosedRange) Intersect(other RangeInt) RangeInt {
	if other.IsEmpty() {
		return EmptyRange{}
	} else {
		otherMinimum, ok := other.Minimum()
		if !ok {
			panic("other min")
		}

		otherMaximum, ok := other.Maximum()
		if !ok {
			panic("other max")
		}

		intersectMin := max(r.min, otherMinimum)
		intersectMax := min(r.max, otherMaximum)

		if intersectMin <= intersectMax {
			return ClosedRange{intersectMin, intersectMax}
		} else {
			return EmptyRange{}
		}
	}
}

func (r ClosedRange) Union(other RangeInt) (RangeInt, bool) {
	if other.IsEmpty() {
		return r, true
	} else if r.IsIntersect(other) {

		otherMinimum, ok := other.Minimum()
		if !ok {
			panic("other min")
		}

		otherMaximum, ok := other.Maximum()
		if !ok {
			panic("other max")
		}

		unionMin := min(r.min, otherMinimum)
		unionMax := max(r.max, otherMaximum)
		return ClosedRange{unionMin, unionMax}, true
	} else {
		return nil, false
	}
}

func (r ClosedRange) IsEmpty() bool {
	return false
}

func (r ClosedRange) ContainsInt(i int) bool {
	return i >= r.min && i <= r.max
}

func (r ClosedRange) ContainsRange(other RangeInt) bool {
	if other.IsEmpty() {
		return true
	} else {
		otherMinimum, ok := other.Minimum()
		if !ok {
			panic("other min")
		}

		otherMaximum, ok := other.Maximum()
		if !ok {
			panic("other max")
		}

		return otherMinimum >= r.min && otherMaximum <= r.max
	}
}

func (r ClosedRange) IsIntersect(other RangeInt) bool {
	if other.IsEmpty() {
		return false
	} else {
		otherMinimum, ok := other.Minimum()
		if !ok {
			panic("other min")
		}

		otherMaximum, ok := other.Maximum()
		if !ok {
			panic("other max")
		}

		return r.min <= otherMaximum && r.max >= otherMinimum
	}
}

func (r ClosedRange) ToSlice() []int {
	a := make([]int, r.max-r.min+1)
	for i := range a {
		a[i] = r.min + i
	}
	return a
}

func (r ClosedRange) Minimum() (int, bool) {
	return r.min, true
}

func (r ClosedRange) Maximum() (int, bool) {
	return r.max, true
}

func (r ClosedRange) ToString() string {
	return "[" + strconv.Itoa(r.min) + ", " + strconv.Itoa(r.max) + "]"
}
