package ru.practicum.task.model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected int id;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
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

    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s\n", id, Type.TASK, name, status, description);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", id=" + id +
                '}';
    }
}

