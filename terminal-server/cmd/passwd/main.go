package main

import (
	"fmt"
	"os"
	"week5TButrov/pkg/password"
)

func main() {
	if len(os.Args) != 2 {
		fmt.Println("Usage: passwd <filePath>")
		os.Exit(1)
	}

	filePath := os.Args[1]

	fmt.Print("Enter passwor for server: ")
	var secret string
	fmt.Scan(&secret)

	hashedPassword, err := password.HashPassword(secret)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error hashing password: %v\n", err)
		os.Exit(1)
	}

	err = os.WriteFile(filePath, []byte(hashedPassword), 0600)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error writing secret file: %v\n", err)
		os.Exit(1)
	}

	fmt.Println("Secret file created successfully")
}
