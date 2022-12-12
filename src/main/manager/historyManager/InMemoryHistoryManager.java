package main.manager.historyManager;

import java.util.*;

import main.constructor.Epic;
import main.constructor.Task;
import main.constructor.TaskStatus;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final Map<Integer, Node> linkMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;
        delete(task.getId());
        Node<Task> node = history.linkLast(task);
        linkMap.put(task.getId(), node);
    }

    @Override
    public void delete(int id) {
        if (linkMap.containsKey(id)) {
            history.deleteNode(linkMap.get(id));
            linkMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    public class CustomLinkedList<Task> {
        private Node<Task> tail;
        private Node<Task> head;

        private Node<Task> linkLast(Task task) {
            Node<Task> newNode = new Node<>(null, task, null);
            if (head == null) {
                head = newNode;
                tail = newNode;
//                return head;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
                tail = newNode;
            }
            return newNode;
        }

        public void deleteNode(Node node) {
            if (node == null) return;
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;


            if (head == node) {
                head = next;
            }
            if (tail == node) {
                tail = prev;
            }

            if (prev != null) {
               prev.next = next;
            }

            if (next != null) {
                next.prev = prev;
            }
        }

        public List<Task> getTasks() {
            final List<Task> tasksList = new ArrayList<>();
            Node<Task> currNode = head;
            while (currNode != null) {
                tasksList.add(currNode.data);
                currNode = currNode.next;
            }
            return tasksList;
        }
    }

    private static class Node<T> {
         public T data;
         public Node<T> next;
         public Node<T> prev;

         public Node(Node<T> prev, T data, Node<T> next) {
             this.prev = prev;
             this.data = data;
             this.next = next;
         }
    }
}
