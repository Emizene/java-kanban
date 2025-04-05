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

class EpicControllerTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        taskManager.clearAll();
        historyManager.clearAllHistory();
    }

    @Test
    public void testSuccessGetEpics() throws Exception {
        Epic epic1 = new Epic(1, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addEpic(epic1);
        mockMvc.perform(get("/epics"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"id": 1,
                        		"name": "EPIC №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": "PT0S",
                        		"startTime": "2025-03-17T16:30:00",
                        		"endTime": null,
                        		"subtasks": []
                        	}
                        ]
                        """));
    }

    @Test
    public void testSuccessGetEpicById() throws Exception {
        Epic epic1 = new Epic(1, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addEpic(epic1);
        taskManager.getEpicById(epic1.getId());
        mockMvc.perform(get("/epics/{id}", epic1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        	{
                        		"id": 1,
                        		"name": "EPIC №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": "PT0S",
                        		"startTime": "2025-03-17T16:30:00",
                        		"endTime": null,
                        		"subtasks": []
                        	}
                        """));
    }

    @Test
    public void testSuccessGetEpicSubtasks() throws Exception {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.getEpicById(epic1.getId());
        mockMvc.perform(get("/epics/{id}/subtask", epic1.getId()))
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
    public void testSuccessUpdateEpic() throws Exception {
        Epic epic1 = new Epic(1, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addEpic(epic1);
        taskManager.getEpicById(epic1.getId());
        mockMvc.perform(post("/epics")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                		"id": 1,
                                		"name": "UPDATE EPIC №1",
                                		"description": "UPDATE DESCRIPTION №1",
                                		"status": "NEW",
                                		"duration": "PT0S",
                                		"startTime": "2025-03-17T16:30:00",
                                		"endTime": null,
                                		"subtasks": []
                                	}
                                """))
                .andExpect(status().isOk());
        Epic epicAfterUpdate = taskManager.getEpicById(epic1.getId()).get();
        assertEquals("UPDATE EPIC №1", epicAfterUpdate.getName());
        assertEquals("UPDATE DESCRIPTION №1", epicAfterUpdate.getDescription());
        assertEquals(epic1.getDuration(), epicAfterUpdate.getDuration());
    }

    @Test
    public void testSuccessCreateEpic() throws Exception {
        assertEquals(0, taskManager.getAllEpics().size());
        mockMvc.perform(post("/epics")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                		"name": "NEW EPIC №1",
                                		"description": "NEW DESCRIPTION №1",
                                		"status": "NEW",
                                		"duration": "PT0S",
                                		"startTime": "2025-03-17T16:30:00",
                                		"endTime": null,
                                		"subtasks": []
                                	}
                                """))
                .andExpect(status().isCreated());
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    public void testSuccessDeleteEpic() throws Exception {
        Epic epic1 = new Epic(1, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addEpic(epic1);
        mockMvc.perform(delete("/epics/{id}", epic1.getId()))
                .andExpect(status().isOk());
        assertEquals(0, taskManager.getAllEpics().size());
    }

}