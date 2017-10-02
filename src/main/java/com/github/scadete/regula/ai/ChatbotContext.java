package com.github.scadete.regula.ai;

import com.google.gson.JsonElement;

import java.util.Map;

public class ChatbotContext {

    private String name;
    private Map<String, JsonElement> data;

    public ChatbotContext(String name, Map<String, JsonElement> data) {
        this.name = name;
        this.data = data;
    }

    public ChatbotContext(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, JsonElement> getData() {
        return data;
    }

    public void setData(Map<String, JsonElement> data) {
        this.data = data;
    }
}
