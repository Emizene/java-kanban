package ru.practicum.task.service.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.TaskManager;

import java.util.Set;

@Service
public class PrioritizedService {
    private final TaskManager taskManager;

    public PrioritizedService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ResponseEntity<Set<Task>> getPrioritizedTasks() {
        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        if (prioritizedTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(prioritizedTasks);
    }

}
