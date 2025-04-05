package ru.practicum.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.server.EpicService;

import java.util.List;

@RestController
@RequestMapping("/epics")
public class EpicController {
    private final EpicService epicService;

    public EpicController(EpicService epicService) {
        this.epicService = epicService;
    }

    @GetMapping
    public ResponseEntity<List<Epic>> getAllEpics() {
        return epicService.getAllEpics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Epic> getEpicById(@PathVariable("id") Integer id) {
        return epicService.getEpicById(id);
    }

    @GetMapping("/{id}/subtask")
    public ResponseEntity<List<Subtask>> getEpicSubtasks(@PathVariable("id") Integer id) {
        return epicService.getEpicSubtasks(id);
    }

    @PostMapping
    public ResponseEntity<Void> createOrUpdateEpic(@RequestBody Epic epic) {
        return epicService.createOrUpdateEpic(epic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable("id") Integer id) {
        return epicService.deleteEpic(id);
    }
}
