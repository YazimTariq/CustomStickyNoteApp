package com.desktopnotes.model;

import java.util.UUID;

public class Note {
    private String id;
    private String title;
    private String content;
    private String backgroundColor;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.backgroundColor = "FFFF00";
    }
}
