package ru.practicum.task.service.server;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.TaskManager;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskManager taskManager;

    public TaskService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskManager.getAllTasks();
        if (allTasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allTasks);
    }

    public ResponseEntity<Task> getTaskById(Integer id) {
        Optional<Task> taskById = taskManager.getTaskById(id);
        return taskById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> createOrUpdateTask(Task task) {
        if (task.getId() == null) {
            taskManager.addTask(task);
            return ResponseEntity.status(201).build();
        }
        taskManager.updateTask(task);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteTask(Integer id) {
        Optional<Task> task = taskManager.getTaskById(id);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskManager.deleteTask(id);
        return ResponseEntity.ok().build();
    }

}
