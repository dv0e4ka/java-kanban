package main.manager.history;

import main.constructor.Epic;
import main.constructor.Task;
import main.constructor.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager taskManager = new InMemoryHistoryManager();
    //Epic epic = new Epic("", "", TaskStatus.NEW);

    @Test
    void shouldStatusNoSubtask(Epic epic) {

    }
}