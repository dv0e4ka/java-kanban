package main.constructor;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return id == subtask.id
                && title.equals(subtask.title)
                && description.equals(subtask.description);
    }

    @Override
    public String toString() {
        return id + "," + "SUBTASK"
                + "," + title
                + "," + getStatus()
                + "," + description
                + "," + getStartTime().format(formatter)
                + "," + getDuration().toMinutes()
                + "," + getEpicId();
    }
}
