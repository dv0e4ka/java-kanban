package main;

import main.constructor.*;
import main.manager.Managers;
import main.manager.taskManager.FileBackedTasksManager;
import main.manager.taskManager.TaskManager;

import java.time.Duration;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTask();


        Epic epic1 = new Epic("Epic 1", "Description epic 1", TaskStatus.NEW);
        int epicId = manager.add(epic1);

        Epic epic2 = new Epic("Epic 2", "Description epic 2", TaskStatus.NEW);
        int epicId2 = manager.add(epic2);

        Epic epic3 = new Epic("Epic3", "Description epic3", TaskStatus.NEW);
        int epicId3 = manager.add(epic3);

        Epic epic4 = new Epic("Epic4", "Description epic4", TaskStatus.NEW);
        int epicId4 = manager.add(epic4);

        Epic epic5 = new Epic("Epic5", "Description epic5", TaskStatus.NEW);
        int epicId5 = manager.add(epic5);


        manager.getById(epicId);
        manager.getById(epicId2);
        manager.getById(epicId3);
        manager.getById(epicId4);
        manager.getById(epicId5);

        System.out.println(manager.getHistory());
    }
}