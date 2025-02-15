package ru.practicum.task.service;

import ru.practicum.task.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> linkedMap = new HashMap<>();
    private Node head;
    private Node tail;


    private Node getNode(Task task) {
        return linkedMap.get(task.getId());
    }

    private Node getNode(int id) {
        return linkedMap.get(id);
    }

    @Override
    public void addInHistory(Task task) {
        if (task != null && !task.getName().isEmpty()) {
            if (getNode(task) != null) {
                removeNode(getNode(task));
            }
            addLast(task);
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getTask());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void remove(int id) {
        removeNode(getNode(id));
    }

    private void addLast(Task task) {
        Node newNode = new Node(null, task.getTaskCopy(), null);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
        linkedMap.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
        linkedMap.remove(node.getTask().getId());
    }

}


