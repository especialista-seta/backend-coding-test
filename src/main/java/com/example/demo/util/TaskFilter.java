package com.example.demo.util;

public enum TaskFilter {
    PRIORITY,
    COMPLETED;

    public String getTaskFilter() {
        return this.name().toLowerCase();
    }
}
