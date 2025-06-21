package converter

import (
	"encoding/json"
	"fmt"
	"gopkg.in/yaml.v3"
)

func ConvertJSONToYAML(jsonData []byte) ([]byte, error) {
	var data interface{}
	if err := json.Unmarshal(jsonData, &data); err != nil {
		return nil, fmt.Errorf("ошибка при разборе JSON: %w", err)
	}
	yamlData, err := yaml.Marshal(data)
	if err != nil {
		return nil, fmt.Errorf("ошибка при конвертации в YAML: %w", err)
	}
	return yamlData, nil
}

func ConvertYAMLToJSON(yamlData []byte) ([]byte, error) {
	var data interface{}
	if err := yaml.Unmarshal(yamlData, &data); err != nil {
		return nil, fmt.Errorf("ошибка при разборе YAML: %w", err)
	}
	jsonData, err := json.MarshalIndent(data, "", "  ")
	if err != nil {
		return nil, fmt.Errorf("ошибка при конвертации в JSON: %w", err)
	}
	return jsonData, nil
}
