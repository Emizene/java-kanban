package ru.practicum.task.service.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.TaskManager;

import java.util.List;

@Service
public class HistoryService {
    private final TaskManager taskManager;

    public HistoryService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ResponseEntity<List<Task>> getHistory() {
        List<Task> history = taskManager.getHistory();
        if (history.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(history);
    }

}
