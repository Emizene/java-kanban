package ru.practicum.task.service.manager;

import ru.practicum.task.service.history.HistoryManager;
import ru.practicum.task.service.history.InMemoryHistoryManager;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
