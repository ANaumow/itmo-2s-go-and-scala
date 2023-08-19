package mark

import (
	"math"
	"sort"
)

type StudentsStatisticImpl struct {
	sumTable       map[string]int
	avgTable       map[string]float32
	sum            int
	median         int
	mostFrequent   int
	sortedStudents []string
}

func NewStudentStatistic(students []Student) *StudentsStatisticImpl {
	sourceTable := make(map[string][]int)
	for _, s := range students {
		sourceTable[s.Name] = append(sourceTable[s.Name], s.Mark)
	}

	var allMarks []int
	for _, marks := range sourceTable {
		allMarks = append(allMarks, marks...)
	}
	sort.Ints(allMarks)

	sumTable := calcSumTable(sourceTable)
	avgTable := calcAvgTable(sourceTable, sumTable)
	sum := calcSum(sumTable)
	median := calcMedian(allMarks)
	mostFrequent := calcMostFrequent(allMarks)
	sortedStudents := calcSortedStudents(sumTable)

	return &StudentsStatisticImpl{
		sortedStudents: sortedStudents,
		sumTable:       sumTable,
		avgTable:       avgTable,
		sum:            sum,
		median:         median,
		mostFrequent:   mostFrequent,
	}
}

func calcSortedStudents(sumTable map[string]int) []string {
	pairs := make([][2]interface{}, 0, len(sumTable))
	for name, sum := range sumTable {
		pairs = append(pairs, [2]interface{}{name, sum})
	}

	sort.Slice(pairs, func(i, j int) bool {
		return pairs[i][1].(int) > pairs[j][1].(int)
	})

	var sortedStudents []string
	for _, item := range pairs {
		sortedStudents = append(sortedStudents, item[0].(string))
	}
	return sortedStudents
}

func calcMostFrequent(allMarks []int) int {
	frequencyMap := make([]int, 10, 10)

	for _, mark := range allMarks {
		frequencyMap[mark-1] += 1
	}

	var frequencyData [][2]int
	for mark, count := range frequencyMap {
		frequencyData = append(frequencyData, [2]int{mark + 1, count})
	}

	sort.Slice(frequencyData, func(i, j int) bool {
		if frequencyData[i][1] != frequencyData[j][1] {
			return frequencyData[i][1] > frequencyData[j][1]
		}
		return frequencyData[i][0] > frequencyData[j][0]
	})

	mostFrequent := frequencyData[0][0]
	return mostFrequent
}

func calcMedian(allMarks []int) int {
	median := allMarks[len(allMarks)/2]
	return median
}

func calcSum(sumTable map[string]int) int {
	sum := 0
	for _, s := range sumTable {
		sum += s
	}
	return sum
}

func calcAvgTable(sourceTable map[string][]int, sumTable map[string]int) map[string]float32 {
	avgTable := make(map[string]float32)
	for name, marks := range sourceTable {
		avgTable[name] = float32(math.Round(float64(float32(sumTable[name])/float32(len(marks))*100)) / 100)
	}
	return avgTable
}

func calcSumTable(sourceTable map[string][]int) map[string]int {
	sumTable := make(map[string]int)
	for name, marks := range sourceTable {
		sum := 0
		for _, m := range marks {
			sum += m
		}
		sumTable[name] = sum
	}
	return sumTable
}

func (s *StudentsStatisticImpl) SummaryByStudent(name string) (int, bool) {
	i, ok := s.sumTable[name]
	return i, ok
}

func (s *StudentsStatisticImpl) AverageByStudent(name string) (float32, bool) {
	i, ok := s.avgTable[name]
	return i, ok
}

func (s *StudentsStatisticImpl) Students() []string {
	return s.sortedStudents
}

func (s *StudentsStatisticImpl) Summary() int {
	return s.sum
}

func (s *StudentsStatisticImpl) Median() int {
	return s.median
}

func (s *StudentsStatisticImpl) MostFrequent() int {
	return s.mostFrequent
}
