package manager.task;

import constructor.Epic;
import constructor.Subtask;
import constructor.Task;
import manager.history.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;


public class FileBackedTasksManager extends InMemoryTaskManager {
    static File historySavesFile;
    File dir;

    public static void main (String[] args) throws IOException {
        historySavesFile = new File("historySavesFile");
        if (!historySavesFile.exists()) {
            historySavesFile.createNewFile();
        }
    }
    public FileBackedTasksManager() {
        super();
    }

    public void save() throws ManagerSaveException {
        try {
            PrintWriter printHistory = new PrintWriter(historySavesFile);
            printHistory.printf("%s,%s,%s,%s,%s,%s", "id","type","name","status","description","epic");
            printHistory.printf(getHistoryManager());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка: " + e.getStackTrace());
        }
    }


    public String toString(Task task) {
        //TODO:
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        String historyToString =
        return null;
    }

    public static List<Integer> historyFromString(String value) {
        //TODO:
        return null;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        //TODO:
        return null;
    }
    public Task fromString(String value) {
        //TODO:
        return null;
    }

    @Override
    public int add(Task task) {
        int addTask = super.add(task);
        save();
        return addTask;
    }

    @Override
    public int add(Subtask subtask) {
        int addSubtask = super.add(subtask);
        save();
        return addSubtask;
    }

    @Override
    public int add(Epic epic) {
        int addEpic = super.add(epic);
        save();
        return addEpic;
    }



    @Override
    public void getById(int id) {
        super.getById(id);
    }

    @Override
    public Task getTaskById(int taskId) {
        Task getTaskById = super.getTaskById(taskId);
        save();
        return getTaskById;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask getSubtaskById = super.getSubtaskById(subtaskId);
        return getSubtaskById;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic getEpicById = super.getEpicById(epicId);
        return getEpicById;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }


}
