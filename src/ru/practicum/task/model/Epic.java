package ru.practicum.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Subtask> allSubtasks = new ArrayList<>();

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
        allSubtasks.add(subtask);
    }

    public List<Subtask> getAllSubtasks() {
        return allSubtasks;
    }

    public void clearAllSubtasks() {
        allSubtasks.clear();
    }

    public void updateStatus() {
        if (allSubtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }
        boolean isInProgress = false;
        boolean isAllDone = false;

        for (Subtask subtask : allSubtasks) {
            if (subtask.getStatus() == Status.DONE) {
                isAllDone = true;
            }
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                isInProgress = true;
            }
        }
        if (isAllDone) {
            setStatus(Status.DONE);
        } else if (isInProgress) {
            setStatus(Status.IN_PROGRESS);
        } else {
            setStatus(Status.NEW);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(allSubtasks, epic.allSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), allSubtasks);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{name='" + name +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
