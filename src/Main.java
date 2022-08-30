import constructor.*;
import manager.Managers;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager task = Managers.getDefaultTask();


        Task work = new Task("Проект", "Изменить главный метод", TaskStatus.NEW);
        Epic travel = new Epic("Поехать в Индию", "Подготовиться", TaskStatus.NEW);
        Subtask tickets = new Subtask("Билеты", "Выбрать лучший вариант", TaskStatus.DONE, 1);
        Subtask bag = new Subtask("Вещи", "Собрать чемодан", TaskStatus.NEW, 1);
        Epic car = new Epic("Машина", "ТО", TaskStatus.NEW);
        Subtask oil = new Subtask("Масло", "Поменять", TaskStatus.NEW, 2);
        task.add(work);
        task.add(travel);
        task.add(tickets);
        task.add(bag);
        task.add(car);
        task.add(oil);


        task.getTaskById(1);
        System.out.println(task.getHistory());

        task.getEpicById(2);
        System.out.println(task.getHistory());

        task.getEpicById(2);
        System.out.println(task.getHistory());

        task.getEpicById(3);
        System.out.println(task.getHistory());

        task.getSubtaskById(4);
        System.out.println(task.getHistory());
    }
}