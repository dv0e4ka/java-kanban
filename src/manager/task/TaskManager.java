package manager.task;

import constructor.*;
import manager.history.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    List<Task> getHistory();

    int add(Task task);

    int add(Subtask subtask);

    int add(Epic epic);

    void getById(int id);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    List<Integer> getEpicIds();

    List<Task> getTaskValues();

    List<Subtask> getSubtaskValues();

    List<Subtask> getSubtaskListFromEpic(int id);

    List<Epic> getEpicValues();

    void deleteById(int id);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    void deleteAllTasks();

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    public List<Task> getAllTasks();
}
