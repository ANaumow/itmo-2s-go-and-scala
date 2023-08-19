package rangeI

type EmptyRange struct {
}

func (r EmptyRange) Length() int {
	return 0
}

func (r EmptyRange) Intersect(other RangeInt) RangeInt {
	return EmptyRange{}
}

func (r EmptyRange) Union(other RangeInt) (RangeInt, bool) {
	return other, true
}

func (r EmptyRange) IsEmpty() bool {
	return true
}

func (r EmptyRange) ContainsInt(i int) bool {
	return false
}

func (r EmptyRange) ContainsRange(other RangeInt) bool {
	return other.IsEmpty()
}

func (r EmptyRange) IsIntersect(other RangeInt) bool {
	return false
}

func (r EmptyRange) ToSlice() []int {
	return []int{}
}

func (r EmptyRange) Minimum() (int, bool) {
	return 0, false
}

func (r EmptyRange) Maximum() (int, bool) {
	return 0, false
}

func (r EmptyRange) ToString() string {
	return ""
}
