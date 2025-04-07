package ru.practicum.task.service.manager;

import org.springframework.stereotype.Component;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.history.HistoryManager;

import java.util.*;

@Component
public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private Integer idCounter = 0;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistoryManager();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer generateId() {
        return ++idCounter;
    }

    @Override
    public void addTask(Task task) {
        if (isIntersection(task)) {
            return;
        }
        if (task.getId() == null) {
            Integer id = generateId();
            task.setId(id);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null || epics.containsKey(epic.getId())) {
            return;
        }
        if (isIntersection(epic)) {
            return;
        }
        if (epic.getId() == null) {
            Integer id = generateId();
            epic.setId(id);
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask == null || !epics.containsKey(subtask.getEpicId())) {
            return;
        }
        if (isIntersection(subtask)) {
            return;
        }
        if (subtask.getId() == null) {
            Integer id = generateId();
            subtask.setId(id);
        }
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        epic.updateStatus();
    }

    public Optional<Task> getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addInHistory(task);
            return Optional.of(tasks.get(id));
        }
        return Optional.empty();
    }

    public Optional<Epic> getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addInHistory(epic);
            return Optional.of(epics.get(id));
        }
        return Optional.empty();
    }

    public Optional<Subtask> getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.addInHistory(subtask);
            return Optional.of(subtasks.get(id));
        }
        return Optional.empty();
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
            for (Integer id : subtasks.keySet()) {
                historyManager.remove(id);
            }
            subtasks.clear();
        }
    }

    @Override
    public void clearAll() {
        clearAllTasks();
        clearAllEpics();
        clearAllEpics();
    }

    @Override
    public void deleteTask(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.getSubtasks().stream()
                    .map(Subtask::getId)
                    .forEach(subtasks::remove);
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(Integer subtaskId) {
        Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
        epic.getSubtasks().remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        historyManager.remove(subtaskId);
        epic.updateStatus();
    }

    @Override
    public void updateTask(Task task, Task newTask) {
        tasks.put(task.getId(), newTask);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            deleteTask(task.getId());
            historyManager.remove(task.getId());
                tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic, Epic newEpic) {
        epics.put(epic.getId(), newEpic);
        newEpic.updateStatus();
    }

    @Override
    public void updateEpic(Epic epic) {
        List<Subtask> oldSubtasks = epics.get(epic.getId()).getSubtasks();
        for (Subtask oldSubtask : oldSubtasks) {
            subtasks.remove(oldSubtask.getId());
        }
        if (epics.containsKey(epic.getId())) {
            deleteEpic(epic.getId());
            historyManager.remove(epic.getId());
                epics.put(epic.getId(), epic);
                for (Subtask subtask : epic.getSubtasks()) {
                    subtasks.put(subtask.getId(), subtask);
                }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, Subtask newSubtask) {
        newSubtask.setId(subtask.getId());
        subtasks.remove(subtask.getId());
        subtasks.put(newSubtask.getId(), newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        epic.getSubtasks().remove(subtask);
        epic.addSubtask(newSubtask);
        epic.updateStatus();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            epics.get(oldSubtask.getEpicId()).getSubtasks().remove(oldSubtask);
            deleteSubtask(subtask.getId());
            historyManager.remove(subtask.getId());
                epics.get(subtask.getEpicId()).getSubtasks().add(subtask);
                subtasks.put(subtask.getId(), subtask);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(Integer epicId) {
        Optional<Epic> epic = getEpicById(epicId);
        if (epic.isEmpty()) {
            return new ArrayList<>();
        }
        List<Subtask> subtasks = epic.get().getSubtasks();
        if (subtasks == null || subtasks.isEmpty()) {
            return new ArrayList<>();
        }
        List<Subtask> epicSubtask = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask != null) {
                epicSubtask.add(subtask);
            }
        }
        return epicSubtask;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        prioritizedTasks.clear();
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

    private boolean isIntersection(Task task) {
        return getPrioritizedTasks()
                .stream()
                .filter(Objects::nonNull)
                .filter(prioritizedTask -> prioritizedTask.getStartTime() != null
                        && prioritizedTask.getEndTime() != null)
                .anyMatch(prioritizedTask ->
                        (task.getStartTime().isBefore(prioritizedTask.getEndTime()) ||
                                task.getEndTime().isEqual(prioritizedTask.getEndTime()) &&
                                        task.getStartTime().isAfter(prioritizedTask.getStartTime()) ||
                                task.getStartTime().isEqual(prioritizedTask.getStartTime()))

                );
    }

}



