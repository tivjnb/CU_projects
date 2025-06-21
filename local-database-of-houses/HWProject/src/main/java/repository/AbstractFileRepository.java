package repository;

import model.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileRepository<T extends Entity> implements Repository<T> {
    private final File file;

    public AbstractFileRepository(String filePath) throws Exception {
        this.file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Failed to create directory: " + parent.getAbsolutePath());
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file: " + file.getAbsolutePath());
            }
        }
    }

    protected abstract T deserialize(String content) throws Exception;
    protected abstract String serialize(T object);

    protected List<String> readAllLines() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading file: " + file.getName(), e);
        }
        return lines;
    }

    protected void writeAllLines(List<String> lines) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new Exception("Error writing to file: " + file.getName(), e);
        }
    }

    @Override
    public List<T> getAll() throws Exception {
        List<T> entities = new ArrayList<>();
        List<String> lines = readAllLines();
        for (String line : lines) {
            entities.add(deserialize(line));
        }
        return entities;
    }

    @Override
    public void add(T entity) throws Exception {
        List<T> entities = getAll();
        entities.add(entity);
        saveAll(entities);
    }

    @Override
    public void update(int position, T entity) throws Exception {
        List<T> entities = getAll();
        if (position < 0 || position >= entities.size()) {
            throw new Exception("Invalid position for update");
        }
        entities.set(position, entity);
        saveAll(entities);
    }

    @Override
    public void delete(int position) throws Exception {
        List<T> entities = getAll();
        if (position < 0 || position >= entities.size()) {
            throw new Exception("Invalid position for deletion");
        }
        entities.remove(position);
        saveAll(entities);
    }

    private void saveAll(List<T> entities) throws Exception {
        List<String> lines = new ArrayList<>();
        for (T entity : entities) {
            lines.add(serialize(entity));
        }
        writeAllLines(lines);
    }
}