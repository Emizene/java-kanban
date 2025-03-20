package ru.practicum.task.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected LocalDateTime endTime = startTime;
    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, Status status, LocalDateTime startTime) {
        super(name, description, status, startTime);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Status status, LocalDateTime startTime) {
       super(id, name, description, status, startTime);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic getEpicCopy() {
        return new Epic(this.getId(), this.getName(), this.getDescription(), this.getStatus());
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        List<Subtask> epicSubtasks = subtasks.stream()
                .filter(sub -> sub.getEpicId() == id)
                .toList();

        startTime = epicSubtasks.stream()
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        endTime = epicSubtasks.stream()
                .map(Task::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        duration = Duration.ofMinutes(epicSubtasks.stream()
                .mapToLong(sub -> sub.getDuration().toMinutes())
                .sum());
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean isInProgress = false;
        boolean isNew = false;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                isInProgress = true;
            }
            if (subtask.getStatus() == Status.NEW) {
                isNew = true;
            }

        }
        if (isInProgress) {
            setStatus(Status.IN_PROGRESS);
        } else if (isNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.DONE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) {
            return "Epic{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", id=" + id +
                    ", 'time' = no info" +
                    '}';
        }
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public String toFileString() {
        if (duration == null) {
            duration = Duration.ofMinutes(0);
        }
        return String.format("%s,%s,%s,%s,%s\n", id, Type.EPIC, name, status, description,
                duration.toMinutes(), startTime, getEndTime());
    }

}
