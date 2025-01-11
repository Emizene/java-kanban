package ru.practicum.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int idCounter = 0;

    public int generateId() {
        return ++idCounter;
    }

    public void addTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addSubtask(Subtask subtask) {
        int id = generateId();
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        epic.updateStatus();
    }

    public void updateTask(Task task, Task newTask) {
        tasks.put(task.getId(), newTask);
    }

    public void updateEpic(Epic epic, Epic newEpic) {
        epics.put(epic.getId(), newEpic);
        newEpic.updateStatus();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();

    }

    public void clearAllEpics() {
        epics.clear();

    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epicId) {
        epics.remove(epicId);
    }

    public void deleteSubtask(int subtaskId) {
        Epic epic = epics.get(subtaskId);
        epic.getAllSubtasks().remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        epic.updateStatus();
    }

    public void updateSubtaskStatus(Subtask subtask, Subtask newSubtask) {
        newSubtask.setId(subtask.getId());
        subtasks.remove(subtask.getEpicId());
        subtasks.put(newSubtask.getId(), newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.getAllSubtasks().remove(subtask);
        epic.addSubtask(newSubtask);
        epic.updateStatus();
    }
}



