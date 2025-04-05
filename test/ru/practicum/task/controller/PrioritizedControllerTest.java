package ru.practicum.task.controller;

import org.junit.jupiter.api.Test;
import ru.practicum.task.BaseTest;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrioritizedControllerTest extends BaseTest {

    @Test
    public void testSuccessGetPrioritizedTasks() throws Exception {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(20),
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        mockMvc.perform(get("/prioritized"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"id": 1,
                        		"name": "TASK №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": "PT1H",
                        		"startTime": "2025-03-17T10:00:00",
                        		"endTime": "2025-03-17T11:00:00"
                        	},
                        	{
                        		"id": 2,
                        		"name": "TASK №2",
                        		"description": "DESCRIPTION №2",
                        		"status": "NEW",
                        		"duration": "PT20M",
                        		"startTime": "2025-03-17T16:30:00",
                        		"endTime": "2025-03-17T16:50:00"
                        	}
                        ]
                        """));
    }

}