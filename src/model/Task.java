package model;

import java.util.UUID;
public class Task {
    private final UUID id;
    private final String title;
    private Status status;

    public Task(UUID id, String title, Status status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    // Геттеры и сеттеры
    public UUID getId() {
        return id; }
    public String getTitle() {
        return title; }
    public Status getStatus() {
        return status; }
    public void setStatus(Status status) {
        this.status = status; }

    @Override
    public String toString() {

        return String.format("[%s] %-20s | Статус: %s", id, title, status);
    }

    // Для сохранения в файл
    public String toFileString() {

        return id + ";" + title + ";" + status;
    }
}

