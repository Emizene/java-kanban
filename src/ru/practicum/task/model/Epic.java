package ru.practicum.task.model;

import ru.practicum.task.service.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> allSubtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubtask(Subtask subtask) {
        allSubtasks.add(subtask);
    }

    public List<Subtask> getAllSubtasks() {
        return allSubtasks;
    }

    public void clearAllSubtask() {
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
}
