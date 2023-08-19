package rangeI

type RangeInt interface {
	Length() int
	Intersect(other RangeInt) RangeInt
	Union(other RangeInt) (RangeInt, bool)
	IsEmpty() bool
	ContainsInt(i int) bool
	ContainsRange(other RangeInt) bool
	IsIntersect(other RangeInt) bool
	ToSlice() []int
	Minimum() (int, bool)
	Maximum() (int, bool)
	ToString() string
}

type RangeIntImpl struct {
}

func NewRangeInt(a, b int) RangeInt {
	if a > b {
		return EmptyRange{}
	} else {
		return ClosedRange{a, b}
	}
}
