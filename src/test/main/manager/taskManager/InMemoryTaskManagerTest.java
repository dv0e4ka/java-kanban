package main.manager.taskManager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void createManager() {
        manager = new InMemoryTaskManager();
    }

//    @Override
//    protected InMemoryTaskManager createManager() {
//        return manager = new InMemoryTaskManager();
//    }
}