package server

import (
	"bytes"
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"testing"
	"time"
)

func TestServer(t *testing.T) {
	err := os.Chdir("../..") // Или путь к корневой директории проекта для корректной работы со static
	if err != nil {
		t.Fatalf("Failed to change working directory: %v", err)
	}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	address := ":8080"
	go RunServer(ctx, address)

	// Дадим время на включение
	time.Sleep(10 * time.Millisecond)

	client := &http.Client{}

	t.Run("Test GET /", func(t *testing.T) {
		fmt.Println("http://localhost" + address + "/")
		req, err := http.NewRequest("GET", "http://localhost:8080/", nil)
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()
		log.Println(resp.StatusCode, resp.Status)
		if resp.StatusCode != http.StatusOK {
			t.Errorf("Expected status 200 OK, got %d", resp.StatusCode)
		}
	})

	t.Run("Test POST /api/v1/yaml-to-json with valid YAML", func(t *testing.T) {
		payload := []byte(`key: value`)
		req, err := http.NewRequest("POST", "http://localhost"+address+"/api/v1/yaml-to-json", bytes.NewBuffer(payload))
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusOK {
			t.Errorf("Expected status 200 OK, got %d", resp.StatusCode)
		}
	})

	t.Run("Test POST /api/v1/yaml-to-json with empty body", func(t *testing.T) {
		req, err := http.NewRequest("POST", "http://localhost"+address+"/api/v1/yaml-to-json", nil)
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusBadRequest {
			t.Errorf("Expected status 400 Bad Request, got %d", resp.StatusCode)
		}
	})

	t.Run("Test POST /api/v1/json-to-yaml with valid JSON", func(t *testing.T) {
		payload := []byte(`{"key": "value"}`)
		req, err := http.NewRequest("POST", "http://localhost"+address+"/api/v1/json-to-yaml", bytes.NewBuffer(payload))
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusOK {
			t.Errorf("Expected status 200 OK, got %d", resp.StatusCode)
		}
	})

	t.Run("Test POST /api/v1/json-to-yaml with invalid JSON", func(t *testing.T) {
		payload := []byte(`invalid json`)
		req, err := http.NewRequest("POST", "http://localhost"+address+"/api/v1/json-to-yaml", bytes.NewBuffer(payload))
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusBadRequest {
			t.Errorf("Expected status 400 Bad Request, got %d", resp.StatusCode)
		}
	})

	t.Run("Test Invalid Method for /api/v1/yaml-to-json", func(t *testing.T) {
		req, err := http.NewRequest("GET", "http://localhost"+address+"/api/v1/yaml-to-json", nil)
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusMethodNotAllowed {
			t.Errorf("Expected status 405 Method Not Allowed, got %d", resp.StatusCode)
		}
	})

	t.Run("Test Non-existent Route", func(t *testing.T) {
		req, err := http.NewRequest("GET", "http://localhost"+address+"/nonexistent", nil)
		if err != nil {
			t.Fatalf("Failed to create request: %v", err)
		}

		resp, err := client.Do(req)
		if err != nil {
			t.Fatalf("Request failed: %v", err)
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusNotFound {
			t.Errorf("Expected status 404 Not Found, got %d", resp.StatusCode)
		}
	})
}
