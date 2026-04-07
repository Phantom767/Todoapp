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
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    manager.getAllTasks().forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Название: ");
                    manager.addTask(scanner.nextLine());
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
                    int s = Integer.parseInt(scanner.nextLine());
                    Status status = (s == 2) ? Status.IN_PROGRESS : (s == 3) ? Status.DONE : Status.NEW;
                    manager.changeStatus(id, status);
                    break;
                case 4:
                    System.out.print("ID для удаления: ");
                    manager.deleteTask(UUID.fromString(scanner.nextLine()));
                    break;
                case 0:
                    manager.saveToFile();
                    System.exit(0);
            }
        }
    }
}