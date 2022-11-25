package main.manager.task;

import main.constructor.*;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    int add(Task task);

    int add(Subtask subtask);

    int add(Epic epic);

    void getById(int id);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

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

    public void updateEpicStatus(Epic epic);

    public void updateEpicTime(Epic epic);
}
