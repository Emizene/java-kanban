package ru.practicum.task.service.manager;

import ru.practicum.task.model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final int ID_POSITION = 0;
    private static final int NAME_POSITION = 1;
    private static final int DESCRIPTION_POSITION = 2;
    private static final int STATUS_POSITION = 3;
    private static final int EPIC_ID_POSITION = 4;

    public FileBackedTaskManager(File file) throws ManagerSaveException {
        this.file = file;
        load(file);
    }

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epicId\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toFileString());
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(epic.toFileString());
            }
            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(subtask.toFileString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private Task fromString(String value) throws ManagerSaveException {
        String[] data = value.split(",");
        try {
            Type taskType = Type.valueOf(data[1]);
            switch (taskType) {
                case TASK:
                    Task task = new Task(Integer.parseInt(data[ID_POSITION]), data[NAME_POSITION],
                            data[DESCRIPTION_POSITION], Status.valueOf(data[STATUS_POSITION]));
                    tasks.put(task.getId(), task);
                    return task;
                case EPIC:
                    Epic epic = new Epic(Integer.parseInt(data[ID_POSITION]), data[NAME_POSITION],
                            data[DESCRIPTION_POSITION], Status.valueOf(data[STATUS_POSITION]));
                    epics.put(epic.getId(), epic);
                    return epic;
                case SUBTASK:
                    Subtask subtask = new Subtask(Integer.parseInt(data[ID_POSITION]), data[NAME_POSITION],
                            data[DESCRIPTION_POSITION], Status.valueOf(data[STATUS_POSITION]),
                            Integer.parseInt(data[EPIC_ID_POSITION]));
                    subtasks.put(subtask.getId(), subtask);
                    return subtask;
                default:
                    throw new ManagerSaveException("Некорректный тип задачи: " + taskType);
            }
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при создании задачи из строки: " + e.getMessage());
        }
    }

    private void load(File file) throws ManagerSaveException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    return;
                }
                if(line.equals("id,type,name,status,description,epicId")) {
                    continue;
                }
                Task task = fromString(line);
                if (task instanceof Subtask) {
                    subtasks.put(task.getId(), (Subtask) task);
                } else if (task instanceof Epic) {
                    epics.put(task.getId(), (Epic) task);
                } else {
                    tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + file.getPath());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        return new FileBackedTaskManager(file);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task, Task newTask) {
        super.updateTask(task, newTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic, Epic newEpic) {
        super.updateEpic(epic, newEpic);
        save();
    }

    @Override
    public void updateSubtaskStatus(Subtask subtask, Subtask newSubtask) {
        super.updateSubtaskStatus(subtask, newSubtask);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

}
