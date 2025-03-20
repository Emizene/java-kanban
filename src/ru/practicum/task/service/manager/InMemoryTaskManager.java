package ru.practicum.task.service.manager;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.history.HistoryManager;

import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        if (isIntersection(task)) {
            return;
        }
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (isIntersection(epic)) {
            return;
        }
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isIntersection(subtask)) {
            return;
        }
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
            prioritizedTasks.removeAll(tasks.values());
        }
    }

    @Override
    public void clearAllEpics() {
        if (!epics.isEmpty()) {
            for (int id : epics.keySet()) {
                historyManager.remove(id);
            }
            epics.clear();
            prioritizedTasks.removeAll(epics.values());
            subtasks.clear();
            prioritizedTasks.removeAll(subtasks.values());
        }
    }

    @Override
    public void clearAllSubtasks() {
        if (!subtasks.isEmpty()) {
            for (int id : subtasks.keySet()) {
                historyManager.remove(id);
            }
            subtasks.clear();
            prioritizedTasks.removeAll(subtasks.values());
        }
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        prioritizedTasks.remove(tasks.get(taskId));
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.getSubtasks().stream()
                    .map(Subtask::getId)
                    .forEach(subtasks::remove);
            epics.remove(epicId);
            prioritizedTasks.remove(epic);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Epic epic = epics.get(subtaskId);
        epic.getSubtasks().remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        prioritizedTasks.remove(subtasks.get(subtaskId));
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
    public Set<Task> getPrioritizedTasks() {
        List<Task> taskList = tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();
        prioritizedTasks.addAll(taskList);
        List<Epic> epicList = epics.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();
        prioritizedTasks.addAll(epicList);
        List<Subtask> subtaskList = subtasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();
        prioritizedTasks.addAll(subtaskList);
        return prioritizedTasks;
    }

    public boolean isIntersection(Task task) {
        return getPrioritizedTasks()
                .stream()
                .anyMatch(prioritizedTask ->
                        (task.getStartTime().isAfter(prioritizedTask.getStartTime()) ||
                                task.getStartTime().isEqual(prioritizedTask.getStartTime())) &&
                                task.getStartTime().isBefore(prioritizedTask.getEndTime()) ||
                                task.getEndTime().isEqual(prioritizedTask.getEndTime())
                );
    }

    @Override
    public void printAllTasks() {
        System.out.println("Задачи, Эпики, подзадачи:");
        Stream.concat(
                        Stream.concat(
                                getAllTasks().stream(),
                                getAllEpics().stream()
                        ),
                        getAllSubtasks().stream()
                )
                .forEach(System.out::println);

        System.out.println("История:");
        if (getHistory().isEmpty()) {
            System.out.println("История пуста");
        } else {
            getHistory()
                    .forEach(System.out::println);
        }
    }
}



