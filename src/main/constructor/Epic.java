package main.constructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasks;
    TaskType taskType = TaskType.EPIC;
    protected LocalDateTime startTime;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
        subtasks = new ArrayList<>();
        setStartTime(LocalDateTime.MIN.format(formatter));
        setDuration1(Duration.ZERO);
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setDuration1(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtask(int subId) {
        if (!subtasks.contains(subId)) {
            this.subtasks.add(subId);
        }
    }

    public List<Integer> getSubIds() {
        return subtasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Epic epic = (Epic) obj;
        return id == epic.id
                && title.equals(epic.title)
                && description.equals(epic.description);
    }

    @Override
    public String toString() {
        return id + "," + "EPIC"
                + "," + title
                + "," + getStatus()
                + "," + description
                + "," + "start: " + getStartTime().format(formatter)
                + "," + "duration: " + getDuration().toMinutes() + "min";
    }
}
