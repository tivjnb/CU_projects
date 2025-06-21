package server

// Serevr fuction

import (
	"bufio"
	"context"
	"fmt"
	"net"
	"os/exec"
	"strings"
	"time"
	"week5TButrov/pkg/password"
)

func server(ctx context.Context, conn net.Conn, hashedPassword []byte) {
	defer conn.Close()
	reader := bufio.NewReader(conn)
	writer := bufio.NewWriter(conn)

	writer.Write([]byte("Welcome to the Server! Password: "))
	writer.Flush()
	conn.SetDeadline(time.Now().Add(15 * time.Second)) // Ставим дедлайн на ввод пароля

	userPassword, err := reader.ReadString('\n')
	if err != nil {
		writer.Write([]byte("Error reading password\n"))
		writer.Flush()
		return
	}
	userPassword = strings.TrimSpace(userPassword)

	err = password.CheckPassword(userPassword, hashedPassword)
	if err != nil {
		writer.Write([]byte("Invalid password\n"))
		writer.Flush()
		return
	}

	conn.SetDeadline(time.Time{})
	writer.Write([]byte("Authentication successful\r\n"))
	writer.Flush()

	for {
		_, _ = conn.Write([]byte("> "))
		command, err := reader.ReadString('\n')
		if err != nil {
			writer.Write([]byte("Error reading command\n"))
			writer.Flush()
			return
		}

		command = strings.TrimSpace(command)
		if command == "" || strings.HasPrefix(command, "#") {
			continue
		}

		if command == "exit" {
			writer.Write([]byte("Goodbye"))
			writer.Flush()
			return
		}

		// Создаем и запускаем команду
		patrts := strings.Split(command, " ")
		cmd := exec.Command(patrts[0], patrts[1:]...)

		var out []byte
		var commandErr error
		done := make(chan struct{})
		go func() {
			defer close(done)
			out, commandErr = cmd.CombinedOutput()
		}()

		// Ждем завершения команды или контекста
		select {
		case <-ctx.Done():
			writer.Write([]byte("Server is shutting down... Goodbye\n"))
			writer.Flush()
			return
		case <-done:
			if commandErr != nil {
				writer.Write([]byte(fmt.Sprintf("Error: %v\r\n", commandErr)))
				writer.Flush()
			} else {
				writer.Write(out)
				writer.Flush()
			}
		}
	}
}

func newConnection(ctx context.Context, conn net.Conn, hashedPassword []byte) error {

	serverCtx := context.Background()

	go server(serverCtx, conn, hashedPassword)

	select {
	case <-ctx.Done():
		fmt.Println("Сервер выключен.")
		return nil
	case <-serverCtx.Done():
		fmt.Println("Соединение остановленно.")
		return nil
	}
}

func RunServer(ctx context.Context, address string, hashedPassword []byte) error {
	listener, err := net.Listen("tcp", address)
	if err != nil {
		panic(err)
	}
	defer listener.Close()
	go func() {
		for {
			conn, err := listener.Accept()
			if err != nil {
				fmt.Println("Ошибка принятия подключения:", err)
				continue
			}

			fmt.Println("Новое подключение:", conn.RemoteAddr())
			go newConnection(ctx, conn, hashedPassword)
		}
	}()
	<-ctx.Done()
	return nil
}
