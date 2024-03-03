package com.example.demo.controller;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskPriority;
import com.example.demo.service.TaskService;
import com.example.demo.util.SortDirection;
import com.example.demo.util.SortField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Task", description = "Task Operations")
@RestController
@RequestMapping(path = "/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Get all tasks",
            parameters = {
                    @Parameter(name = "priority", description = "", in = ParameterIn.QUERY, schema = @Schema(implementation = TaskPriority.class)),
                    @Parameter(name = "completed", description = "", in = ParameterIn.QUERY, schema = @Schema(implementation = Boolean.class)),
                    @Parameter(name = "sortDirection", description = "Sort tasks", schema = @Schema(implementation = SortDirection.class)),
                    @Parameter(name = "sortField", description = "Sort field", schema = @Schema(implementation = SortField.class))
            })
    @GetMapping
    public ResponseEntity<List<TaskEntity>> getAllTasks(
            @RequestParam(required = false) @Parameter(hidden = true) Map<String, String> filter,
            @RequestParam(required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(required = false, defaultValue = "PRIORITY") SortField sortField
    ) {

        return ResponseEntity
                .ok()
                .body(taskService.getAllTasks(filter, Sort.by(Sort.Direction.fromString(sortDirection.getSortDirection()), sortField.getSortField())));
    }

    @Operation(summary = "Get task by id", description = "Get task by id", parameters = {
            @Parameter(name = "id", description = "Task id", required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create task", description = "Create task")
    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity taskEntity) {
        return ResponseEntity.ok().body(taskService.createTask(taskEntity));
    }

    @Operation(summary = "Update task", description = "Update task", parameters = {
            @Parameter(name = "id", description = "Task id", required = true)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody TaskEntity taskEntity) {
        return taskService.updateTask(id, taskEntity)
                .map((task) -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete task", description = "Delete task", parameters = {
            @Parameter(name = "id", description = "Task id", required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        return taskService.deleteTask(id)
                .map((task) -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
