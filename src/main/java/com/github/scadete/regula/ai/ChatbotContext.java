package com.github.scadete.regula.ai;

import com.google.gson.JsonElement;

import java.util.Map;

public class ChatbotContext {

    private String name;
    private Map<String, String> data;
    private int lifespan;

    public ChatbotContext(String name, int lifespan) {
        this.name = name;
        this.lifespan = lifespan;
    }

    public ChatbotContext(String name, Map<String, String> data) {
        this.name = name;
        this.data = data;
        this.lifespan = 7;
    }

    public ChatbotContext(String name) {
        this.name = name;
        this.lifespan = 7;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }
}
