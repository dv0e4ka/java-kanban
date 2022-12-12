package main.manager.historyManager;

import main.constructor.Epic;
import main.constructor.Task;
import main.constructor.TaskStatus;
import main.manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryHistoryManager();
    }

    private Task setTime(Task task) {
        task.setStartTime("01.01.23/00:00");
        task.setDuration(Duration.ofMinutes(1));
        return task;
    }

    @Test
    public void shouldGetHistoryWhenEmpty() {
        Task task = setTime(new Task("task", "description", TaskStatus.NEW));
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    public void shouldGetHistoryWhenDuplicated() {
        Task task = setTime(new Task("task1", "description1", TaskStatus.NEW));
        task.setId(1);
        Task task2 = setTime(new Task("task2", "description2", TaskStatus.NEW));
        task2.setId(2);
        Task task3 = setTime(new Task("task3", "description3", TaskStatus.NEW));
        task3.setId(3);

        Task task4 = setTime(new Task("task4", "description4", TaskStatus.NEW));
        task4.setId(4);

        manager.add(task);
        manager.add(task);
        manager.add(task2);
        manager.add(task);
        assertEquals(2, manager.getHistory().size(), "ошибка менеджера истории при дубликации задач");

        manager.add(task);
        manager.add(task2);
        manager.add(task);
        manager.add(task3);
        manager.add(task4);
        manager.add(task);
        assertEquals(4, manager.getHistory().size(), "ошибка менеджера истории при дубликации задач");
    }

    @Test
    public void shouldDeleteFromStart() {
        Task task = setTime(new Task("task1", "description1", TaskStatus.NEW));
        task.setId(1);
        Task task2 = setTime(new Task("task2", "description2", TaskStatus.NEW));
        task2.setId(2);
        Task task3 = setTime(new Task("task3", "description3", TaskStatus.NEW));
        task3.setId(3);

        manager.add(task);
        manager.add(task2);
        manager.add(task3);

        manager.delete(task.getId());
        assertEquals(List.of(task2, task3), manager.getHistory());
    }

    @Test
    public void shouldDeleteFromEnd() {
        Task task = setTime(new Task("task1", "description1", TaskStatus.NEW));
        task.setId(1);
        Task task2 = setTime(new Task("task2", "description2", TaskStatus.NEW));
        task2.setId(2);
        Task task3 = setTime(new Task("task3", "description3", TaskStatus.NEW));
        task3.setId(3);

        manager.add(task);
        manager.add(task2);
        manager.add(task3);

        manager.delete(task3.getId());
        assertEquals(List.of(task, task2), manager.getHistory());
    }

    @Test
    public void shouldDeleteFromMid() {
        Task task = setTime(new Task("task1", "description1", TaskStatus.NEW));
        task.setId(1);
        Task task2 = setTime(new Task("task2", "description2", TaskStatus.NEW));
        task2.setId(2);
        Task task3 = setTime(new Task("task3", "description3", TaskStatus.NEW));
        task3.setId(3);

        manager.add(task);
        manager.add(task2);
        manager.add(task3);

        manager.delete(task2.getId());
        assertEquals(List.of(task, task3), manager.getHistory());
    }
}