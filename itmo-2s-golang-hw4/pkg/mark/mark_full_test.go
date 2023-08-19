package mark

import (
	"io"
	"strings"
	"testing"

	"github.com/stretchr/testify/assert"
)

// Don't modify this file! Here will be tests from course

func mockReader(str string) io.Reader {
	return strings.NewReader(str)
}

// ================ SummaryByStudent =========

type TestCase_summaryByStudent struct {
	input         string
	studentName   string
	isWantSummary bool
	wantSummary   int
}

func (tc TestCase_summaryByStudent) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotSummary, isGotSummary := statistics.SummaryByStudent(tc.studentName)
	assert.Equal(t, tc.isWantSummary, isGotSummary)
	if tc.isWantSummary {
		assert.Equal(t, tc.wantSummary, gotSummary)
	}
}

func Test_summaryByStudent_Sum(t *testing.T) {
	TestCase_summaryByStudent{
		input: `Примеров Пример Примерович	5
Примеров Пример Примерович	6
Примеров Пример	7`,
		studentName:   "Примеров Пример Примерович",
		isWantSummary: true,
		wantSummary:   11,
	}.Run(t)
}

func Test_summaryByStudent_Sum_1(t *testing.T) {
	TestCase_summaryByStudent{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		studentName:   "Примеров Пример Примерович1",
		isWantSummary: true,
		wantSummary:   25,
	}.Run(t)
}

func Test_summaryByStudent_Sum_Arabian(t *testing.T) {
	TestCase_summaryByStudent{
		input: `عباس	5
عباس	6
Примеров Пример	7`,
		studentName:   "عباس",
		isWantSummary: true,
		wantSummary:   11,
	}.Run(t)
}

func Test_summaryByStudent_Sum_Skip(t *testing.T) {
	TestCase_summaryByStudent{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович1
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		studentName:   "Примеров Пример Примерович1",
		isWantSummary: true,
		wantSummary:   25,
	}.Run(t)
}

func Test_summaryByStudent_NoStudent(t *testing.T) {
	TestCase_summaryByStudent{
		input:         `Примеров Пример Примерович1	9`,
		studentName:   "Примеров Пример Примерович2",
		isWantSummary: false,
		wantSummary:   0,
	}.Run(t)
}

// ================ AverageByStudent =========

type TestCase_averageByStudent struct {
	input         string
	studentName   string
	isWantAverage bool
	wantAverage   float32
}

func (tc TestCase_averageByStudent) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotAverage, isGotAverage := statistics.AverageByStudent(tc.studentName)
	assert.Equal(t, tc.isWantAverage, isGotAverage)
	if tc.isWantAverage {
		assert.Equal(t, tc.wantAverage, gotAverage)
	}
}

func Test_AveragebyStudent_Sum(t *testing.T) {
	TestCase_averageByStudent{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		studentName:   "Примеров Пример Примерович1",
		isWantAverage: true,
		wantAverage:   6.67,
	}.Run(t)
}

func Test_AveragebyStudent_NoStudent(t *testing.T) {
	TestCase_averageByStudent{
		input:         `Примеров Пример Примерович1	9`,
		studentName:   "Примеров Пример Примерович2",
		isWantAverage: false,
		wantAverage:   0,
	}.Run(t)
}

// ================ Students =================

type TestCase_students struct {
	input        string
	wantStudents []string
}

func (tc TestCase_students) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotStudents := statistics.Students()
	assert.Equal(t, tc.wantStudents, gotStudents)
}

func Test_Students_OneStudent(t *testing.T) {
	TestCase_students{
		input:        `Примеров Пример Примерович1	9`,
		wantStudents: []string{"Примеров Пример Примерович1"},
	}.Run(t)
}

func Test_Students_SomeStudents(t *testing.T) {
	TestCase_students{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович3	1
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		wantStudents: []string{
			"Примеров Пример Примерович1",
			"Примеров Пример Примерович2",
			"Примеров Пример Примерович3",
		},
	}.Run(t)
}

func Test_Students_IgnoreBadLines(t *testing.T) {
	TestCase_students{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович3	1
Примеров Пример Примерович4
Примеров Пример Примерович5	42
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		wantStudents: []string{
			"Примеров Пример Примерович1",
			"Примеров Пример Примерович2",
			"Примеров Пример Примерович3",
		},
	}.Run(t)
}

// ================ Summary ==================

type TestCase_summary struct {
	input       string
	wantSummary int
}

func (tc TestCase_summary) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotSummary := statistics.Summary()
	assert.Equal(t, tc.wantSummary, gotSummary)
}

func Test_Summary_OneStudent(t *testing.T) {
	TestCase_summary{
		input:       `Примеров Пример Примерович1	9`,
		wantSummary: 9,
	}.Run(t)
}

func Test_Summary_SomeStudents(t *testing.T) {
	TestCase_summary{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	7
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович3	1
Примеров Пример Примерович1	6
Примеров Пример Примерович2	2`,
		wantSummary: 35,
	}.Run(t)
}

// ================ Median ===================

type TestCase_median struct {
	input      string
	wantMedian int
}

func (tc TestCase_median) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotMedian := statistics.Median()
	assert.Equal(t, tc.wantMedian, gotMedian)
}

func Test_Median_OneStudent(t *testing.T) {
	TestCase_median{
		input:      `Примеров Пример Примерович1	9`,
		wantMedian: 9,
	}.Run(t)
}

func Test_Median_SomeStudentsEven(t *testing.T) {
	TestCase_median{
		input: `Примеров Пример Примерович1	2
Примеров Пример Примерович2	1
Примеров Пример Примерович1	6
Примеров Пример Примерович1	4
Примеров Пример Примерович1	3
Примеров Пример Примерович3	5`,
		wantMedian: 4,
	}.Run(t)
}

func Test_Median_SomeStudentsOdd(t *testing.T) {
	TestCase_median{
		input: `Примеров Пример Примерович1	2
Примеров Пример Примерович2	1
Примеров Пример Примерович2	7
Примеров Пример Примерович1	6
Примеров Пример Примерович1	4
Примеров Пример Примерович1	3
Примеров Пример Примерович3	5`,
		wantMedian: 4,
	}.Run(t)
}

// ================ MostFrequent =============

type TestCase_mostFrequent struct {
	input            string
	wantMostFrequent int
}

func (tc TestCase_mostFrequent) Run(t *testing.T) {
	statistics, err := ReadStudentsStatistic(mockReader(tc.input))
	if err != nil {
		t.Errorf("unexpected error")
	}
	gotMostFrequent := statistics.MostFrequent()
	assert.Equal(t, tc.wantMostFrequent, gotMostFrequent)
}

func Test_MostFrequent_UniqMarks(t *testing.T) {
	TestCase_mostFrequent{
		input: `Примеров Пример Примерович1	2
Примеров Пример Примерович2	1
Примеров Пример Примерович2	7
Примеров Пример Примерович1	6
Примеров Пример Примерович1	4
Примеров Пример Примерович1	3
Примеров Пример Примерович3	5`,
		wantMostFrequent: 7,
	}.Run(t)
}

func Test_MostFrequent_RepeatedMarks(t *testing.T) {
	TestCase_mostFrequent{
		input: `Примеров Пример Примерович1	9
Примеров Пример Примерович2	5
Примеров Пример Примерович1	5
Примеров Пример Примерович1	5
Примеров Пример Примерович3	1
Примеров Пример Примерович1	1
Примеров Пример Примерович2	2`,
		wantMostFrequent: 5,
	}.Run(t)
}

func Test_MostFrequent_RepeatedSameTimesMarks(t *testing.T) {
	TestCase_mostFrequent{
		input: `Примеров Пример Примерович1	1
Примеров Пример Примерович3	5
Примеров Пример Примерович2	1
Примеров Пример Примерович1	5
Примеров Пример Примерович1	3
Примеров Пример Примерович1	3`,
		wantMostFrequent: 5,
	}.Run(t)
}
