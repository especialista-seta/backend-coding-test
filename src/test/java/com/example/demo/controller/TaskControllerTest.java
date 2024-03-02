package com.example.demo.controller;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskPriority;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @Autowired
    private TestRestTemplate server;

    @MockBean
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<TaskEntity> taskCaptor;

    @Captor
    private ArgumentCaptor<HttpEntity> headersCaptor;


    private List<TaskEntity> taskEntities;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void whenGetAllTasks_thenReturnAllTasks() {
        when(taskService.getAllTasks()).thenReturn(List.of(
                new TaskEntity(1, "task 1", false, TaskPriority.LOW),
                new TaskEntity(2, "task 2", true, TaskPriority.HIGH)
        ));

        ResponseEntity<List> response = server.getForEntity("/task", List.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).size(), is(2));
    }

    @Test
    public void whenGetAllTasksAndNoTask_thenReturnEmpty() {
        when(taskService.getAllTasks()).thenReturn(new ArrayList<>());

        ResponseEntity<List> response = server.getForEntity("/task", List.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).size(), is(0));
    }

    @Test
    public void whenGetTaskById_thenReturnTask() {
        TaskEntity task = new TaskEntity(1, "task 1", false, TaskPriority.LOW);
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));

        ResponseEntity<TaskEntity> response = server.getForEntity("/task/1", TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(1));
        assertThat(response.getBody().getDescription(), is("task 1"));
        assertThat(response.getBody().isCompleted(), is(false));
        assertThat(response.getBody().getPriority(), is(TaskPriority.LOW));
    }

    @Test
    public void whenGetTaskByIdAndNoTaskExist_thenReturn404() {
        when(taskService.getTaskById(1)).thenReturn(Optional.empty());

        ResponseEntity<TaskEntity> response = server.getForEntity("/task/1", TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void whenValidInput_thenCreateTask() throws Exception {
        TaskEntity task = new TaskEntity();
        task.setDescription("task description");
        task.setCompleted(false);
        task.setPriority(TaskPriority.LOW);

        when(taskService.createTask(any())).thenReturn(task);

        ResponseEntity<TaskEntity> response = server.postForEntity("/task", task, TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        verify(taskService, times(1)).createTask(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getDescription(), is("task description"));
        assertThat(taskCaptor.getValue().getPriority(), is(TaskPriority.LOW));
        assertThat(taskCaptor.getValue().isCompleted(), is(false));
    }

    @Test
    public void whenValidInput_thenUpdateTask() {
        TaskEntity task = new TaskEntity(1, "task 1", false, TaskPriority.LOW);
        when(taskService.updateTask(eq(1), any())).thenReturn(Optional.of(task));

        ResponseEntity<TaskEntity> response = server.exchange("/task/1", HttpMethod.PUT, new HttpEntity<>(task), TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        verify(taskService, times(1)).updateTask(eq(1), taskCaptor.capture());
        assertThat(taskCaptor.getValue().getDescription(), is("task 1"));
        assertThat(taskCaptor.getValue().getPriority(), is(TaskPriority.LOW));
        assertThat(taskCaptor.getValue().isCompleted(), is(false));
    }

    @Test
    public void whenNotValidInput_thenNoUpdateTask() {
        when(taskService.updateTask(eq(1), any())).thenReturn(Optional.empty());
        TaskEntity task = new TaskEntity(1, "new value", false, TaskPriority.LOW);

        ResponseEntity<TaskEntity> response = server.exchange("/task/1", HttpMethod.PUT, new HttpEntity<>(task), TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        verify(taskService, times(1)).updateTask(eq(1), any());
    }

    @Test
    public void whenDeleteTask_thenDeleteTask() {
        TaskEntity task = new TaskEntity(1, "task 1", false, TaskPriority.LOW);
        when(taskService.deleteTask(1)).thenReturn(Optional.of(task));

        ResponseEntity<TaskEntity> response = server.exchange("/task/1", HttpMethod.DELETE, null, TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        verify(taskService, times(1)).deleteTask(1);
    }

    @Test
    public void whenDeleteTaskAndNoTask_thenNotDeleteTask() {
        when(taskService.deleteTask(1)).thenReturn(Optional.empty());

        ResponseEntity<TaskEntity> response = server.exchange("/task/1", HttpMethod.DELETE, null, TaskEntity.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        verify(taskService, times(1)).deleteTask(1);
    }

}
