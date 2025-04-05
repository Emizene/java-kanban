package ru.practicum.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.task.service.history.InMemoryHistoryManager;
import ru.practicum.task.service.manager.InMemoryTaskManager;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected InMemoryTaskManager taskManager;

    @Autowired
    protected InMemoryHistoryManager historyManager;

    protected ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
}
