package main.manager.taskManager;

import main.constructor.*;
import main.exceptions.ManagerSaveException;
import main.manager.historyManager.HistoryManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static main.constructor.TaskType.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static Path path;
    private static final String PATH = "resources/TaskManager.csv";
    private static final String HEAD = "id,type,name,status,description,epic";


    //конструктор
    public FileBackedTasksManager(Path path) throws IOException {
        this.path = path;
    }

    public FileBackedTasksManager()  {
        this.path = Paths.get(PATH);
    }

    //автосохранение
    private void save() {
        try{
            if (!Files.exists(path)) {
                Files.createDirectories(Paths.get("resources"));
                path = Path.of(PATH);
            }
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
        String startTime = parts[5];
        Duration duration = Duration.ofMinutes(Integer.parseInt(parts[6]));
        switch (TaskType.valueOf(type)) {
            case TASK -> {
                Task task = new Task(title, description, status);
                task.setStartTime(startTime);
                task.setDuration(duration);
                task.setId(id);
                return task;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(parts[7]);
                Subtask subtask = new Subtask(title, description, status, epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                subtask.setId(id);
                subtask.setTaskType(TaskType.SUBTASK);
                return subtask;
            }
            case EPIC -> {
                Epic epic = new Epic(title, description, status);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                epic.setId(id);
                epic.setTaskType(TaskType.EPIC);
                return epic;
            }
        }
        return null;
    }

    //загрузка Менеджера из файла
    public FileBackedTasksManager loadFromFile() throws ManagerSaveException, IOException {
        if(!Files.exists(path)) {
            return new FileBackedTasksManager();
        }
        id = 0;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        Map<Integer, Task> allTasksMap = new HashMap<>();
        List<Subtask> subtasks = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.equals(HEAD)) {
                    continue;
                }
                if (line.isBlank()) break;
                var task = fromString(line);
                switch (task.getTaskType()) {
                    case EPIC -> {
                        Epic epic = (Epic) fromString(line);
                        fileBackedTasksManager.add(epic);
                    }
                    case SUBTASK -> {
                        Subtask subtask = (Subtask) fromString(line);
                        fileBackedTasksManager.add(subtask);
                    }
                    case TASK -> {
                        fileBackedTasksManager.add(task);
                    }
                }
                int currId = task.getId();
                if (id < currId) id = currId;
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

    @Override
    public List<Subtask> getSubtaskListFromEpic(int epicId) {
        return super.getSubtaskListFromEpic(epicId);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
    }

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager();

        Epic epic1 = new Epic("Epic1", "Description epic1", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic2", "Description epic2", TaskStatus.NEW);

        manager.add(epic1);
        manager.add(epic2);

        Subtask subtask = new Subtask("Subtask1", "Description Subtask1", TaskStatus.NEW, epic1.getId());
        subtask.setStartTime("01.01.22/00:00");
        subtask.setDuration(Duration.ofMinutes(1));
        manager.add(subtask);

        manager.getById(epic1.getId());

        FileBackedTasksManager newManager = new FileBackedTasksManager().loadFromFile();
        System.out.println("размер getAllTasks - " + manager.getAllTasks.size());
        System.out.println("размер getTasks - " + manager.getTasks.size());
        System.out.println("размер getEpics - " + manager.getEpics.size());
        System.out.println("размер getSubtasks - " + manager.getSubtasks.size() + "\r\n");

        System.out.println("newManager: размер getAllTasks - " + newManager.getAllTasks.size());
        System.out.println("newManager: размер getTasks - " + newManager.getTasks.size());
        System.out.println("newManager: размер getEpics - " + newManager.getEpics.size());
        System.out.println("newManager: размер getSubtasks - " + newManager.getSubtasks.size());
    }
}

