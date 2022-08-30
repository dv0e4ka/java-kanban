package constructor;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasks;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(int subId) {
        this.subtasks.add(subId);
    }

    public List<Integer> getSubIds() {
        return subtasks;
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubId(List<Integer> subIds) {
        this.subtasks = subIds;
    }
}
