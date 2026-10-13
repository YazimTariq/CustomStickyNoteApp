package com.desktopnotes.model;

import java.util.UUID;

public class Note {
    private String id;
    private String title;
    private String content;
    private String backgroundColor;
    private int x,y,width,height;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Note(String title, int x, int y, int width, int height, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public Note(String id, String title, int x, int y, int width, int height, String content) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
