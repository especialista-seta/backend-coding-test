package com.example.demo.integration;

import com.example.demo.entity.TaskEntity;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", "Guillermo");
    }

    @Test
    public void whenNoCredential_thenNoAccess() {
        ResponseEntity<TaskEntity> response = restTemplate.getForEntity("/task/1", TaskEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void whenCredential_thenAccess() {
        ResponseEntity<TaskEntity> response = restTemplate
                .exchange("/task/1", HttpMethod.GET, new HttpEntity<>(headers), TaskEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void whenGetTask_thenReturnTasks() {
        ResponseEntity<TaskEntity> response = restTemplate
                .exchange("/task/1", HttpMethod.GET, new HttpEntity<>(headers), TaskEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getTaskId(), is(1));
    }

    @Test
    public void whenGetTaskAndNoTask_thenReturnNotFound() {
        ResponseEntity<TaskEntity> response = restTemplate
                .exchange("/task/3", HttpMethod.GET, new HttpEntity<>(headers), TaskEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void whenAddTask_thenReturnTasks() {
        JSONObject task = new JSONObject();
        task.put("description", "Task 3");
        task.put("completed", false);
        task.put("priority", "LOW");

        ResponseEntity<TaskEntity> response = restTemplate
                .exchange("/task", HttpMethod.POST, new HttpEntity<>(task.toJSONString(), headers), TaskEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getTaskId(), is(3));

        ResponseEntity<TaskEntity> response2 = restTemplate
                .exchange("/task/3", HttpMethod.GET, new HttpEntity<>(headers), TaskEntity.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response2.getBody()).getTaskId(), is(3));

    }
}
