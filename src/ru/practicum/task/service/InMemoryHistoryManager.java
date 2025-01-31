package ru.practicum.task.service;

import ru.practicum.task.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager{

    private final int MAX_SIZE_HISTORY = 10;

    private final List<Task> historyStorage = new ArrayList<>();

    @Override
    public void addTaskInHistory(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        if (historyStorage.size() == MAX_SIZE_HISTORY) {
            historyStorage.removeFirst();
        }

        historyStorage.add(task.getTaskCopy());

    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }

}
