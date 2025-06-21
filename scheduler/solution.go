package main

import (
	"context"
	"fmt"
	"os"
	"os/exec"
	"os/signal"
	"strings"
	"syscall"
	"time"
	"unicode"
)

type Task struct {
	Time    time.Time
	Command string
}

func IsEmoji(r rune) bool {
	// Список взят с сайта https://www.unicode.org/charts/?
	// Сделал через switch тк так визуально проше воспринимать
	switch {
	case r >= 0x1F600 && r <= 0x1F64F:
		return true
	case r >= 0x1F300 && r <= 0x1F5FF:
		return true
	case r >= 0x1F680 && r <= 0x1F6FF:
		return true
	case r >= 0x1F700 && r <= 0x1F77F:
		return true
	case r >= 0x1F900 && r <= 0x1F9FF:
		return true
	case r >= 0x2600 && r <= 0x26FF:
		return true
	case r >= 0x2700 && r <= 0x27BF:
		return true
	default:
		return false
	}
}

// Читает файл и возврашает срез из его строк
func FileToStrings(filepath string) ([]string, error) {
	content, err := os.ReadFile(filepath)
	if err != nil {
		return nil, fmt.Errorf("ошибка чтения файла: %w", err)
	}
	lines := strings.Split(string(content), "\n")
	return lines, nil
}

// Из списка строк отбирает те, в которых записанны комманды и возврашает их в виде среза
func TaskParser(lines []string) ([]Task, error) {
	// Такое разделение позволит в последствии сконкатенировать срезы в хронологическом порядке задач
	Tasks := make([]Task, 0)
	TomorrowTask := make([]Task, 0)

	for _, str := range lines {
		if len(str) == 0 { // Если строка пустая, мы её пропускаем
			continue
		}
		firstChar := []rune(str)[0]
		// Если первый символ - эмодзи, решетка или пробельный символ (в том числе перенос строки), то пропускаем строку
		if IsEmoji(firstChar) || firstChar == 35 || unicode.IsSpace(firstChar) {
			continue
		}

		// Преобразуем строку в Задачу
		parts := strings.SplitN(str, " ", 2)
		if len(parts) < 2 {
			return nil, fmt.Errorf("неверный формат строки: %s", str)
		}

		parsedTime, err := time.Parse("15:04:05", parts[0])
		if err != nil {
			return nil, fmt.Errorf("ошибка при парсинге времени: %w", err)
		}

		now := time.Now()
		taskTime := time.Date(now.Year(), now.Month(), now.Day(), parsedTime.Hour(),
			parsedTime.Minute(), parsedTime.Second(), 0, now.Location())

		if taskTime.Before(now) {
			taskTime = taskTime.AddDate(0, 0, 1)
			TomorrowTask = append(TomorrowTask, Task{Time: taskTime, Command: parts[1]})
		} else {
			Tasks = append(Tasks, Task{Time: taskTime, Command: parts[1]})
		}

	}
	fmt.Printf("Задач на сегодня: %d\n", len(Tasks))
	fmt.Printf("Задач на завтра: %d\n", len(TomorrowTask))
	allTasks := append(Tasks, TomorrowTask...)
	return allTasks, nil
}

func scheduler(ctx context.Context, tasks []Task) {
	fmt.Printf("Задач в расписании: %d\n", len(tasks))
	currentTime := time.Now()
	for i, task := range tasks {
		// Вычисляем время до начала задачи и при необходимости ждем
		timeToWait := task.Time.Sub(currentTime)
		if timeToWait >= 0 {
			select {
			case <-time.After(timeToWait):
			case <-ctx.Done():
				fmt.Println("Получен сигнал завершения")
				os.Exit(0)
			}
		}

		// Таймаут - 1 минута или начало следуюшей
		timeout := 1 * time.Minute
		if i+1 < len(tasks) {
			nextTaskTime := tasks[i+1].Time
			if nextTaskTime.Sub(task.Time) < timeout {
				timeout = nextTaskTime.Sub(task.Time)
			}
		}
		taskCtx, timer := context.WithTimeout(context.Background(), timeout)
		defer timer()

		patrts := strings.Split(task.Command, " ")
		cmd := exec.Command(patrts[0], patrts[1:]...)

		cmd.Start()

		// Создаем поток, ожидаюший завершения задачи
		var status error
		done := make(chan struct{})
		go func() {
			defer close(done)
			_, status = cmd.Process.Wait()
		}()

		select {
		case <-ctx.Done():
			fmt.Println("Получин сигнал завершения")
			if err := cmd.Process.Kill(); err != nil {
				fmt.Printf("Не удалось прервать задачу %d: %v\n", i, err)
			}
			os.Exit(0)
		case <-taskCtx.Done():
			fmt.Printf("Вышло время выполнения здачи %d вышло", i)
			cmd.Process.Kill()
			continue
		case <-done:
			if status != nil {
				fmt.Printf("Процесс завершился %d со статусом: %s\n", i, status)
			}
			fmt.Printf("Процесс под номером %d завершен корректно\n", i)
		}
	}
}

func main() {
	fmt.Printf("Время сейчас: %s\n", time.Now())
	if len(os.Args) < 2 {
		fmt.Println("Запускайте так: go run solution.go <config-file>")
		os.Exit(1)
	}

	filePath := os.Args[1]

	ctx, releaseSignalHandlers := signal.NotifyContext(
		context.Background(),
		syscall.SIGINT, syscall.SIGTERM,
	)
	defer releaseSignalHandlers()

	for {
		// Получаем задачи
		lines, err := FileToStrings(filePath)
		if err != nil {
			fmt.Printf("Ошибка при чтении файла: %s \n", err)
		}

		tasks, err := TaskParser(lines)
		if err != nil {
			fmt.Printf("Ошибка в парсинге файла конфигурации: %v\n", err)
			os.Exit(1)
		}
		if len(tasks) < 1 {
			fmt.Println("Нет задач")
			os.Exit(0)
		}

		// Запускаем задачи
		scheduler(ctx, tasks)
	}
}
