package ru.practicum.task.controller;

import org.junit.jupiter.api.Test;
import ru.practicum.task.BaseTest;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HistoryControllerTest extends BaseTest {

    @Test
    public void testSuccessGetHistory() throws Exception {
        Epic epic1 = new Epic(1,"EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addEpic(epic1);
        historyManager.addInHistory(epic1);
        mockMvc.perform(get("/history"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"id": 1,
                        		"name": "EPIC №1",
                        		"description": "DESCRIPTION №1",
                        		"status": "NEW",
                        		"duration": null,
                        		"startTime": "2025-03-17T16:30:00",
                        		"endTime": "2025-03-17T16:30:00"
                        	}
                        ]
                        """));
    }

}