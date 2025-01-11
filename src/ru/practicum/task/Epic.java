package ru.practicum.task;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> allSubtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubtask(Subtask subtask) {
        allSubtasks.add(subtask);
    }

    public ArrayList<Subtask> getAllSubtasks() {
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
