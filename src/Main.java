import constructor.*;
import manager.Managers;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTask();
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        manager.add(task1);

        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        manager.add(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1", TaskStatus.NEW);
        manager.add(epic1);

        Subtask Subtask1 = new Subtask("Subtask1", "Description Subtask1", TaskStatus.NEW, epic1.getId());
        manager.add(Subtask1);

        manager.getById(epic1.getId());
        manager.getById(task1.getId());

        Task task3 = new Task("Task3", "Description task3", TaskStatus.NEW);
        manager.add(task3);

        System.out.println(manager.getHistory());
    }
}