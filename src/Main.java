import model.Status;
import service.TaskManager;

import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.loadFromFile();
        Scanner scanner = new Scanner(System.in);

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
                    System.out.print("Название: ");
                    String title = scanner.nextLine().trim();
                    if (title.isEmpty()) {
                        System.out.println("Название не может быть пустым!");
                        break;
                    }
                    if (title.contains(";")) {
                        System.out.println("Название не должно содержать символ ';', так как он используется для сохранения.");
                        break;
                    }
                    if (manager.isTaskExists(title)) {
                        System.out.println("Ошибка: Задача с таким названием уже существует!");
                    } else {
                        manager.addTask(title);
                        System.out.println("Задача добавлена.");
                    }
                    break;
                case 3:
                    System.out.print("ID задачи: ");
                    UUID id;
                    try {
                        id = UUID.fromString(scanner.nextLine());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный формат ID. Попробуйте снова.");
                        continue;
                    }
                    System.out.println("1. NEW | 2. IN_PROGRESS | 3. DONE");
                    int statusChoice;
                    try {
                        statusChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: нужно ввести цифру (1-3).");
                        continue;
                    }
                    Status status = (statusChoice == 2) ? Status.IN_PROGRESS : (statusChoice == 3) ? Status.DONE : Status.NEW;
                    manager.changeStatus(id, status);
                    break;
                case 4:
                    System.out.print("ID для удаления: ");
                    manager.deleteTask(UUID.fromString(scanner.nextLine()));
                    break;
                case 0:
                    manager.saveToFile();
                    System.exit(0);
                default:
                    System.out.println("Неверная команда, выберите от 0 до 4.");
                    break;
            }
        }
    }
}