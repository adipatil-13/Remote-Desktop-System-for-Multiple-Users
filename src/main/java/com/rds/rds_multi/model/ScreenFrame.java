package com.rds.rds_multi.model;

public class ScreenFrame {
    private String type; // e.g., "image", "cursor", "command"
    private String data; // base64 image, or text command

    public ScreenFrame() {}

    public ScreenFrame(String type, String data) {
        this.type = type;
        this.data = data;
    }

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
