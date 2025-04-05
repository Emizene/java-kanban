package ru.practicum.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.task.BaseTest;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SubtaskControllerTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        taskManager.clearAll();
        historyManager.clearAllHistory();
    }

    @Test
    public void testSuccessGetSubtasks() throws Exception {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        mockMvc.perform(get("/subtasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"id": 3,
                        		"name": "SUBTASK №1 EPIC №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": "PT5M",
                        		"startTime": "2025-03-17T17:00:00",
                        		"epicId": 2,
                        		"endTime": "2025-03-17T17:05:00"
                        	}
                        ]
                        """));
    }

    @Test
    public void testSuccessGetSubtaskById() throws Exception {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        mockMvc.perform(get("/subtasks/{id}", subtask1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        	{
                        		"id": 3,
                        		"name": "SUBTASK №1 EPIC №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": "PT5M",
                        		"startTime": "2025-03-17T17:00:00",
                        		"epicId": 2,
                        		"endTime": "2025-03-17T17:05:00"
                        	}
                        """));
    }

    @Test
    public void testSuccessUpdateSubtask() throws Exception {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        mockMvc.perform(post("/subtasks")
                        .contentType(APPLICATION_JSON)
                        .content("""
                         	{
                         		"id": 3,
                         		"name": "UPDATE SUBTASK №1 EPIC №1",
                         		"description": "UPDATE DESCRIPTION №1",
                         		"status": "NEW",
                         		"duration": "PT5M",
                         		"startTime": "2025-03-17T17:00:00",
                         		"epicId": 2,
                         		"endTime": "2025-03-17T17:05:00"
                         	}
                        """))
                .andExpect(status().isOk());
        Subtask subtaskAfterUpdate = taskManager.getSubtaskById(subtask1.getId()).get();
        assertEquals("UPDATE SUBTASK №1 EPIC №1", subtaskAfterUpdate.getName());
        assertEquals("UPDATE DESCRIPTION №1", subtaskAfterUpdate.getDescription());
        assertEquals(subtask1.getDuration(), subtaskAfterUpdate.getDuration());
    }

    @Test
        public void testSuccessCreateSubtask() throws Exception {
            Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                    LocalDateTime.of(2025, 3, 17, 16, 30));
            taskManager.addEpic(epic1);
        assertEquals(0, taskManager.getAllSubtasks().size());
            mockMvc.perform(post("/subtasks")
                            .contentType(APPLICATION_JSON)
                            .content("""
                             	{
                             		"name": "NEW SUBTASK №1 EPIC №1",
                             		"description": "NEW DESCRIPTION №1",
                             		"status": "NEW",
                             		"duration": "PT5M",
                             		"startTime": "2025-03-17T17:00:00",
                             		"epicId": 2
                             	}
                            """))
                    .andExpect(status().isCreated());
            assertEquals(1, taskManager.getAllSubtasks().size());
        }

    @Test
    public void testSuccessDeleteSubtask() throws Exception {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        mockMvc.perform(delete("/subtasks/{id}", subtask1.getId()))
                .andExpect(status().isOk());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

}