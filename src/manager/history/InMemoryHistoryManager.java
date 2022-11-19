package manager.history;

import java.util.*;
import constructor.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final Map<Integer, Node> linkMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;
        delete(task.getId());
        history.linkLast(task);
        linkMap.put(task.getId(), history.tail.prev);
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

        private void linkLast(Task task) {
            if (head == null) {
                Node<Task> currNode = new Node<>(null, task, tail);
                head = currNode;
                tail = new Node<>(currNode, null, null);
                return;
            }
                Node<Task> currNode = tail;
                currNode.data = task;
                tail = new Node<>(currNode, null, null);
                currNode.prev.next = currNode;
                currNode.next = tail;
        }

        public void deleteNode(Node node) {
            if (node == null) return;


            if (head == node) {
               head = node.next;
               if (node.next != null) {
                   node.next.prev = null;
               }
            } else {
               node.prev.next = node.next;
               if (node.next != null) {
                   node.next.prev = node.prev;
               }
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
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
