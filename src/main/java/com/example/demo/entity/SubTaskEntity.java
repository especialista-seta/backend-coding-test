package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subTaskId;

    private String description;

    private boolean completed;

    private int taskId;

    private int priority;

    @ManyToOne
    @JoinColumn(name = "taskId", nullable = false, insertable = false, updatable = false)
    private TaskEntity taskEntity;
}
