package parser

import (
	"fmt"
	"log"
	"os"
	"path/filepath"
	proto_pkg "week10/pkg/proto"
)

type Stat struct {
	path        string
	isDir       bool
	lastChanged int64
}

func ParseFromPath(path string) (*proto_pkg.Directory, error) {
	rootStat, err := os.Stat(path)
	if err != nil {
		return nil, fmt.Errorf("error parsing root directory: %v", err)
	}
	if !rootStat.IsDir() {
		return nil, fmt.Errorf("root directory must be directory")
	}

	result, err := parseDirectory("", path)
	if err != nil {
		return nil, fmt.Errorf("error parsing directory: %v", err)
	}
	return result, nil
}

func parseFileStat(path string) (*Stat, error) {
	info, err := os.Stat(path)
	if err != nil {
		return nil, fmt.Errorf("error: %v", err)
	}
	var stat Stat
	stat.path = path
	stat.isDir = info.IsDir()
	stat.lastChanged = info.ModTime().Unix()

	return &stat, nil
}

func parseDirectory(path string, rootPath string) (*proto_pkg.Directory, error) {
	log.Println("Parsing directory", path)
	absPath := filepath.Join(rootPath, path)
	entries, err := os.ReadDir(absPath)
	if err != nil {
		return nil, fmt.Errorf("error getting entries: %v", err)
	}
	var dirs []*proto_pkg.Directory
	var files []*proto_pkg.File

	for _, entry := range entries {
		if entry.IsDir() {
			internalDir, err := parseDirectory(filepath.Join(path, entry.Name()), rootPath)
			if err != nil {
				return nil, fmt.Errorf("error: %v", err)
			}
			dirs = append(dirs, internalDir)
		} else { // Parse file
			file, err := parseFile(filepath.Join(path, entry.Name()), rootPath)
			if err != nil {
				return nil, fmt.Errorf("error: %v", err)
			}
			files = append(files, file)
		}
	}
	info, err := os.Stat(absPath)
	if err != nil {
		return nil, fmt.Errorf("error: %v", err)
	}
	directory := &proto_pkg.Directory{
		Path:        path,
		Files:       files,
		Directories: dirs,
		Timestamp:   info.ModTime().Unix(),
	}
	return directory, nil
}

func parseFile(path string, rootPath string) (*proto_pkg.File, error) {
	absPath := filepath.Join(rootPath, path)
	stat, err := parseFileStat(absPath)
	if err != nil {
		return nil, fmt.Errorf("error read file: %v", err)
	}
	if stat.isDir {
		return nil, fmt.Errorf("%s is a directory", path)
	}
	content, err := os.ReadFile(absPath)
	if err != nil {
		return nil, fmt.Errorf("error: %v", err)
	}
	log.Println(absPath)
	file := &proto_pkg.File{
		Path:      path,
		Content:   content,
		Timestamp: stat.lastChanged,
	}
	return file, nil
}
