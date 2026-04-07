package service;

import model.Task;
import model.Status;
import java.util.*;
import java.io.*;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.txt";

    public void addTask(String title) {
        tasks.add(new Task(UUID.randomUUID(), title, Status.NEW));
        saveToFile();
    }

    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void changeStatus(UUID id, Status newStatus) {
        tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .ifPresent(t -> {
                    t.setStatus(newStatus);
                    saveToFile();
                });
    }

    public void deleteTask(UUID id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) {
            saveToFile();
        }
    }

    public boolean isTaskExists(String title) {
        return tasks.stream()
                .anyMatch(task -> task.getTitle().equalsIgnoreCase(title));
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.println(task.getId() + ";" + task.getTitle() + ";" + task.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        tasks.clear(); // Избегаем дубликатов при повторной загрузке
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    UUID id = UUID.fromString(parts[0]);
                    tasks.add(new Task(id, parts[1], Status.valueOf(parts[2])));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        }
    }
}