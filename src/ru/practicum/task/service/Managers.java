package ru.practicum.task.service;

import ru.practicum.task.service.history.HistoryManager;
import ru.practicum.task.service.history.InMemoryHistoryManager;
import ru.practicum.task.service.manager.InMemoryTaskManager;
import ru.practicum.task.service.manager.TaskManager;

public final class Managers {

    private Managers() {}

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}

