package main.manager;

import main.manager.historyManager.*;
import main.manager.taskManager.*;

public class Managers {
    public Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }
}

