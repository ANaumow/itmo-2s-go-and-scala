package mark

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

var testInput = `A	1
A	2
B	1
B	4
D	7
D	7
`

func value(value interface{}, ok bool) interface{} {
	if !ok {
		panic("not ok")
	}
	return value
}

func ok(value interface{}, ok bool) interface{} {
	return ok
}

func Test_StudentFromLine_ReturnCorrectStudentOnCorrectInput(t *testing.T) {
	got := NewStudent("Пример\t10")
	want := Student{"Пример", 10}
	assert.Equal(t, got.Name, want.Name)
	assert.Equal(t, got.Mark, want.Mark)
}

func Test_StatisticsShouldGetSumOptCorrectly(t *testing.T) {
	statistics, _ := ReadStudentsStatistic(mockReader(testInput))
	assert.Equal(t, value(statistics.SummaryByStudent("A")), 3)
	assert.Equal(t, value(statistics.SummaryByStudent("B")), 5)
	assert.Equal(t, ok(statistics.SummaryByStudent("C")), false)
}

func Test_StatisticsGetAvgOptCorrectly(t *testing.T) {
	statistics, _ := ReadStudentsStatistic(mockReader(testInput))
	assert.Equal(t, float32(1.5), value(statistics.AverageByStudent("A")))
	assert.Equal(t, float32(2.5), value(statistics.AverageByStudent("B")))
	assert.Equal(t, ok(statistics.AverageByStudent("C")), false)
}

func Test_StatisticsGetMedianCorrectly(t *testing.T) {
	statistics, _ := ReadStudentsStatistic(mockReader(testInput))
	assert.Equal(t, 4, statistics.Median())
}

func Test_StatisticsGetMostFrequentCorrectly(t *testing.T) {
	statistics, _ := ReadStudentsStatistic(mockReader(testInput))
	assert.Equal(t, 7, statistics.MostFrequent())
}

func Test_StatisticsGetStudentCorrectly(t *testing.T) {
	statistics, _ := ReadStudentsStatistic(mockReader(testInput))
	assert.Equal(t, statistics.Students(), []string{"D", "B", "A"})
}
