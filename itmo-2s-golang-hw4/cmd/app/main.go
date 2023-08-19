package main

import (
	m "hw3/pkg/mark"
	"log"
	"os"
)

func main() {
	file, err := os.Open("./data/input_1.tsv")
	if err != nil {
		log.Fatal("error with reading input file: ", err)
	}
	studentsStatistic, err := m.ReadStudentsStatistic(file)
	if err != nil {
		log.Fatal("error with reading input file: ", err)
	}
	studentsStatistic.Students()

	file, err = os.OpenFile("./data/output_1_got.tsv", os.O_WRONLY|os.O_CREATE, 0644)

	err = m.WriteStudentsStatistic(file, studentsStatistic)
	if err != nil {
		log.Fatal("error with writing output file: ", err)
	}

	file.Close()
}
