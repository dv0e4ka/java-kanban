package manager.task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import constructor.*;
import manager.Managers;
import manager.history.*;


public class InMemoryTaskManager implements TaskManager {

    private static int id = 0;

    private final Map<Integer, Task> getTasks = new HashMap<>();

    private final Map<Integer, Epic> getEpics = new HashMap<>();

    private final Map<Integer, Subtask> getSubtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();




    @Override
    public void add(Task task) {
        task.setId(++id);
        getTasks.put(task.getId(), task);
    }

    @Override
    public void add(Subtask subtask) {
        subtask.setId(++id);
        getSubtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void add(Epic epic) {
        epic.setId(++id);
        updateEpicStatus(epic);
        getEpics.put(epic.getId(), epic);
    }

    @Override
    public List<List> getAllTasksList() {
        List<List> listAllTask = new ArrayList<>();
        listAllTask.add(getEpicValues());
        listAllTask.add(getSubtaskValues());
        listAllTask.add(getTaskValues());
        return listAllTask;
    }

    @Override
    public List<Task> getTaskValues() {
        return new ArrayList<>(getTasks.values());
    }

    @Override
    public List<Subtask> getSubtaskValues() {
        return new ArrayList<>(getSubtasks.values());
    }

    @Override
    public List<Epic> getEpicValues() {
        return new ArrayList<>(getEpics.values());
    }


    @Override
    public Task getTaskById(int taskId) {
        historyManager.addHistory(getTasks.get(taskId));
        return getTasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.addHistory(getSubtasks.get(subtaskId));
        return getSubtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.addHistory(getEpics.get(epicId));
        return getEpics.get(epicId);
    }

    @Override
    public void removeById(int id) {
        if (getTasks.containsKey(id)) removeTask(id);
        if (getSubtasks.containsKey(id)) removeSubtask(id);
        if (getEpics.containsKey(id)) removeEpic(id);
    }

    @Override
    public void removeTask(int id) {
        getTasks.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        getSubtasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        getEpics.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        getTasks.clear();
        getSubtasks.clear();
        getEpics.clear();
    }

    @Override
    public void update(Task task) {
        getTasks.put(task.getId(), task);
    }

    @Override
    public void update(Subtask subtask) {
        getSubtasks.put(subtask.getEpicId(), subtask);
        Epic epic = getEpics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public void update(Epic epic) {
        getTasks.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        List<Integer> subtasksId = epic.getSubIds();
        for (int id : subtasksId) {
            if (getSubtasks.get(id).getStatus().equals(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
                break;
            } else if (getSubtasks.get(id).getStatus() .equals(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (epic.getStatus().equals(TaskStatus.DONE) && getSubtasks.get(id).getStatus().equals(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }
    }

    @Override
    public List<Subtask> getSubtaskListFromEpic(int epicId) {
        List<Integer> subtaskIdList = new ArrayList<>(getEpics.get(epicId).getSubtasks());
        List<Subtask> subtasksListFromEpic = new ArrayList<>();
        for (int idSub : subtaskIdList) {
            subtaskIdList.add(idSub);
        }
        return subtasksListFromEpic;
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Integer> getEpicIds() {
        return new ArrayList<>(getEpics.keySet());
    }
}