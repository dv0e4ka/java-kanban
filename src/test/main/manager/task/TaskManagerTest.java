package main.manager.task;

import main.constructor.*;
import main.manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    protected abstract T createManager();

    @BeforeEach
    void setUp() {
        manager = createManager();
    }

    protected Subtask getTimeSettingsForSubtask(Subtask subtask) {
        subtask.setStartTime("01.01.22,00:00");
        subtask.setDuration(Duration.ofMinutes(1));
        return subtask;
    }

    protected Task createTask() {
        Task task = new Task("Task1", "Description task1", TaskStatus.NEW);
        task.setStartTime("01.01.22,00:00");
        task.setDuration(Duration.ofMinutes(1));
        return task;
    }

    protected Epic createEpic() {
        Epic epic = new Epic("Epic1", "Description Epic1", TaskStatus.NEW);
//        epic.setStartTime("01.03.22,00:00");
//        epic.setDuration(Duration.ofMinutes(1));
        return epic;
    }

    protected Subtask createSubtask() {
        Epic epicForSubtask = new Epic("EpicForSubtask", "Description EpicForSubtask", TaskStatus.NEW);
//        epicForSubtask.setStartTime("01.03.22,00:00");
//        epicForSubtask.setDuration(Duration.ofMinutes(1));
        int epicId = manager.add(epicForSubtask);

        Subtask subtask = new Subtask("subtask", "Description subtask", TaskStatus.NEW, epicId);
        subtask.setStartTime("01.02.22,00:00");
        subtask.setDuration(Duration.ofMinutes(10));
        return subtask;
    }

    @Test
    public void addNewTask() {
        Task task = createTask();

        final int taskID = manager.add(task);
        final Task savedTask = manager.getTaskById(taskID);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = manager.getTaskValues();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));

        //проверяю работу при не стандартном поведении
        Task taskNull = null;
        try {
            final int taskNullId = manager.add(taskNull);
            final Task savedTaskNull = manager.getTaskById(taskNullId);
            assertNull(taskNullId, "менеджер не видит, что задача пустая");
        } catch (NullPointerException e) {
        }

    }

    @Test
    public void addNewEpic() {
        Epic epic = createEpic();

        final int epicID = manager.add(epic);
        final Epic savedTask = manager.getEpicById(epicID);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(epic, savedTask, "Задачи не совпадают");

        final List<Epic> tasks = manager.getEpicValues();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size());
        assertEquals(epic, tasks.get(0));

        //проверяю работу при не стандартном поведении
        Task taskNull = null;
        try {
            final int taskNullId = manager.add(taskNull);
            final Task savedTaskNull = manager.getTaskById(taskNullId);
            assertNull(taskNullId, "менеджер не видит, что задача пустая");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void addNewSubtask() {
        Subtask subtask = createSubtask();

        final int subtaskId = manager.add(subtask);
        final Subtask savedTask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(subtask, savedTask, "Задачи не совпадают");

        final List<Subtask> tasks = manager.getSubtaskValues();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size());
        assertEquals(subtask, tasks.get(0));

        //проверяю работу при не стандартном поведении
        Subtask taskNull = null;
        try {
            final int taskNullId = manager.add(taskNull);
            final Subtask savedTaskNull = manager.getSubtaskById(taskNullId);
            assertNull(taskNullId, "менеджер не видит, что задача пустая или не существует");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void getTaskValue() {
        Task task = createTask();
        manager.add(task);
        List<Task> savedTask = manager.getTaskValues();

        assertTrue(savedTask.contains(task), "менеджер не сохраняет epic");
        assertEquals(savedTask.get(0), task, "менеджер некорректно сохраняет taks-и");
    }

    @Test
    public void getTaskValueIfNull() {
        List<Task> savedTask = manager.getTaskValues();
        assertNotNull(savedTask, "менеджер некорректно сохраняет taks-и");
    }

    @Test
    public void getEpicValue() {
        Epic epic = createEpic();
        manager.add(epic);
        List<Epic> savedTask = manager.getEpicValues();

        assertTrue(savedTask.contains(epic), "менеджер не сохраняет epic");
        assertEquals(savedTask.get(0), epic, "менеджер некорректно сохраняет epic-и");
    }

    @Test
    public void getEpicValueIfNull() {
        List<Epic> savedTask = manager.getEpicValues();
        assertNotNull(savedTask, "менеджер некорректно сохраняет epic-и");
    }

    @Test
    public void getSubtaskValue() {
        Subtask subtask = createSubtask();
        manager.add(subtask);
        List<Subtask> savedSubtask = manager.getSubtaskValues();

        assertTrue(savedSubtask.contains(subtask), "менеджер не сохраняет subtask");
        assertEquals(savedSubtask.get(0), subtask, "менеджер некорректно сохраняет subtaks-и");
    }

    @Test
    public void getSubtaskValueIfNull() {
        List<Subtask> savedTask = manager.getSubtaskValues();
        assertNotNull(savedTask, "менеджер некорректно сохраняет taks-и");
    }

    @Test
    public void shouldGetSubtaskListFromEpic() {
        Subtask subtask = createSubtask();
        manager.add(subtask);
        int epicID = subtask.getEpicId();

        List<Subtask> subtaskList = manager.getSubtaskListFromEpic(epicID);
        assertTrue(subtaskList.contains(subtask), "по вытаскиванию subtask, менеджер" +
                "некорректно выводит subtask по EpicID");

        try {
            List<Subtask> falseSubtasks = manager.getSubtaskListFromEpic(++epicID);
            assertNull(falseSubtasks, "по вытаскиванию subtask, менеджер" +
                    "работает некорректно по несуществующем ID");
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void shouldDeleteById() {
        Task task = createTask();
        Subtask subtask = createSubtask();
        Epic epic = createEpic();

        int taskId = manager.add(task);
        int subtaskId = manager.add(subtask);
        int epicId = manager.add(epic);

        manager.deleteById(taskId);
        manager.deleteById(subtaskId);
        manager.deleteById(epicId);

        assertNull(manager.getTaskById(taskId), "менеджер не удаляет task");
        assertNull(manager.getSubtaskById(subtaskId), "менеджер не удаляет subtask");
        assertNull(manager.getEpicById(epicId), "менеджер не удаляет epic");
    }

    @Test
    public void shouldBeDeletedAllTasks() {
        manager.add(createTask());
        manager.add(createSubtask());
        manager.add(createEpic());

        manager.deleteAllTasks();

        int size = manager.getAllTasks().size();
        assertEquals(0, size, "менеджер не удаляет все задачи всех типов");
    }

    @Test
    public void shouldUpdateTask() {
        Task task = createTask();
        int taskId = manager.add(task);

        Task newTask = new Task("Task2", "Description task2", TaskStatus.NEW);
        newTask.setStartTime("01.01.22,00:00");
        newTask.setDuration(Duration.ofMinutes(1));
        newTask.setId(taskId);

        manager.update(newTask);

        assertEquals("Task2", manager.getTaskById(taskId).getTitle(),
                "программа не обновляет task корректно");
    }

    @Test
    public void shouldUpdateSubtask() {
        Subtask oldSubtask = createSubtask();
        int oldSubtaskId = manager.add(oldSubtask);
        int epicId = oldSubtask.getEpicId();

        Subtask newSubtask = new Subtask("newSubtask", "Description newSubtask", TaskStatus.NEW, epicId);
        newSubtask.setStartTime("01.01.22,00:00");
        newSubtask.setDuration(Duration.ofMinutes(1));
        newSubtask.setId(oldSubtaskId);

        manager.update(newSubtask);
        int newSubtaskId = newSubtask.getId();

        assertEquals("newSubtask", manager.getSubtaskById(newSubtaskId).getTitle(),
                "программа не обновляет subtask корректно");

    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = createEpic();
        int id = manager.add(epic);

        Epic newEpic = new Epic("epic2", "Description epic2", TaskStatus.NEW);
        newEpic.setStartTime("01.01.22,00:00");
        newEpic.setDuration(Duration.ofMinutes(1));
        newEpic.setId(id);

        manager.update(newEpic);

        assertEquals("epic2", manager.getEpicById(id).getTitle(),
                "программа не обновляет epic корректно");
    }

    @Test
    public void shouldGetAllTasks() {
        List<Task> allTasksList = manager.getAllTasks();
        assertEquals(0, allTasksList.size(), "некорректная отображение всех задач при пустом списке");

        Task task = createTask();
        manager.add(task);


        Epic epic = createEpic();
        manager.add(epic);

        Subtask subtask = createSubtask();
        manager.add(subtask);
        System.out.println("$$ - ");

        allTasksList = manager.getAllTasks();
        for (Task task1 : allTasksList) {
            System.out.println(task1);
        }


        assertEquals(4, allTasksList.size(), "некорректное отображение менеджером" +
                " количества всех задач");

        assertTrue(allTasksList.contains(task), "при вызове всех задач, не выводится subtask");
        assertTrue(allTasksList.contains(subtask), "при вызове всех задач, не выводится subtask");
        assertTrue(allTasksList.contains(epic), "при вызове всех задач, не выводится epic");
    }


    @Test
    public void updateEpicStatus() {
//        Пустой список подзадач
        Epic epic = createEpic();
        int epicId = manager.add(epic);
        manager.updateEpicStatus(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "менеджер некорректно обрабатывает" +
                "статус при пустом списке подзадач");

//        Все подзадачи со статусом NEW.
        Subtask subtask = new Subtask("subtask", "Description subtask", TaskStatus.NEW, epicId);
        subtask = getTimeSettingsForSubtask(subtask);
        manager.add(subtask);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "менеджер некорректно обрабатывает" +
                "статус c подзадачами только со статусом NEW");

//        Подзадачи со статусами NEW и DONE.
        Subtask subtask2 = new Subtask("subtask", "Description subtask", TaskStatus.DONE, epicId);
        subtask2 = getTimeSettingsForSubtask(subtask2);
        manager.add(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "менеджер некорректно обрабатывает" +
                "статус с подзадачами со статусом DONE и NEW");

//        Все подзадачи со статусом DONE.
        subtask.setStatus(TaskStatus.DONE);
        manager.update(subtask);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "менеджер некорректно обрабатывает" +
                "статус с подзадачами только со статусом DONE");

//        Подзадачи со статусом IN_PROGRESS.
        Subtask subtaskInProgress = new Subtask("subtask", "Description subtask", TaskStatus.IN_PROGRESS, epicId);
        subtaskInProgress = getTimeSettingsForSubtask(subtaskInProgress);
        manager.add(subtaskInProgress);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "менеджер некорректно обрабатывает" +
                "статус с подзадачами со статусом IN_PROGRESS");
    }

//TODO: дописать updateEpicTime
    @Test
    public void updateEpicTime() {
        Subtask subtask = createSubtask();
        int epicId = subtask.getEpicId();
        LocalDateTime subStartTime = subtask.getStartTime();
        LocalDateTime subEndTime = subtask.getEndTime();

        Duration duration = Duration.between(subStartTime, subEndTime);


        manager.add(subtask);
        manager.getEpicById(epicId).updateTimeEpic(manager.getSubtaskListFromEpic(epicId));

        assertEquals(subStartTime, manager.getEpicById(epicId).getStartTime(),
                "менеджер некорректно рассчитывает " +
                "startTime для epic-a с 1 subtask-ом");

        assertEquals(subEndTime, manager.getEpicById(epicId).getEndTime(),
                "менеджер некорректно рассчитывает " +
                "endTime для epic-a с 1 subtask-ом");

        assertEquals(duration, manager.getEpicById(epicId).getDuration(),
                "менеджер некорректно рассчитывает " +
                "продолжительность epic-a с 1 subtask-ом");

        Subtask subtask2 = new Subtask("Subtask2", "description sub2", TaskStatus.NEW, epicId);
        subtask2.setStartTime("01.01.22,00:02");
        Duration sub2Duration = duration.plusMinutes(2);
        subtask2.setDuration(sub2Duration);


        manager.add(subtask2);

        assertEquals(subStartTime, manager.getEpicById(epicId).getStartTime(),
                "менеджер некорректно рассчитывает " +
                "startTime для epic-a с 2 subtask-ами");
        assertEquals(subtask2.getEndTime(), manager.getEpicById(epicId).getEndTime(),
                "менеджер некорректно рассчитывает " +
                "endTime для epic-a с 2 subtask-ами");
        assertEquals(Duration.ofMinutes(4), manager.getEpicById(epicId).getDuration(),
                "менеджер некорректно рассчитывает " +
                "продолжительность epic-a с 2 subtask-ами");

    }

@Test
public void subtaskShouldHaveInRealEpic() {
    int randomId = (int) (Math.random() * 10);
    final Subtask subtask = getTimeSettingsForSubtask(new Subtask("Subtask", "description", TaskStatus.NEW, randomId));

    NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> manager.add(subtask)
    );
    assertEquals("Cannot invoke \"main.constructor.Epic.addSubtask(int)\" because \"epic\" is null",
            exception.getMessage(),
            "некорректное создание subtask без существующего epic-a");
}

    @Test
    public void deleteByIdAndAllTasks() {
        int taskId = manager.add(createTask());
        int subtaskId = manager.add(getTimeSettingsForSubtask(createSubtask()));
        int epicId = manager.getSubtaskById(subtaskId).getEpicId();

        manager.deleteById(taskId);
        manager.deleteById(subtaskId);
        manager.deleteById(epicId);

        assertNull(manager.getTaskById(taskId), "менеджер некорректно удаляет task по id");
        assertNull(manager.getSubtaskById(subtaskId), "менеджер некорректно удаляет subtask по id");
        assertNull(manager.getEpicById(epicId), "менеджер некорректно удаляет epic по id");
    }

    @Test
    public void deleteAllTasks() {
        int taskId = manager.add(createTask());
        int subtaskId = manager.add(getTimeSettingsForSubtask(createSubtask()));
        int epicId = manager.getSubtaskById(subtaskId).getEpicId();

        manager.deleteAllTasks();

        assertTrue(manager.getAllTasks().size() == 0, "менеджер некорректно удаляет задачи всех типов ");

        assertNull(manager.getTaskById(taskId), "менеджер некорректно удаляет task " +
                "при удалении задач всех типов");
        assertNull(manager.getSubtaskById(subtaskId), "менеджер некорректно удаляет subtask " +
                "при удалении задач всех типов");
        assertNull(manager.getEpicById(epicId), "менеджер некорректно удаляет epic " +
                "при удалении задач всех типов");
    }

    @Test
    public void getPrioritizedTasks() {

    }
}



