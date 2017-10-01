package com.github.scadete.regula.ai;

import ai.api.*;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    @Autowired
    private AIConfiguration aiConfig;

    public String textMessage(String message, String senderId) {
        logger.debug("ChatbotService textMessage -  message: '{}', from: '{}'", message, senderId);
        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(senderId).build();
        AIDataService service = new AIDataService(aiConfig, context);
        try {
            AIResponse response = service.request(new AIRequest(message));

            return response.getResult().getFulfillment().getSpeech();
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return "Desculpe, mas não estou conseguindo pensar...";
    }

    public String attachment(String senderId) {
        logger.debug("ChatbotService attachment - from: '{}'", senderId);
        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(senderId).build();
        AIDataService service = new AIDataService(aiConfig, context);


        AIEvent attachmentReceivedEvent = new AIEvent("ATTACHMENT_RECEIVED");
        AIRequest request = new AIRequest("ATTACHMENT_RECEIVED");
        request.setEvent(attachmentReceivedEvent);

        logger.debug("Attachment request: {}", request.toString());

        try {
            AIResponse response = service.request(request);

            return response.getResult().getFulfillment().getSpeech();
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return "Desculpe, mas não estou conseguindo pensar...";
    }

    public String audio(InputStream voiceStream, String senderId) {
        logger.debug("ChatbotService audio - from: '{}'", senderId);
        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(senderId).build();
        AIDataService service = new AIDataService(aiConfig, context);

        try {
            AIResponse response = service.voiceRequest(voiceStream);

            return response.getResult().getFulfillment().getSpeech();
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return "Desculpe, mas não estou conseguindo pensar...";
    }

}
