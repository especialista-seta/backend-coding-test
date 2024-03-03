package com.example.demo.util;

public enum SortField {
    PRIORITY,
    CREATED;

    public String getSortField() {
        return this.name().toLowerCase();
    }
}
