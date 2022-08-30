package manager.history;


import constructor.Task;
import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    List<Task> getHistory();

}
