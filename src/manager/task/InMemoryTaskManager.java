package manager.task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import constructor.*;
import manager.Managers;
import manager.history.*;


public class InMemoryTaskManager implements TaskManager {

    protected static int id = 0;

    protected final Map<Integer, Task> getAllTasks = new HashMap<>();
    protected final Map<Integer, Task> getTasks = new HashMap<>();
    protected final Map<Integer, Epic> getEpics = new HashMap<>();
    protected final Map<Integer, Subtask> getSubtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int add(Task task) {
        task.setId(++id);
        getTasks.put(task.getId(), task);
        getAllTasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int add(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic2 = getEpics.get(epicId);
        subtask.setId(++id);
        getSubtasks.put(subtask.getId(), subtask);
        getAllTasks.put(subtask.getId(), subtask);
        epic2.addSubtask(subtask.getId());
        updateEpicStatus(epic2);
        return subtask.getId();
    }

    @Override
    public int add(Epic epic) {
        epic.setId(++id);
        getEpics.put(epic.getId(), epic);
        getAllTasks.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void getById(int id) {
        if (getTasks.containsKey(id)) {
            getTaskById(id);
        } else if (getSubtasks.containsKey(id)) {
            getSubtaskById(id);
        } else if (getEpics.containsKey(id)) {
            getEpicById(id);
        } else {
            System.out.println("такого id нет");
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(getTasks.get(taskId));
        return getTasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(getSubtasks.get(subtaskId));
        return getSubtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(getEpics.get(epicId));
        return getEpics.get(epicId);
    }

    @Override
    public List<Integer> getEpicIds() {
        return new ArrayList<>(getEpics.keySet());
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
    public List<Subtask> getSubtaskListFromEpic(int epicId) {
        List<Integer> subtaskIdList = new ArrayList<>(getEpics.get(epicId).getSubIds());
        List<Subtask> subtasksListFromEpic = new ArrayList<>();
        for (int idSub : subtaskIdList) {
            subtasksListFromEpic.add(getSubtaskById(idSub));
        }
        return subtasksListFromEpic;
    }

    @Override
    public void deleteById(int id) {
        if (getTasks.containsKey(id)) {
            deleteTask(id);
            return;
        } else if (getSubtasks.containsKey(id)) {
            deleteSubtask(id);
            return;
        } else if (getEpics.containsKey(id)) {
            deleteEpic(id);
            return;
        } else {
            System.out.println("такого id нет");
        }
    }

    @Override
    public void deleteTask(int id) {
        if (getTasks.containsKey(id)) {
            String taskTitle = getTasks.get(id).getTitle();
            getTasks.remove(id);
            historyManager.delete(id);
            System.out.println("Удалена задача: " + taskTitle + "\n");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (getSubtasks.containsKey(id)) {
            String subTitle = getSubtasks.get(id).getTitle();
            getSubtasks.remove(id);
            historyManager.delete(id);
            System.out.println("Удалена подзадача: " + subTitle);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (getEpics.containsKey(id)) {
            List<Integer> subTasksId = getEpics.get(id).getSubIds();
            for (int subTaskId : subTasksId) {
                String subName = getSubtasks.get(subTaskId).getTitle();
                deleteById(subTaskId);
            }
            String epicTitle = getEpics.get(id).getTitle();
            getEpics.remove(id);
            historyManager.delete(id);
            System.out.println("Удалён эпик по id: " + epicTitle + "\n");
        }
    }

    @Override
    public void deleteAllTasks() {
        getTasks.clear();
        System.out.println("Удалены все задачи\n");
        getSubtasks.clear();
        System.out.println("Удалены все субзадачи\n");
        getEpics.clear();
        System.out.println("Удалены все эпики\n");
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
    public List<Task> getAllTasks() {
        return new ArrayList<>(getAllTasks.values());
    }
}