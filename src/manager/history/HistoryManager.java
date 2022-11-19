package manager.history;

import constructor.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void delete(int id);
    List<Task> getHistory();

}
