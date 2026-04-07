import model.Status;
import service.TaskManager;

import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {

    private final TaskManager manager;
    private final Scanner scanner;

    private static final Map<Integer, Status> STATUS_MAP = Map.of(
            1, Status.NEW,
            2, Status.IN_PROGRESS,
            3, Status.DONE
    );

    public ConsoleUI(TaskManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    public void run() {

        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ ЗАДАЧАМИ ---");
            System.out.println("1. Список задач | 2. Добавить | 3. Статус | 4. Удалить | 0. Выход");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите цифру (0-4).");
                continue;
            }

            switch (choice) {
                case 1:
                    manager.getAllTasks().forEach(System.out::println);
                    break;
                case 2:
                    handleAddTask();
                    break;
                case 3:
                    handleChangeStatus();
                    break;
                case 4:
                    handleDeleteTask();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверная команда, выберите от 0 до 4.");
            }
        }
    }

    private void handleAddTask() {
        System.out.print("Название: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            return;
        }
        if (title.contains(";")) {
            System.out.println("Название не должно содержать символ ';'.");
            return;
        }
        boolean added = manager.addTask(title);
        if (added) {
            System.out.println("Задача добавлена.");
        } else {
            System.out.println("Задача с таким названием уже существует!");
        }
    }

    private void handleChangeStatus() {
        System.out.print("ID задачи: ");
        UUID taskId;
        try {
            taskId = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат ID. Попробуйте снова.");
            return;
        }

        System.out.println("1. NEW | 2. IN_PROGRESS | 3. DONE");
        int statusChoice;
        try {
            statusChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: нужно ввести цифру (1-3).");
            return;
        }

        Status status = STATUS_MAP.getOrDefault(statusChoice, Status.NEW);
        manager.changeStatus(taskId, status);
        System.out.println("Статус обновлён.");
    }

    private void handleDeleteTask() {
        System.out.print("ID для удаления: ");
        UUID taskId;
        try {
            taskId = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат ID. Попробуйте снова.");
            return;
        }
        manager.deleteTask(taskId);
        System.out.println("Задача удалена (если существовала).");
    }
}