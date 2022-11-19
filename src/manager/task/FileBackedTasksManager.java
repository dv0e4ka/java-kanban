package manager.task;

import constructor.*;
import manager.exceptions.ManagerSaveException;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path path;
    private static final String PATH = "resources/TaskManager.csv";
    private static final String HEAD = "id,type,name,status,description,epic";


    //конструктор
    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public FileBackedTasksManager()  {
        this.path = Paths.get(PATH);
    }

    //автосохранение
    private void save() {
        try{
            path = Path.of(PATH);
            String head = HEAD + System.lineSeparator();
            StringBuilder data = new StringBuilder(head);
            List<Task> allTasks = this.getAllTasks();
            for (Task task : allTasks) {
                data.append(task).append(System.lineSeparator());
            }
            data.append(System.lineSeparator());
            data.append(historyToString(historyManager));

            Files.writeString(path, data);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }
    }

    //сохранение истории в CSV
    private static String historyToString(HistoryManager manager) {
        if (manager == null) {
            return "история пуста";
        }
        List<Task> historyList = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : historyList) {
            if (task == null) continue;
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    //восстановление задачи из строки
    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String title = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];

        Task task = null;
        switch (TaskType.valueOf(type)) {
            case TASK :
                task = new Task(title, description, status);
                break;
            case SUBTASK :
                int epicId = Integer.parseInt(parts[5]);
                task = new Subtask(title, description, status, epicId);
                break;
            case EPIC :
                task = new Epic(title, description, status);
        }
        task.setId(id);
        return task;
    }

    //загрузка Менеджера из файла
    public static FileBackedTasksManager loadFromFile(Path file) throws ManagerSaveException {
        if(!Files.exists(file)) {
            return new FileBackedTasksManager();
        }
        id = 0;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Map<Integer, Task> allTasksMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile(), StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.equals(HEAD)) {
                    continue;
                }
                if (line.isBlank()) break;
                Task task = fromString(line);
                fileBackedTasksManager.add(task);
                int currId = task.getId();
                if (id < currId) id = currId;
                allTasksMap.put(task.getId(), task);
            }
            String lineWithHistory = bufferedReader.readLine();
            for (int id : historyFromString(lineWithHistory)) {
                fileBackedTasksManager.getById(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла");
        }
        return fileBackedTasksManager;
    }

    //восстановление истории из CSV
    private static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        if (value != null) {
            String[] list = value.split(",");
            for (String id : list) {
                historyList.add(Integer.parseInt(id));
            }
        }
        return historyList;
    }

    @Override
    public int add(Task task) {
        int result = super.add(task);
        save();
        return result;
    }

    @Override
    public int add(Subtask subtask) {
        int result = super.add(subtask);
        save();
        return result;
    }

    @Override
    public int add(Epic epic) {
        int result = super.add(epic);
        save();
        return result;
    }

    @Override
    public void getById(int id) {
        super.getById(id);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task result = super.getTaskById(taskId);
        save();
        return result;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask result = super.getSubtaskById(subtaskId);
        save();
        return result;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic result = super.getEpicById(epicId);
        save();
        return result;
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public List<Task> getTaskValues() {
        return super.getTaskValues();
    }

    @Override
    public List<Subtask> getSubtaskValues() {
        return super.getSubtaskValues();
    }

    @Override
    public List<Epic> getEpicValues() {
        return super.getEpicValues();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(getAllTasks.values());
    }

    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        Task task1 = new Task("Task1", "Description task1", TaskStatus.NEW);
        manager.add(task1);

        Task task2 = new Task("Task2", "Description task2", TaskStatus.NEW);
        manager.add(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1", TaskStatus.NEW);
        manager.add(epic1);

        Subtask Subtask1 = new Subtask("Subtask1", "Description Subtask1", TaskStatus.NEW, epic1.getId());
        manager.add(Subtask1);

        manager.getById(epic1.getId());
        manager.getById(task1.getId());

        loadFromFile(Path.of("TaskManager.csv"));

        Task task3 = new Task("Task3", "Description task3", TaskStatus.NEW);
        manager.add(task3);
    }
}

