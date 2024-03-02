package com.example.demo.service;

import com.example.demo.entity.TaskEntity;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<TaskEntity> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public TaskEntity createTask(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    public Optional<TaskEntity> updateTask(int id, TaskEntity taskEntity) {
        return taskRepository
                .findById(id)
                .flatMap(task -> Optional.of(taskRepository.save(taskEntity)));
    }

    public Optional<TaskEntity> deleteTask(int id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return Optional.of(task);
                })
                .orElse(Optional.empty());
    }
}
