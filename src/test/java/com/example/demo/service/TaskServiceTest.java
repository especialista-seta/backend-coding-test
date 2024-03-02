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
    void whenCallingSayHello_thenReturnHello() {
        when(taskRepository.findById(1))
                .thenReturn(Optional.of(new TaskEntity(1, "task 1", false, TaskPriority.LOW)));

        Optional<TaskEntity> result = taskService.getTaskById(1);

        assert result.isPresent();
        assert result.get().getId() == 1;
        assert result.get().getDescription().equals("task 1");
        assert !result.get().isCompleted();
        assert result.get().getPriority().equals(TaskPriority.LOW);
    }
}
