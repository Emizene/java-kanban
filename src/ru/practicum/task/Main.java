package ru.practicum.task;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.model.Status;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW);
        Task task3 = new Task("TASK №3", "DESCRIPTION №3", Status.NEW);
        Task task4 = new Task("TASK №4", "DESCRIPTION №4", Status.NEW);
        Task task5 = new Task("TASK №5", "DESCRIPTION №5", Status.NEW);
        Task task6 = new Task("TASK №6", "DESCRIPTION №6", Status.NEW);
        Task task7 = new Task("TASK №7", "DESCRIPTION №7", Status.NEW);
        Task task8 = new Task("TASK №8", "DESCRIPTION №8", Status.NEW);
        Task task9 = new Task("TASK №9", "DESCRIPTION №9", Status.NEW);
        Task task10 = new Task("TASK №10", "DESCRIPTION №10", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addTask(task5);
        manager.addTask(task6);
        manager.addTask(task7);
        manager.addTask(task8);
        manager.addTask(task9);
        manager.addTask(task10);

        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW);
        Epic epic2 = new Epic("EPIC №2", "DESCRIPTION №2", Status.NEW);
        Epic epic3 = new Epic("EPIC №3", "DESCRIPTION №3", Status.DONE);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("SUBTASK №2 EPIC №2 ", "DESCRIPTION №2", Status.NEW, epic2.getId());
        Subtask subtask3 = new Subtask("SUBTASK №3 EPIC №3 ", "DESCRIPTION №3", Status.NEW, epic3.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Subtask newSubtask1 = new Subtask("NEW SUBTASK №1", "DESCRIPTION №1", Status.DONE, epic1.getId());
        Subtask newSubtask2 = new Subtask("NEW SUBTASK №2", "DESCRIPTION №2", Status.DONE, epic2.getId());
        Epic newEpic3 = new Epic("NEW EPIC №3", "DESCRIPTION №3", Status.NEW);


        manager.printAllTasks(manager);
        System.out.println();
        System.out.println("----------------");
        System.out.println();

        manager.updateSubtaskStatus(subtask1, newSubtask1);
        manager.updateSubtaskStatus(subtask2, newSubtask2);
        manager.updateEpic(epic3, newEpic3);

        manager.printAllTasks(manager);

        System.out.println();
        System.out.println("----------------");
        System.out.println();

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(7);
        manager.getTaskById(8);
        manager.getTaskById(9);
        manager.getTaskById(10);
        manager.getEpicById(11);
        manager.getEpicById(12);
        manager.getSubtaskById(14);
        manager.getSubtaskById(15);

        manager.printAllTasks(manager);

    }
}
