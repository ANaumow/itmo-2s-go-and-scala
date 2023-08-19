package mark

import (
	"bufio"
	"errors"
	"fmt"
	"io"
	"regexp"
	"strconv"
	"strings"
)

type Student struct {
	Name string
	Mark int
}

func NewStudent(line string) *Student {
	pattern := regexp.MustCompile(`^(.*)\t([1-9]|10)$`)

	matches := pattern.MatchString(line)

	if matches {
		split := strings.Split(line, "\t")
		var name, rawMark = split[0], split[1]

		mark, err := strconv.Atoi(rawMark)
		if err != nil {
			return nil
		}

		return &Student{
			Name: name,
			Mark: mark,
		}
	}

	return nil
}

type StudentsStatistic interface {
	SummaryByStudent(student string) (int, bool)     // default_value, false - если студента нет
	AverageByStudent(student string) (float32, bool) // default_value, false - если студента нет
	Students() []string
	Summary() int
	Median() int
	MostFrequent() int
}

func ReadStudentsStatistic(reader io.Reader) (StudentsStatistic, error) {
	scanner := bufio.NewScanner(reader)
	var students []Student

	for scanner.Scan() {
		line := scanner.Text()
		student := NewStudent(line)
		if student != nil {
			students = append(students, *student)
		}
	}

	if err := scanner.Err(); err != nil {
		return nil, err
	}

	if len(students) == 0 {
		return nil, errors.New("no students")
	}

	return NewStudentStatistic(students), nil
}

func WriteStudentsStatistic(writer io.Writer, statistic StudentsStatistic) error {
	summary := statistic.Summary()
	median := strconv.Itoa(statistic.Median())
	mostFrequent := strconv.Itoa(statistic.MostFrequent())
	output := strconv.Itoa(summary) + "\t" + median + "\t" + mostFrequent + "\n"

	for _, studentName := range statistic.Students() {
		s, _ := statistic.SummaryByStudent(studentName)
		a, _ := statistic.AverageByStudent(studentName)
		output += studentName + "\t" + strconv.Itoa(s) + "\t" + fmt.Sprintf("%.2f", a) + "\n"
	}
	_, err := writer.Write([]byte(output))
	if err != nil {
		return err
	}
	return nil
}
