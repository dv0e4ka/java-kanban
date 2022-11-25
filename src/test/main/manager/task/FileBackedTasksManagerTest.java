package main.manager.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final Path path = Paths.get("resources/TaskManager.csv");
    @Override
    protected FileBackedTasksManager createManager() {
        manager = new FileBackedTasksManager();
        return manager;
    }

    @Test
    protected void saveAndLoadWithoutAnyTasks() {
        manager.add(createTask());
        manager.deleteAllTasks();
//        manager.loadFromFile(path);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(path);
        assertEquals(newManager.getAllTasks().size(), 0);
    }

    @AfterEach
     void setDown() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}