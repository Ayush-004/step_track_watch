package com.example.watch_step;

public class Note {
    private int id;
    private String noteText;
    private long timestamp;
    private long notificationId;

    public Note(int id, String noteText, long timestamp, long notificationId){
        this.id = id;
        this.noteText = noteText;
        this.timestamp = timestamp;
        this.notificationId = notificationId;
    }

    public int getId() {
        return id;
    }

    public String getNoteText() {
        return noteText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getNotificationId() {
        return notificationId;
    }
}