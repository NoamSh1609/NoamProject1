package com.noam.noamproject1.models;

public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp; // זמן השליחה

    // 🔹 בנאי ריק (נדרש עבור Firebase)
    public Message() {}

    // 🔹 בנאי מלא
    public Message(String senderId, String receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // 🔹 Getters ו- Setters
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
