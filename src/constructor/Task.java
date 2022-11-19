package constructor;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected TaskStatus status;

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public void setId(int idNumber) {
        id = idNumber;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return id + "," + "TASK"
                + "," + title
                + "," + getStatus()
                + "," + getDescription() + ",";
    }
}
