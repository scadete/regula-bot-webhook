package com.github.scadete.regula.ai;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ChatbotResponse {

    private String action;
    private Double confidence;
    private String speech;
    private String message;
    private String sessionId;
    private List<ChatbotContext> contexts;

    public ChatbotResponse() {
    }

    public ChatbotResponse(Double confidence, String speech, String message, String sessionId) {
        this.confidence = confidence;
        this.speech = speech;
        this.message = message;
        this.sessionId = sessionId;
    }

    private Map<String, JsonElement> data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, JsonElement> getData() {
        return data;
    }

    public void setData(Map<String, JsonElement> data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatbotContext> getContexts() {
        return contexts;
    }

    public void setContexts(List<ChatbotContext> contexts) {
        this.contexts = contexts;
    }
}
