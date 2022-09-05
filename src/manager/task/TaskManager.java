package manager.task;

import constructor.*;
import manager.history.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    int add(Task task);

    int add(Subtask subtask);

    int add(Epic epic);

    List<List> getAllTasksList();

    void getById(int id);

    List<Task> getTaskValues();

    List<Subtask> getSubtaskValues();

    List<Epic> getEpicValues();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void removeById(int id);

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    void deleteAllTasks();

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    List<Subtask> getSubtaskListFromEpic(int id);

    List<Task> getHistory();

    List<Integer> getEpicIds();
}
