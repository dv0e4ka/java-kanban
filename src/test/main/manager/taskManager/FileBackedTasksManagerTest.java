package main.manager.taskManager;


import main.constructor.Epic;
import main.constructor.Subtask;
import main.constructor.Task;
import main.constructor.TaskStatus;
import main.exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private static final String PATH = "resources/TaskManager.csv";

    @BeforeEach
    void createManager() {
        manager = new FileBackedTasksManager();
    }

    @Test
    protected void saveAndLoadWithHistory()  {
        Task task = new Task("task", "description", TaskStatus.NEW);
        task.setStartTime("01.01.22/00:00");
        task.setDuration(Duration.ofMinutes(1));
        int taskId = manager.add(task);

        Epic epic = new Epic("task", "description", TaskStatus.NEW);
        int epicId = manager.add(epic);

        Subtask subtask = new Subtask("task", "description", TaskStatus.NEW, epicId);
        getTimeSettingsForSubtask(subtask);
        int subtaskId = manager.add(subtask);

        manager.getById(taskId);

        FileBackedTasksManager newManager = null;
        try {
            newManager = new FileBackedTasksManager().loadFromFile();

        } catch (ManagerSaveException | IOException e) {}
        assertEquals(3, newManager.getAllTasks.size(), "менеджер некорректно " +
        "восстанавливает файл с задачами");

        System.out.println(manager.getAllTasks);

        assertEquals(task, newManager.getTasks.get(taskId), "менеджер некорректно " +
        "восстанавливает task из файла");
        assertEquals(subtask, newManager.getSubtasks.get(subtaskId), "менеджер некорректно " +
        "восстанавливает subtask из файла");
        assertEquals(epic, newManager.getEpics.get(epicId), "менеджер некорректно " +
        "восстанавливает epic из файла");
        assertEquals(manager.getHistory(), newManager.getHistory());
    }


    @AfterEach
    void setDown() throws IOException {
        manager = null;

        Files.deleteIfExists(Paths.get(PATH));
    }

    @Test
    protected void saveAndLoadWithoutAnyTasks()  {
        manager.add(createTask());
        manager.deleteAllTasks();
        FileBackedTasksManager newManager = null;
        try {
            newManager = new FileBackedTasksManager().loadFromFile();

        } catch (ManagerSaveException | IOException e) {}
        assertEquals(0, newManager.getAllTasks.size(), "менеджер не корректно " +
                "восстанавливает файл с пустыми задачами");
    }
}