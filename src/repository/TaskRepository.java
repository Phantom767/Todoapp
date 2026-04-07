package repository;

import model.Task;
import java.util.List;

public interface TaskRepository {
    List<Task> loadAll();
    void saveAll(List<Task> tasks);
}