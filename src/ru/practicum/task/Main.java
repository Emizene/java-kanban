package ru.practicum.task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW);
        Task task3 = new Task("TASK №3", "DESCRIPTION №3", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW);
        Epic epic2 = new Epic("EPIC №2", "DESCRIPTION №2", Status.NEW);
        Epic epic3 = new Epic("EPIC №3", "DESCRIPTION №3", Status.DONE);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("SUBTASK №2 EPIC №2 ", "DESCRIPTION №2", Status.NEW, epic2.getId());
        Subtask subtask3 = new Subtask("SUBTASK №3 EPIC №3 ", "DESCRIPTION №3", Status.NEW, epic3.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);


        Subtask newSubtask1 = new Subtask("NEW SUBTASK №1", "DESCRIPTION №1", Status.DONE, epic1.getId());
        Subtask newSubtask2 = new Subtask("NEW SUBTASK №2", "DESCRIPTION №2", Status.DONE, epic2.getId());
        Epic newEpic3 = new Epic("NEW EPIC №3", "DESCRIPTION №3", Status.NEW);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();
        System.out.println("----------------");
        System.out.println();

        taskManager.updateSubtaskStatus(subtask1, newSubtask1);
        taskManager.updateSubtaskStatus(subtask2, newSubtask2);
        taskManager.updateEpic(epic3, newEpic3);
        taskManager.deleteTask(2);
        taskManager.deleteEpic(5);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

    }
}
