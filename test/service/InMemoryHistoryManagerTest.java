package service;

import ru.practicum.task.service.history.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

        @Override
        protected InMemoryHistoryManager getDefaultHistoryManager() {
            return new InMemoryHistoryManager();
        }
    }

