package com.example.myapplication3;

import java.io.Serializable;

public class AnkaraEvent implements Serializable {
    private String name;
    private String content;

    // Constructor
    public AnkaraEvent(String name, String content) {
        this.name = name;
        this.content = content;
    }

    // Getter ve Setter metotları
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
