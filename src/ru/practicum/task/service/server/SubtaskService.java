package ru.practicum.task.service.server;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.manager.TaskManager;

import java.util.List;
import java.util.Optional;

@Service
public class SubtaskService {
    private final TaskManager taskManager;

    public SubtaskService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ResponseEntity<List<Subtask>> getSubtasks() {
        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        if (allSubtasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allSubtasks);
    }

    public ResponseEntity<Subtask> getSubtaskById(Integer id) {
        Optional<Subtask> subtasksById = taskManager.getSubtaskById(id);
        return subtasksById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> createOrUpdateSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            taskManager.addSubtask(subtask);
            return ResponseEntity.status(201).build();
        }
        taskManager.updateSubtask(subtask);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteSubtask(Integer id) {
        Optional<Subtask> subtask = taskManager.getSubtaskById(id);
        if (subtask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskManager.deleteSubtask(id);
        return ResponseEntity.ok().build();
    }

}
