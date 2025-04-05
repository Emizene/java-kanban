package ru.practicum.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.BaseTest;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        taskManager.clearAll();
        historyManager.clearAllHistory();
    }

    @Test
    public void testSuccessGetTasks() throws Exception {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        taskManager.addTask(task1);
        mockMvc.perform(get("/tasks"))
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
                        	}
                        ]
                        """));
    }

    @Test
    public void testSuccessGetTaskById() throws Exception {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        taskManager.addTask(task1);
        taskManager.getTaskById(task1.getId());
        mockMvc.perform(get("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        	"id": 1,
                        	"name": "TASK №1",
                        	"description": "DESCRIPTION №1",
                        	"status": "NEW",
                        	"duration": "PT1H",
                        	"startTime": "2025-03-17T10:00:00",
                        	"endTime": "2025-03-17T11:00:00"
                        }
                        """));
    }

    @Test
    public void testSuccessUpdateTask() throws Exception {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        taskManager.addTask(task1);
        mockMvc.perform(post("/tasks")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                	"id": 1,
                                	"name": "UPDATE TASK №1",
                                	"description": "UPDATE DESCRIPTION №1",
                                	"status": "NEW",
                                	"duration": "PT1H",
                                	"startTime": "2025-03-17T10:00:00",
                                	"endTime": "2025-03-17T11:00:00"
                                }
                                """))
                .andExpect(status().isOk());
        Task taskAfterUpdate = taskManager.getTaskById(task1.getId()).get();
        assertEquals("UPDATE TASK №1", taskAfterUpdate.getName());
        assertEquals("UPDATE DESCRIPTION №1", taskAfterUpdate.getDescription());
        assertEquals(task1.getDuration(), taskAfterUpdate.getDuration());
    }

    @Test
    public void testSuccessCreateTask() throws Exception {
        assertEquals(0, taskManager.getAllTasks().size());
        mockMvc.perform(post("/tasks")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                	"name": "NEW TASK №1",
                                	"description": "NEW DESCRIPTION №1",
                                	"status": "NEW",
                                	"duration": "PT1H",
                                	"startTime": "2025-03-17T10:00:00"
                                }
                                """))
                .andExpect(status().isCreated());
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void testSuccessDeleteTask() throws Exception {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        taskManager.addTask(task1);
        mockMvc.perform(delete("/tasks/{id}", task1.getId()))
                .andExpect(status().isOk());
        assertEquals(0, taskManager.getAllTasks().size());
    }
}