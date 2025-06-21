package ru.culab.week10;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ProtoUnpackable {
    /**
     * Открывает архивный файл для чтения
     * @param archivePath путь к архивному файлу
     * @throws IOException если файл не может быть открыт или содержит не валидные данные
     */
    void open(String archivePath) throws IOException;

    /**
     * Возвращает список всех файлов в архиве
     * @return список путей к файлам
     */
    List<String> listFiles();

    /**
     * Читает содержимое файла из архива
     * @param filePath путь к файлу в архиве
     * @return содержимое файла
     * @throws IOException если файл не найден или не может быть прочитан
     */
    byte[] readFile(String filePath) throws IOException;

    /**
     * Возвращает время последней модификации файла
     * @param filePath путь к файлу в архиве
     * @return timestamp последней модификации
     * @throws FileNotFoundException если файл не найден
     */
    long getLastModified(String filePath) throws FileNotFoundException;
}
