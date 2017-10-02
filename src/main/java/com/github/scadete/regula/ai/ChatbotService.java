package com.github.scadete.regula.ai;


import org.springframework.stereotype.Component;

public interface ChatbotService {
    public ChatbotResponse converse(ChatbotRequest request);
}
