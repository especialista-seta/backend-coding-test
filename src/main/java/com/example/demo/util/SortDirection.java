package com.example.demo.util;

public enum SortDirection {
    ASC,
    DESC;

    public String getSortDirection() {
        return this.name().toLowerCase();
    }
}
