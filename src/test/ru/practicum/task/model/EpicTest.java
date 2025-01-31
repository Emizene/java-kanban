package ru.practicum.task.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void testEqualsTrue_whenEpicIdIsTheSame() {
        Epic epic1 = new Epic("TEST EPIC №1", "DESCRIPTION №1", Status.NEW);
        Epic epic2 = new Epic("TEST EPIC №2", "DESCRIPTION №2", Status.NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "ID не равны");
        assertEquals(epic1, epic2, "Экземпляры класса не равны");
    }

    @Test
    public void testEqualsTrue_whenEpicCannotBecomeItIsOwnSubtask() {
        //Epic epic1 = new Epic("TEST EPIC №1", "DESCRIPTION №1", Status.NEW);
        //java: incompatible types: ru.practicum.task.model.Epic cannot be converted to ru.practicum.task.model.Subtask
        //В Epic нельзя добавить самого себя в качестве Subtask, так как метод addSubtask принимает только тип Subtask
        //epic1.addSubtask(epic1);
    }

}