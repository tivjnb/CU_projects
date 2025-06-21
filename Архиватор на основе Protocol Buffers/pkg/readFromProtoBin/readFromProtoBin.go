package readFromProtoBin

import (
	"fmt"
	"log"
	"os"

	"google.golang.org/protobuf/proto"
	protoPkg "week10/pkg/proto"
)

func ReadBin(filePath string) (string, error) {
	// Читаем содержимое бинарного файла
	data, err := os.ReadFile(filePath)
	if err != nil {
		log.Printf("Error reading binary file: %v", err)
		return "", fmt.Errorf("error reading binary file: %v", err)
	}

	// Десериализуем данные в структуру Directory
	var directory protoPkg.Directory
	if err := proto.Unmarshal(data, &directory); err != nil {
		log.Printf("Error unmarshalling protobuf data: %v", err)
		return "", fmt.Errorf("error unmarshalling protobuf data: %v", err)
	}
	ParseDirStr(&directory)
	return "", err
}

func ParseDirStr(dir *protoPkg.Directory) {
	fmt.Println("dir path", dir.Path)
	for _, d := range dir.Directories {
		ParseDirStr(d)
	}
	for _, f := range dir.Files {
		ParseFileStr(f)
	}
}

func ParseFileStr(file *protoPkg.File) {
	fmt.Println("Filename: ", file.Path)
	fmt.Println("Content size: ", len(file.Content))
	fmt.Println("Timestamp: ", file.Timestamp)
	fmt.Println("")
}
