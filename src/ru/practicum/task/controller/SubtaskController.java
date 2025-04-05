package ru.practicum.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.server.SubtaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @GetMapping
    public ResponseEntity<List<Subtask>> getSubtasks() {
        return subtaskService.getSubtasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getSubtaskById(@PathVariable("id") Integer id) {
        return subtaskService.getSubtaskById(id);
    }

    @PostMapping
    public ResponseEntity<Void> createOrUpdateSubtask(@RequestBody Subtask subtask) {
        return subtaskService.createOrUpdateSubtask(subtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable("id") Integer id) {
        return subtaskService.deleteSubtask(id);
    }
}

