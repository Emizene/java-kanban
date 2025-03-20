package ru.practicum.task.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected int id;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task getTaskCopy() {
        return new Task(this.getId(), this.getName(), this.getDescription(), this.getStatus());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) {
            return this.getClass().getSimpleName() +
                    "{name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status='" + status + '\'' +
                    ", id='" + id + '\'' +
                    ", 'time' = no info" +
                    '}';
        }
        return this.getClass().getSimpleName() +
                "{name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                ", endTime=" + getEndTime() +
                '}';
    }

    public String toFileString() {
        if (duration == null) {
            duration = Duration.ofMinutes(0);
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", id, Type.TASK, name, status, description,
                duration.toMinutes(), startTime, getEndTime());
    }
}

