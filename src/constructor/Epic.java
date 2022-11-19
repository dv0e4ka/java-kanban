package constructor;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasks;
    TaskType taskType = TaskType.EPIC;

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

    @Override
    public String toString() {
        return id + "," + "EPIC"
                + "," + title
                + "," + getStatus()
                + "," + getDescription() + ",";
    }
}
