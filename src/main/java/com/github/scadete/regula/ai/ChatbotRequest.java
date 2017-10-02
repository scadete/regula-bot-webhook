package com.github.scadete.regula.ai;

import java.io.InputStream;
import java.util.Map;

public class ChatbotRequest {
    private String message;
    private String sessionId;

    private InputStream voiceStream;

    private Map<String, String> parameters;

    public ChatbotRequest(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public ChatbotRequest(InputStream voiceStream, String sessionId) {
        this.voiceStream = voiceStream;
        this.sessionId = sessionId;
    }

    public ChatbotRequest(String message, String sessionId, Map<String, String> parameters) {
        this.message = message;
        this.sessionId = sessionId;
        this.parameters = parameters;
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
