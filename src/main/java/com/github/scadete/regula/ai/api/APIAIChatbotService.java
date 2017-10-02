package com.github.scadete.regula.ai.api;

import ai.api.*;
import ai.api.model.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.github.scadete.regula.ai.ChatbotContext;
import com.github.scadete.regula.ai.ChatbotRequest;
import com.github.scadete.regula.ai.ChatbotResponse;
import com.github.scadete.regula.ai.ChatbotService;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("apiaiChatbotService")
public class APIAIChatbotService implements ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(APIAIChatbotService.class);

    private AIConfiguration aiConfig;

    public APIAIChatbotService (@Value("${ai.clientAccessToken}") final String clientAccessToken,
                                @Value("${ai.language}") final String languageTag) {
        logger.debug("Initializing ChatbotConfiguration - clientAccessToken: '{}', language: '{}'",
                clientAccessToken,
                languageTag);
        this.aiConfig = new AIConfiguration(clientAccessToken,
                AIConfiguration.SupportedLanguages.fromLanguageTag(languageTag));
    }

    @Override
    public ChatbotResponse textMessage(ChatbotRequest request) {
        logger.debug("ChatbotService textMessage -  message: '{}', from: '{}'",
                request.getMessage(),
                request.getSessionId());

        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(request.getSessionId()).build();
        AIDataService service = new AIDataService(aiConfig, context);

        AIRequest aiRequest = new AIRequest(request.getMessage());
        aiRequest.setSessionId(request.getSessionId());

        try {
            Map<String, String> requestParameters = request.getParameters();
            if (requestParameters != null && !requestParameters.isEmpty())
            {
                AIContext requestContext = new AIContext(UUID.randomUUID().toString());
                requestContext.setLifespan(1);
                requestContext.setParameters(requestParameters);
                aiRequest.addContext(requestContext);
                logger.debug("New request context: '{}'", requestContext.getName());
            }

            AIResponse response = service.request(aiRequest, context);
            logger.debug(response.getResult().toString());
            return getResponse(response);
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null; // FIXME
    }

    @Override
    public ChatbotResponse voiceMessage(ChatbotRequest request) {
        return null;
    }

    @Override
    public ChatbotResponse attachment(ChatbotRequest request) {
        logger.debug("ChatbotService attachment - from: '{}'", request.getSessionId());
        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(request.getSessionId()).build();
        AIDataService service = new AIDataService(aiConfig, context);

        AIRequest aiRequest = new AIRequest("e=ATTACHMENT_RECEIVED");

        logger.debug("Attachment request: {}", aiRequest.toString());

        try {
            return getResponse(service.request(aiRequest));
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null; // FIXME
    }

    private ChatbotResponse getResponse(AIResponse aiResponse) {
        ChatbotResponse response = new ChatbotResponse();

        response.setSpeech(aiResponse.getResult().getFulfillment().getSpeech());
        response.setConfidence((double) aiResponse.getResult().getScore());
        setDataFromAIResponse(response, aiResponse);
        setMessageFromAIResponse(response, aiResponse);
        response.setSessionId(aiResponse.getSessionId());

        if (!aiResponse.getResult().isActionIncomplete()) {
            response.setAction(aiResponse.getResult().getAction());
        }

        return response;
    }

    private void setDataFromAIResponse(ChatbotResponse response, AIResponse aiResponse) {
        Map<String, JsonElement> dataMap = response.getData();
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.putAll(aiResponse.getResult().getParameters());
        response.setData(dataMap);
    }

    private void setMessageFromAIResponse(ChatbotResponse response, AIResponse aiResponse) {
        // TODO
    }

    private void setContextsFromAIResponse(ChatbotResponse response, AIResponse aiResponse) {
        List<ChatbotContext> contexts = new ArrayList<>();
        List<AIOutputContext> aiContexts = aiResponse.getResult().getContexts();
        for (AIOutputContext aiContext: aiContexts
             ) {
            contexts.add(new ChatbotContext(aiContext.getName(), aiContext.getParameters()));
        }
        response.setContexts(contexts);
    }

}
