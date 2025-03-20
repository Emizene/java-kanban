package ru.practicum.task.service.history;

import ru.practicum.task.model.Task;

import java.util.List;

public interface HistoryManager {

    void addInHistory(Task task);

    void remove(int id);

    List<Task> getHistory();

    void clearAllHistory();

}

