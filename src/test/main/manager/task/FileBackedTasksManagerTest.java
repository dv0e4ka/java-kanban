package main.manager.task;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    protected FileBackedTasksManager createManager() {
        return manager = new FileBackedTasksManager();
    }

}