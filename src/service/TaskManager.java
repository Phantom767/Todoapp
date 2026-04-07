package service;

import model.Task;
import model.Status;
import java.util.*;
import java.io.*;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final String FILE_NAME = "tasks.txt";

    public void addTask(String title) {

        tasks.add(new Task(UUID.randomUUID(), title, Status.NEW));
    }

    public List<Task> getAllTasks() {

        return tasks;
    }

    public void changeStatus(UUID id, Status newStatus) {
        tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .ifPresent(t -> t.setStatus(newStatus));
    }

    public void deleteTask(UUID id) {

        tasks.removeIf(t -> t.getId().equals(id));
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.println(task.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                UUID id = UUID.fromString(parts[0]);
                tasks.add(new Task(id, parts[1], Status.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        }
    }
}
