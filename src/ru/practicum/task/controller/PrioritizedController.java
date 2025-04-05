package ru.practicum.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.server.PrioritizedService;

import java.util.Set;

@RestController
@RequestMapping("/prioritized")
public class PrioritizedController {
    private final PrioritizedService prioritizedService;

    public PrioritizedController(PrioritizedService prioritizedService) {
        this.prioritizedService = prioritizedService;
    }

    @GetMapping
    public ResponseEntity<Set<Task>> getPrioritizedTasks() {
        return prioritizedService.getPrioritizedTasks();
    }
}
