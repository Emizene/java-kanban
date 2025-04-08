package ru.practicum.task.service;

import ru.practicum.task.service.history.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @Override
    protected InMemoryHistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
