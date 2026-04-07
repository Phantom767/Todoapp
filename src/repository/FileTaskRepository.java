package repository;

import model.Status;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileTaskRepository implements TaskRepository {
    private final String fileName;

    public FileTaskRepository(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Task> loadAll() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    try {
                        UUID id = UUID.fromString(parts[0]);
                        Status status = Status.valueOf(parts[2]);
                        tasks.add(new Task(id, parts[1], status));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Пропуск поврежденной строки: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при загрузке данных: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public void saveAll(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Task task : tasks) {
                writer.println(task.getId() + ";" + task.getTitle() + ";" + task.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }
}
