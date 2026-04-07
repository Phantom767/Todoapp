import repository.FileTaskRepository;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        FileTaskRepository repository = new FileTaskRepository("tasks.txt");
        TaskManager manager = new TaskManager(repository);
        new ConsoleUI(manager).run();
    }
}