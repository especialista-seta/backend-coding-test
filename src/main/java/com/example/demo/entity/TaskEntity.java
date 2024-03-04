package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    private String description;

    private boolean completed;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Timestamp created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "taskId")
    private Set<SubTaskEntity> subTasks;

    public TaskEntity(String description, boolean completed, TaskPriority priority, Set<SubTaskEntity> subTasks) {
        this.description = description;
        this.completed = completed;
        this.priority = priority;
        this.subTasks = subTasks;
    }
}
