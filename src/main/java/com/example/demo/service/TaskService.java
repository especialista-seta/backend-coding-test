package com.example.demo.service;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskPriority;
import com.example.demo.repository.TaskRepository;
import com.example.demo.util.TaskFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    static Specification<TaskEntity> priorityEquals(TaskPriority priority) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }

    static Specification<TaskEntity> completedEquals(boolean completed) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("completed"), completed);
    }

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<TaskEntity> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public List<TaskEntity> getAllTasks(Map<String, String> filter, Sort sort) {

        List<Specification<TaskEntity>> specs = filter.entrySet().stream()
                .map(entry -> {
                    if (entry.getKey().equals(TaskFilter.PRIORITY.getTaskFilter())) {
                        return priorityEquals(TaskPriority.valueOf(entry.getValue()));
                    } else {
                        return completedEquals(Boolean.parseBoolean(entry.getValue()));
                    }
                })
                .collect(Collectors.toList());

        return taskRepository.findAll(specs.stream().reduce(Specification::and).orElse(null),
                sort);
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
