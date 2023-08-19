package fact

type InputImpl struct {
	n     int
	slice []int
}

func NewInputImpl(n int, slice []int) *InputImpl {
	return &InputImpl{n: n, slice: slice}
}

func (i InputImpl) N() int {
	return i.n
}

func (i InputImpl) Slice() []int {
	return i.slice
}
