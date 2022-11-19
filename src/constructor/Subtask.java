package constructor;

public class Subtask extends Task {
    protected int epicId;
    TaskType taskType = TaskType.SUBTASK;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return id + "," + "SUBTASK"
                + "," + title
                + "," + getStatus()
                + "," + getDescription()
                + "," + getEpicId();
    }
}
