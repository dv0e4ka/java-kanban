package constructor;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
