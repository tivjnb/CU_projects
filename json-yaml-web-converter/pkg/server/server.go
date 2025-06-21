package server

import (
	"context"
	"fmt"
	"io"
	"log"
	"net/http"
	"net/url"
	"strings"
	"time"
	"week7/pkg/converter"
)

func parseBody(r io.ReadCloser) ([]byte, error) {
	bodyBytes, err := io.ReadAll(r)
	if err != nil {
		return nil, fmt.Errorf("error reading body")
	}
	defer r.Close()

	// Проверяем, пустое ли тело запроса
	if len(bodyBytes) == 0 {
		return nil, fmt.Errorf("empty body")
	}
	return bodyBytes, nil
}

func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		log.Printf("=> %s %s\n", r.Method, r.URL.Path) // логируем метод и путь
		next.ServeHTTP(w, r)                           // вызываем следующий обработчик
		log.Printf("<= отправлен ответ клиенту")
	})
}

// Middleware на проверку типа запроса
func postMethodChecker(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
			return
		}
		next.ServeHTTP(w, r)
	})
}

func yamlToJson(w http.ResponseWriter, r *http.Request) {

	bodyBytes, err := parseBody(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	convertedBytes, err := converter.ConvertYAMLToJSON(bodyBytes)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	fmt.Fprint(w, string(convertedBytes))
}

func jsonToYaml(w http.ResponseWriter, r *http.Request) {

	bodyBytes, err := parseBody(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	convertedBytes, err := converter.ConvertJSONToYAML(bodyBytes)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	fmt.Fprintln(w, string(convertedBytes))
}

func baseUrl(w http.ResponseWriter, r *http.Request) {
	// Проверка неправильных адресов
	if r.URL.Path != "/" {
		http.Error(w, "Not found", http.StatusNotFound)
		return
	}

	if r.Method != http.MethodGet {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	http.ServeFile(w, r, "static/index.html")
}

func convertHandler(w http.ResponseWriter, r *http.Request) {
	// Извлекаем часть пути после "/convert/"
	id := r.URL.Path[len("/convert/"):]

	// Удаляем лишнюю часть формы и переводим из htmlform формата
	bodyBytes, err := parseBody(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	decodedBody, err := url.QueryUnescape(string(bodyBytes))
	if err != nil {
		http.Error(w, "Failed to decode URL-encoded data", http.StatusBadRequest)
		return
	}

	parts := strings.SplitN(decodedBody, "=", 2)
	if len(parts) != 2 {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}
	bodyBytes = []byte(parts[1])
	log.Println(string(bodyBytes))

	// Конвертируем в нужный формат
	var convertedBytes []byte
	switch id {
	case "toJson":
		convertedBytes, err = converter.ConvertYAMLToJSON(bodyBytes)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
	case "toYaml":
		convertedBytes, err = converter.ConvertJSONToYAML(bodyBytes)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
	default:
		http.Error(w, "must be convert/{toJson} or {toYaml}", http.StatusBadRequest)
		return
	}
	fmt.Fprintln(w, string(convertedBytes))
}

func RunServer(ctx context.Context, address string) {
	mux := http.NewServeMux()

	// REST
	mux.Handle("/api/v1/yaml-to-json", postMethodChecker(http.HandlerFunc(yamlToJson)))
	mux.Handle("/api/v1/json-to-yaml", postMethodChecker(http.HandlerFunc(jsonToYaml)))

	// Страничка с формой
	mux.HandleFunc("/", baseUrl)
	mux.Handle("/convert/", postMethodChecker(http.HandlerFunc(convertHandler)))

	// Навешиваем логирование
	loggedMux := loggingMiddleware(mux)

	// Создаем и запускаем сервер
	server := &http.Server{
		Addr:    address,
		Handler: loggedMux,
	}
	go func() {
		log.Println(address)
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Ждем окончания контекста и безопасно останавливаем сервер
	<-ctx.Done()

	// У сервера 5 секунд на остановку
	shutdownCtx, shutdownCancel := context.WithTimeout(ctx, 5*time.Second)
	defer shutdownCancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		log.Printf("Server shutdown failed: %v", err)
	} else {
		log.Println("Server gracefully stopped")
	}
}
