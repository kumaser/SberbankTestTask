package com.sberbank.model.database;

public enum DBColumnType {

    INTEGER("INTEGER"),
    TEXT ("TEXT");

    private String value;

    DBColumnType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
