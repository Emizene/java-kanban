package ru.practicum.task.service.manager;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskManager {

    Integer generateId();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    Optional<Task> getTaskById(Integer id);

    Optional<Epic> getEpicById(Integer id);

    Optional<Subtask> getSubtaskById(Integer id);

    void updateTask(Task task, Task newTask);

    void updateTask(Task task);

    void updateEpic(Epic epic, Epic newEpic);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask, Subtask newSubtask);

    void updateSubtask(Subtask subtask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    void clearAll();

    void deleteTask(Integer taskId);

    void deleteEpic(Integer epicId);

    void deleteSubtask(Integer subtaskId);

    List<Subtask> getEpicSubtasks(Integer id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}
