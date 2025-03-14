package ru.practicum.task.service.manager;

import ru.practicum.task.service.HistoryManager;
import ru.practicum.task.service.InMemoryHistoryManager;

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
