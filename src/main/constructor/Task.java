package main.constructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected TaskType taskType = TaskType.TASK;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;
    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy,mm:HH");
    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
        setEndTime(startTime.plus(duration));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
    }

    public Duration getDuration() {
        return duration;
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id
                && title.equals(task.title)
                && description.equals(task.description);
    }

    @Override
    public String toString() {
        return id + "," + "TASK"
                + "," + title
                + "," + getStatus()
                + "," + getDescription()
                + "," + "start: " + getStartTime().format(formatter)
                + "," + "duration: " + getDuration().toMinutes() + "min";
    }
}
