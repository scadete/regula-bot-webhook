package com.github.scadete.regula.ai;

import ai.api.*;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    @Autowired
    private AIConfiguration aiConfig;

    public String getResponse(String message, String senderId) {
        logger.debug("ChatbotService sendMessage -  message: '{}', from: '{}'", message, senderId);
        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(senderId).build();
        AIDataService service = new AIDataService(aiConfig, context);
        try {
            AIResponse response = service.request(new AIRequest(message));
            return response.getResult().getFulfillment().getDisplayText();
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return "Desculpe, mas não estou conseguindo pensar...";
    }

}
