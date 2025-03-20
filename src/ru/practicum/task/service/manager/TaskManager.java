package ru.practicum.task.service.manager;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {

    int generateId();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(Task task, Task newTask);

    void updateEpic(Epic epic, Epic newEpic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubtask(int subtaskId);

    void updateSubtaskStatus(Subtask subtask, Subtask newSubtask);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();

    void printAllTasks();
}
