package ru.practicum.task.service.server;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.manager.TaskManager;

import java.util.List;
import java.util.Optional;

@Service
public class EpicService {
    private final TaskManager taskManager;

    public EpicService(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ResponseEntity<List<Epic>> getAllEpics() {
        List<Epic> allEpics = taskManager.getAllEpics();
        if (allEpics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allEpics);
    }

    public ResponseEntity<Epic> getEpicById(Integer id) {
        Optional<Epic> epicById = taskManager.getEpicById(id);
        return epicById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Subtask>> getEpicSubtasks(Integer id) {
        Optional<Epic> epicById = taskManager.getEpicById(id);
        return epicById.map(epic -> ResponseEntity.ok(epic.getSubtasks())).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> createOrUpdateEpic(Epic epic) {
        if (epic.getId() == null) {
            taskManager.addEpic(epic);
            return ResponseEntity.status(201).build();
        }
        taskManager.updateEpic(epic);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteEpic(Integer id) {
        Optional<Epic> epic = taskManager.getEpicById(id);
        if (epic.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskManager.deleteEpic(id);
        return ResponseEntity.ok().build();
    }

}
