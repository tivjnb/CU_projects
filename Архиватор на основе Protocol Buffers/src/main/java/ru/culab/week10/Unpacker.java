package ru.culab.week10;

import main.proto.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unpacker implements ProtoUnpackable {
    private final Map<String, File> fileMap = new HashMap<>();
    private final Map<String, Directory> dirmap = new HashMap<>();
    //  Я решил что под специфику интерфейса и с учетом специфики proto файла проще сразу распарсить файлы

    @Override
    public void open(String archivePath) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(archivePath);
            Directory root = Directory.parseFrom(fis);
            processDirectory(root);
        } catch (Exception e) {
            throw new IOException("Failed to parse proto file", e);
        }
    }

    private void processDirectory(Directory directory) {
        for (File file : directory.getFilesList()) {
            fileMap.put(file.getPath(), file);
        }

        for (Directory subDir : directory.getDirectoriesList()) {
            dirmap.put(subDir.getPath(), subDir);
            processDirectory(subDir);
        }
    }

    @Override
    public List<String> listFiles() {
        ArrayList<String> filesPathes = new ArrayList<>(fileMap.keySet());
        filesPathes.addAll(dirmap.keySet());
        return filesPathes;
    }

    @Override
    public byte[] readFile(String filePath) throws IOException {
        File file = fileMap.get(filePath);
        if (file == null) {
            throw new IOException("File not found: " + filePath);
        }
        return file.getContent().toByteArray();
    }

    @Override
    public long getLastModified(String filePath) throws FileNotFoundException {
        if (fileMap.containsKey(filePath)) {
            File file = fileMap.get(filePath);
            try {
                return file.getTimestamp();
            } catch (NumberFormatException e) {
                throw new RuntimeException("bad timestamp");
            }
        } else if (dirmap.containsKey(filePath)) {
            Directory dir = dirmap.get(filePath);
            try {
                return dir.getTimestamp();
            } catch (NumberFormatException e) {
                throw new RuntimeException("bad timestamp");
            }
        } else {
            throw new FileNotFoundException("file or dir not found");
        }

    }
}
