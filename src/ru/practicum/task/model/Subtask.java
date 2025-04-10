package ru.practicum.task.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask() {
    }

    public Subtask(String name, String description, Status status,
                   Integer epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status,
                   Duration duration, LocalDateTime startTime, Integer epicId) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask makeSubtaskCopy() {
        return new Subtask(this.getId(), this.getName(), this.getDescription(), this.getStatus(), this.getEpicId());
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toFileString() {
        if (duration == null) {
            duration = Duration.ofMinutes(0);
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", id, Type.SUBTASK, name, status, description,
                epicId, duration.toMinutes(), startTime, getEndTime());
    }

}
