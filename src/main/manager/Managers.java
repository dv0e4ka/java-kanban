package main.manager;

import main.manager.history.*;
import main.manager.task.*;

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

