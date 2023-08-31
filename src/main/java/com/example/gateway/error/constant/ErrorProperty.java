package com.example.gateway.error.constant;

public enum ErrorProperty {

    PATH,
    ERROR,
    STATUS,
    MESSAGE,
    TIMESTAMP,
    REQUESTID("requestId");

    private final String value;
    private ErrorProperty(String value) { this.value = value; }
    private ErrorProperty() { this.value = name().toLowerCase(); }

    public String value() { return value; }
}
