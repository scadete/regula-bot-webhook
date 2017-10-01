package com.github.scadete.regula.ai;


import org.springframework.stereotype.Component;

public interface ChatbotService {
    public ChatbotResponse textMessage(ChatbotRequest request);
    public ChatbotResponse voiceMessage(ChatbotRequest request);
    public ChatbotResponse attachment(ChatbotRequest request);
}
