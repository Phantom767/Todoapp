package service;

import model.Status;
import model.Task;
import repository.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TaskManager {
    private final List<Task> tasks;
    private final TaskRepository repository;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
        this.tasks = repository.loadAll();
    }

    public boolean addTask(String title) {
        boolean exists = tasks.stream().anyMatch(t -> t.getTitle().equalsIgnoreCase(title));
        if (exists) {
            return false;
        }
        tasks.add(new Task(UUID.randomUUID(), title, Status.NEW));
        repository.saveAll(tasks);
        return true;
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
                    repository.saveAll(tasks);
                });
    }

    public void deleteTask(UUID id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) {
            repository.saveAll(tasks);
        }
    }
}