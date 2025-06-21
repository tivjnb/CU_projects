package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"week5TButrov/pkg/server"
)

func main() {
	// Парсим аргументы
	if len(os.Args) != 3 {
		fmt.Println("Usage: server <secret-file> <address>")
		os.Exit(1)
	}

	filePath := os.Args[1]
	address := os.Args[2]

	// Считываем пароль из файла
	hashedPassword, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Printf("Ошибка чтения из файла: %v\n", err)
		os.Exit(1)
	}

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	go func() {
		err := server.RunServer(ctx, address, hashedPassword)
		if err != nil {
			fmt.Printf("Ошибка запуска сервера: %v\n", err)
			os.Exit(1)
		}
	}()

	<-ctx.Done() // Ожидание завершения по сигналу
	fmt.Println("Завершение работы сервера...")
}
