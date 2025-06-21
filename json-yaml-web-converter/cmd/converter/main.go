package main

import (
	"fmt"
	"os"
	"week7/pkg/converter"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Println("Использование: go run main.go [json-to-yaml|yaml-to-json] input_file output_file")
		return
	}

	fmt.Println("Hello")

	command := os.Args[1]
	inputFile := os.Args[2]
	outputFile := os.Args[3]

	fmt.Println(command + " " + inputFile + " " + outputFile)

	// Чтение входного файла
	inputData, err := os.ReadFile(inputFile)
	if err != nil {
		fmt.Printf("Ошибка при чтении файла: %v\n", err)
		return
	}

	var outputData []byte

	switch command {
	case "json-to-yaml":
		outputData, err = converter.ConvertJSONToYAML(inputData)
		if err != nil {
			fmt.Printf("Ошибка при конвертации JSON в YAML: %v\n", err)
			return
		}
	case "yaml-to-json":
		outputData, err = converter.ConvertYAMLToJSON(inputData)
		if err != nil {
			fmt.Printf("Ошибка при конвертации YAML в JSON: %v\n", err)
			return
		}
	default:
		fmt.Println("Неизвестная команда. Используйте 'json-to-yaml' или 'yaml-to-json'")
		return
	}

	// Запись результата в выходной файл
	if err := os.WriteFile(outputFile, outputData, 0644); err != nil {
		fmt.Printf("Ошибка при записи в файл: %v\n", err)
		return
	}

	fmt.Printf("Конвертация завершена успешно. Результат сохранен в %s\n", outputFile)
}
