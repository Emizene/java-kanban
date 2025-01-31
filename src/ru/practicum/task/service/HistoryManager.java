package ru.practicum.task.service;

import ru.practicum.task.model.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskInHistory(Task task);

    List<Task> getHistory();

}

