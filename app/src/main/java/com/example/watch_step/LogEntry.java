package com.example.watch_step;

public class LogEntry {
    private String event;
    private String details;
    private long timestamp;

    public LogEntry(String event, String details, long timestamp) {
        this.event = event;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public String getDetails() {
        return details;
    }

    public long getTimestamp() {
        return timestamp;
    }
}