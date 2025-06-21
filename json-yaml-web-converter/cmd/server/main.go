package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"week7/pkg/server"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Использование: go run main.go address:port")
		return
	}

	address := os.Args[1]

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	server.RunServer(ctx, address)
}
