package ru.practicum.task.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest{

    @Test
    public void testEqualsTrue_whenSubtaskIdIsTheSame() {
        Epic epic1 = new Epic(2,"EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 17, 17, 0));
        Subtask subtask2 = new Subtask(3,"SUBTASK №2 EPIC №1 ", "DESCRIPTION №2",
                Status.NEW, Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 19, 20, 0), epic1.getId());
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask1.getId(), subtask2.getId(), "ID не равны");
        assertEquals(subtask1, subtask2, "Экземпляра классы не равны");
    }

}