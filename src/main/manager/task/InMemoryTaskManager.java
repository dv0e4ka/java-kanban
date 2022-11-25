package main.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import main.constructor.*;
import main.manager.Managers;
import main.manager.exceptions.ManagerSaveException;
import main.manager.history.*;


public class InMemoryTaskManager implements TaskManager {

    protected static int id = 0;
    protected final Map<Integer, Task> getAllTasks = new HashMap<>();
    protected final Map<Integer, Task> getTasks = new HashMap<>();
    protected final Map<Integer, Epic> getEpics = new HashMap<>();
    protected final Map<Integer, Subtask> getSubtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> getPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void update(Epic epic) {
        getEpics.put(epic.getId(), epic);
        getAllTasks.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    private void addPriorityTask(Task task) {
        getPrioritizedTasks.add(task);
        validateTaskPriority();
    }

    private void validateTaskPriority() {
        List<Task> listTask = new ArrayList<>(getPrioritizedTasks);
        for (int i = 1; i < listTask.size(); i++) {
            Task newTask = listTask.get(i);
            Task lastTask = listTask.get(i-1);
            System.out.println();
            boolean check = newTask.getStartTime().isAfter(lastTask.getEndTime());
            if (!check) {
                throw new ManagerSaveException("задача" + newTask.getStartTime() + "пересекается по времени выполнения с " +
                        "задачей " + lastTask.getStartTime());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int add(Task task) {
        task.setId(++id);
        addPriorityTask(task);
        getTasks.put(task.getId(), task);
        getAllTasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int add(Subtask subtask) {
        int epicId = subtask.getEpicId();
        addPriorityTask(subtask);
        Epic epic = getEpics.get(epicId);
        subtask.setId(++id);
        getSubtasks.put(subtask.getId(), subtask);
        getAllTasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
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
            subtasksListFromEpic.add(getSubtasks.get(idSub));
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
        getPrioritizedTasks.removeIf(task -> task.getId() == id);
        if (getTasks.containsKey(id)) {
            String taskTitle = getTasks.get(id).getTitle();
            getTasks.remove(id);
            historyManager.delete(id);
            System.out.println("Удалена задача: " + taskTitle + "\n");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        getPrioritizedTasks.removeIf(task -> task.getId() == id);
        if (getSubtasks.containsKey(id)) {
            String subTitle = getSubtasks.get(id).getTitle();
            getSubtasks.remove(id);
            historyManager.delete(id);
            System.out.println("Удалена подзадача: " + subTitle);
        }
    }

    @Override
    public void deleteEpic(int id) {
        getPrioritizedTasks.removeIf(epic -> epic.getId() == id);
        if (getEpics.containsKey(id)) {
            List<Integer> subTasksId = getEpics.get(id).getSubIds();
            for (int subTaskId : subTasksId) {
                getPrioritizedTasks.removeIf(subtask -> subtask.getId() == subTaskId);
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
        getPrioritizedTasks.clear();
        System.out.println("Дерево приоритета очищен\n");
        getTasks.clear();
        System.out.println("Удалены все задачи\n");
        getSubtasks.clear();
        System.out.println("Удалены все субзадачи\n");
        getEpics.clear();
        System.out.println("Удалены все эпики\n");
        getAllTasks.clear();
        System.out.println("Удаление завершено");
    }

    @Override
    public void update(Task task) {
        getPrioritizedTasks.removeIf(task1 -> task1.getId() == task.getId());
        addPriorityTask(task);
        getTasks.put(task.getId(), task);
    }

    @Override
    public void update(Subtask subtask) {
        getPrioritizedTasks.removeIf(task -> task.getId() == subtask.getId());
        addPriorityTask(subtask);
        getSubtasks.put(subtask.getId(), subtask);
        getAllTasks.put(subtask.getId(), subtask);

        Epic epic = getEpics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());

        updateEpicStatus(epic);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        updateEpicTime(epic);
        List<Integer> subtasksId = epic.getSubIds();
        int subtasksCount = subtasksId.size();
        if (subtasksCount == 0) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        int newCount = 0;
        int doneCount = 0;
        epic.setStatus(TaskStatus.IN_PROGRESS);
        for (int id : subtasksId) {
            if (getSubtasks.get(id).getStatus().equals(TaskStatus.DONE)) {
                doneCount++;
            } else if (getSubtasks.get(id).getStatus().equals(TaskStatus.NEW)) {
                newCount++;
            }
        }
        if (subtasksCount == newCount) {
            epic.setStatus(TaskStatus.NEW);
        } else if (subtasksCount == doneCount) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        int epicId = epic.getId();
        getPrioritizedTasks.removeIf(task -> task.getId() == epicId);
        addPriorityTask(epic);

    }

    @Override
    public void updateEpicTime(Epic epic) {
        List<Subtask> subtasksList = getSubtaskListFromEpic(epic.getId());
        LocalDateTime newStartTime = LocalDateTime.MAX;
        LocalDateTime newEndTime = LocalDateTime.MIN;

        for (Subtask subtask : subtasksList) {
            if (subtask.getStartTime().isBefore(newStartTime)) {
                newStartTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(newEndTime)) {
                newEndTime = subtask.getEndTime();
            }
        }

        Duration epicDuration = Duration.between(newStartTime, newEndTime);
        epic.setStartTime(newStartTime);
        epic.setEndTime(newEndTime);
        epic.setDuration1(epicDuration);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(getAllTasks.values());
    }
}