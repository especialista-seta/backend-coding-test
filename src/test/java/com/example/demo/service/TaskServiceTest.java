package com.example.demo.service;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskPriority;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @BeforeEach
    void initUseCase() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void whenGetSingleTask_thenReturnSingleTask() {
        TaskEntity task = new TaskEntity();
        task.setTaskId(1);
        task.setDescription("task 1");
        task.setCompleted(false);
        task.setPriority(TaskPriority.LOW);

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        Optional<TaskEntity> result = taskService.getTaskById(1);

        assert result.isPresent();
        assert result.get().getTaskId() == 1;
        assert result.get().getDescription().equals("task 1");
        assert !result.get().isCompleted();
        assert result.get().getPriority().equals(TaskPriority.LOW);
    }

    @Test
    void whenGetSingleTask_thenReturnEmpty() {
        when(taskRepository.findById(1))
                .thenReturn(Optional.empty());

        Optional<TaskEntity> result = taskService.getTaskById(1);

        assert result.isEmpty();
    }

    @Test
    void whenCreateTask_thenReturnTask() {
        TaskEntity task = new TaskEntity();
        task.setTaskId(1);
        task.setDescription("task 1");
        task.setCompleted(false);
        task.setPriority(TaskPriority.LOW);

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskEntity result = taskService.createTask(task);

        assert result.getTaskId() == 1;
        assert result.getDescription().equals("task 1");
        assert !result.isCompleted();
        assert result.getPriority().equals(TaskPriority.LOW);
    }

    @Test
    void whenUpdateTask_thenReturnTask() {
        TaskEntity task = new TaskEntity();
        task.setTaskId(1);
        task.setDescription("task 1");
        task.setCompleted(false);
        task.setPriority(TaskPriority.LOW);

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));
        when(taskRepository.save(task))
                .thenReturn(task);

        Optional<TaskEntity> result = taskService.updateTask(1, task);

        assert result.isPresent();
        assert result.get().getTaskId() == 1;
        assert result.get().getDescription().equals("task 1");
        assert !result.get().isCompleted();
        assert result.get().getPriority().equals(TaskPriority.LOW);
    }

    @Test
    void whenDeleteTask_thenReturnTask() {
        TaskEntity task = new TaskEntity();
        task.setTaskId(1);
        task.setDescription("task 1");
        task.setCompleted(false);
        task.setPriority(TaskPriority.LOW);

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        Optional<TaskEntity> result = taskService.deleteTask(1);

        assert result.isPresent();
        assert result.get().getTaskId() == 1;
        assert result.get().getDescription().equals("task 1");
        assert !result.get().isCompleted();
        assert result.get().getPriority().equals(TaskPriority.LOW);
    }
}
