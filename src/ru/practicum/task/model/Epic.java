package ru.practicum.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    private Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic getEpicCopy() {
        return new Epic(this.getId(), this.getName(), this.getDescription(), this.getStatus());
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
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

}
