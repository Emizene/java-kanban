package ru.practicum.task.service.manager;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistoryManager();
    }

    private int idCounter = 0;

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int generateId() {
        return ++idCounter;
    }

    @Override
    public void addTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int id = generateId();
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        epic.updateStatus();
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addInHistory(task);
        }
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addInHistory(epic);
        }
        return epic;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addInHistory(subtask);
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task, Task newTask) {
        tasks.put(task.getId(), newTask);
    }

    @Override
    public void updateEpic(Epic epic, Epic newEpic) {
        epics.put(epic.getId(), newEpic);
        newEpic.updateStatus();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearAllTasks() {
        if (!tasks.isEmpty()) {
            for (int id : tasks.keySet()) {
                historyManager.remove(id);
            }
            tasks.clear();
        }
    }

    @Override
    public void clearAllEpics() {
        if (!epics.isEmpty()) {
            for (int id : epics.keySet()) {
                historyManager.remove(id);
            }
            epics.clear();
            subtasks.clear();
        }
    }

    @Override
    public void clearAllSubtasks() {
        if (!subtasks.isEmpty()) {
            for (int id : subtasks.keySet()) {
                historyManager.remove(id);
            }
            subtasks.clear();
        }
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Epic epic = epics.get(subtaskId);
        epic.getSubtasks().remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        historyManager.remove(subtaskId);
        epic.updateStatus();
    }

    @Override
    public void updateSubtaskStatus(Subtask subtask, Subtask newSubtask) {
        newSubtask.setId(subtask.getId());
        subtasks.remove(subtask.getId());
        subtasks.put(newSubtask.getId(), newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.getSubtasks().remove(subtask);
        epic.addSubtask(newSubtask);
        epic.updateStatus();
    }

    @Override
    public void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        if (getHistory().isEmpty()) {
            System.out.println("История пуста");
        }
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}



