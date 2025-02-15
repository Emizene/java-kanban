package model;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    public void testEqualsTrue_whenSubtaskIdIsTheSame() {
        Epic epic1 = new Epic("TEST EPIC №1", "DESCRIPTION №1", Status.NEW);
        Epic epic2 = new Epic("TEST EPIC №2", "DESCRIPTION №2", Status.NEW);
        Subtask subtask1 = new Subtask("TEST SUBTASK №1 TEST EPIC №1", "DESCRIPTION №1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("TEST SUBTASK №2 TEST EPIC №2 ", "DESCRIPTION №2", Status.NEW, epic2.getId());
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask1.getId(), subtask2.getId(), "ID не равны");
        assertEquals(subtask1, subtask2, "Экземпляра классы не равны");
    }

}