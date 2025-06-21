package main

import (
	"fmt"
	"log"
	rfpb "week10/pkg/readFromProtoBin"
)

func main() {
	parsed, err := rfpb.ReadBin("out.bin")
	if err != nil {
		log.Fatalf("Error parsing binary: %v", err)
	}
	fmt.Println(parsed)
}
