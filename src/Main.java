import constructor.*;
import manager.Managers;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager task = Managers.getDefaultTask();

        // Создаём три простых задачи
        Task task1 = new Task("Task1", "description Task1", TaskStatus.NEW);
        int task1Id = task.add(task1);
        System.out.println("Id созданного Task1 - " + task1Id);

        Task task2 = new Task("Task2", "description Task2", TaskStatus.NEW);
        int task2Id = task.add(task2);
        System.out.println("Id созданного Task2 - " + task2Id);

        Task task3 = new Task("Task3", "description Task3", TaskStatus.NEW);
        int task3Id = task.add(task3);
        System.out.println("Id созданного Task3 - " + task3Id);

        // создаём Epic1 с тремя подзадачами
        Epic epic1 = new Epic("Epic1", "description Epic1", TaskStatus.NEW);
        int epic1Id = task.add(epic1);
        System.out.println("Id созданного Epic1 - " + epic1Id);

        //  1 подзадачу Epic1
        Subtask sub1Epic1 = new Subtask("Sub1 Epic1", "description Sub1 Epic1", TaskStatus.NEW, epic1Id);
        int sub1Epic1Id = task.add(sub1Epic1);
        System.out.println("Id созданного Sub1 Epic1 Id - " + sub1Epic1Id);

        //  2 подзадачу Epic1
        Subtask sub2Epic1 = new Subtask("sub2 Epic1", "Sub2 Epic1", TaskStatus.NEW, epic1Id);
        int sub2Epic1Id = task.add(sub2Epic1);
        System.out.println("Id созданного Sub2 Epic1 Id - " + sub2Epic1Id);

        //  3 подзадачу Epic1
        Subtask sub3Epic1 = new Subtask("sub3 Epic1", "Sub3 Epic1", TaskStatus.NEW, epic1Id);
        int sub3Epic1Id = task.add(sub3Epic1);
        System.out.println("Id созданного Sub3 Epic1 Id - " + sub3Epic1Id);

        //создаем Epic2 без подзадач
        Epic epic2 = new Epic("Epic2", "description Epic2", TaskStatus.NEW);
        int epic2Id = task.add(epic2);
        System.out.println("Id созданного Epic2 - " + epic2Id);


        // запрашиваю задачи несколько раз в разном порядке
        task.getById(task1Id);
        task.getById(task2Id);
        task.getById(epic1Id);
        task.getById(sub1Epic1Id);
        task.getById(epic2Id);
        System.out.println("История вызова должна содержать: [task1, task2, epic1, sub1Epic1, epic2]");
        System.out.println(task.getHistory());
        System.out.println();

        task.getById(task1Id);     // [task2, epic1, sub1Epic1, epic2, - task1]
        task.getById(epic1Id);     // [task2, sub1Epic1, epic2, task1, - epic1]
        task.getById(sub1Epic1Id); // [task2, epic2, task1, epic1, - sub1Epic1]
        System.out.println("История вызова должна содержать: [task2, epic2, task1, epic1, - sub1Epic1]");
        System.out.println(task.getHistory());
        System.out.println();


        task.removeById(task2Id);
        System.out.println("История вызова должна содержать: [epic2, task1, epic1, - sub1Epic1]");
        System.out.println(task.getHistory());
        System.out.println();

        task.removeById(epic1Id);
        System.out.println("История вызова должна содержать: [epic2, task1]");
        System.out.println(task.getHistory());
    }
}