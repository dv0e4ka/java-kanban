package main;

import main.constructor.*;
import main.manager.Managers;
import main.manager.task.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTask();
        Epic epic1 = new Epic("Epic1", "Description epic1", TaskStatus.NEW);
        int epicId = manager.add(epic1);

        Subtask Subtask1 = new Subtask("Subtask1", "Description Subtask1",
                TaskStatus.NEW, epic1.getId());
        Subtask1.setStartTime("01.01.21/00:00");
        Subtask1.setDuration(Duration.ofDays(900));
        manager.add(Subtask1);


        System.out.println(manager.getEpicById(epicId));
    }
}