package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskPriority;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DemoApplication.class})
@ActiveProfiles("test")
@WebMvcTest
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TaskService taskService;

    private MockMvc server;

    @BeforeEach
    public void setup() throws Exception {
        server = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void whenGetAllTasks_thenReturnAllTasks() throws Exception {
        when(taskService.getAllTasks(any(), any())).thenReturn(List.of(
                buildTask(1, "task 1", false, TaskPriority.LOW),
                buildTask(2, "task 2", true, TaskPriority.HIGH)
        ));

        server.perform(get("/task", List.class))
                .andExpect(status().isOk())
                .andExpect(result -> jsonPath("$.size()", is(2)));

    }

    @Test
    public void whenGetAllTasksAndNoTask_thenReturnEmpty() throws Exception {
        when(taskService.getAllTasks(any(), any())).thenReturn(new ArrayList<>());

        server.perform(get("/task", List.class))
                .andExpect(status().isOk())
                .andExpect(result -> jsonPath("$.size()", is(0)));
    }


    @Test
    public void whenGetTaskById_thenReturnTask() throws Exception {
        TaskEntity task = buildTask(1, "task 1", false, TaskPriority.LOW);
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));

        server.perform(get("/task/1", TaskEntity.class))
                .andExpect(status().isOk())
                .andExpect(result -> jsonPath("$.id", is(1)))
                .andExpect(result -> jsonPath("$.description", is("task 1")))
                .andExpect(result -> jsonPath("$.completed", is(false)))
                .andExpect(result -> jsonPath("$.priority", is(TaskPriority.LOW.toString())));
    }

    @Test
    public void whenGetTaskByIdAndNoTaskExist_thenReturn404() throws Exception {
        when(taskService.getTaskById(1)).thenReturn(Optional.empty());

        server.perform(get("/task/1", TaskEntity.class))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenValidInput_thenCreateTask() throws Exception {
        TaskEntity task = buildTask(1, "task 1", false, TaskPriority.LOW);

        when(taskService.createTask(any())).thenReturn(task);

        server.perform(post("/task", TaskEntity.class)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task)))
                .andExpect(status().isOk())
                .andExpect(result -> jsonPath("$.id", is(1)))
                .andExpect(result -> jsonPath("$.description", is("task 1")))
                .andExpect(result -> jsonPath("$.completed", is(false)))
                .andExpect(result -> jsonPath("$.priority", is(TaskPriority.LOW.toString())));

        verify(taskService, times(1)).createTask(any());
    }

    @Test
    public void whenValidInput_thenUpdateTask() throws Exception {
        TaskEntity task = buildTask(1, "task 1", false, TaskPriority.LOW);
        when(taskService.updateTask(eq(1), any())).thenReturn(Optional.of(task));

        server.perform(put("/task/1", TaskEntity.class)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task)))
                .andExpect(status().isOk())
                .andExpect(result -> jsonPath("$.id", is(1)))
                .andExpect(result -> jsonPath("$.description", is("task 1")))
                .andExpect(result -> jsonPath("$.completed", is(false)))
                .andExpect(result -> jsonPath("$.priority", is(TaskPriority.LOW.toString())));

        verify(taskService, times(1)).updateTask(eq(1), any());
    }

    @Test
    public void whenNotValidInput_thenNoUpdateTask() throws Exception {
        when(taskService.updateTask(eq(1), any())).thenReturn(Optional.empty());
        TaskEntity task = buildTask(1, "new value", false, TaskPriority.LOW);

        server.perform(put("/task/1", TaskEntity.class)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(task)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(1), any());
    }

    @Test
    public void whenDeleteTask_thenDeleteTask() throws Exception {
        TaskEntity task = buildTask(1, "task 1", false, TaskPriority.LOW);
        when(taskService.deleteTask(1)).thenReturn(Optional.of(task));

        server.perform(delete("/task/1", TaskEntity.class))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(1);
    }

    @Test
    public void whenDeleteTaskAndNoTask_thenNotDeleteTask() throws Exception {
        when(taskService.deleteTask(1)).thenReturn(Optional.empty());

        server.perform(delete("/task/1", TaskEntity.class))
                .andExpect(status().isNotFound());
        verify(taskService, times(1)).deleteTask(1);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TaskEntity buildTask(int id, String description, boolean completed, TaskPriority priority) {
        TaskEntity task = new TaskEntity();
        task.setTaskId(id);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setPriority(priority);
        return task;
    }
}