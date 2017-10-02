package com.github.scadete.regula.ai;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatbotRequest {
    private String message;
    private String event;
    private String sessionId;

    private InputStream voiceStream;

    List<ChatbotContext> contextList;

    public ChatbotRequest() {
    }

    public ChatbotRequest(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public ChatbotRequest(InputStream voiceStream, String sessionId) {
        this.voiceStream = voiceStream;
        this.sessionId = sessionId;
    }

    public ChatbotRequest(String message, String sessionId, List<ChatbotContext> contextList) {
        this.message = message;
        this.sessionId = sessionId;
        this.contextList = contextList;
    }

    public ChatbotRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public InputStream getVoiceStream() {
        return voiceStream;
    }

    public void setVoiceStream(InputStream voiceStream) {
        this.voiceStream = voiceStream;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatbotContext> getContextList() {
        return contextList;
    }

    public void setContextList(List<ChatbotContext> contextList) {
        this.contextList = contextList;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void addContext(ChatbotContext context) {
        if (this.contextList == null) {
            this.contextList = new ArrayList<>();
        }
        this.contextList.add(context);
    }
}
