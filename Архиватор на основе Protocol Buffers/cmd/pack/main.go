package main

import (
	"fmt"
	"google.golang.org/protobuf/proto"
	"log"
	"os"
	"week10/pkg/parser"
)

func main() {
	args := os.Args
	if len(args) != 3 {
		log.Fatal("Usage: go run main.go dir_to_scan output_file")
	}
	dir_path := args[1]
	output_file := args[2]

	log.Printf("dir_path: %s", dir_path)
	dirInfo, err := parser.ParseFromPath(dir_path)
	if err != nil {
		log.Fatal(err)
	}

	// Сериализуем в бинарный формат
	data, err := proto.Marshal(dirInfo)
	if err != nil {
		log.Fatal(err)
	}
	_ = os.WriteFile(output_file, data, 0644)
	fmt.Printf("Выборка записана, размер %d байт\n", len(data))

}
