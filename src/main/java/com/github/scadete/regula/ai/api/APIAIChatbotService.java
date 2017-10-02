package com.github.scadete.regula.ai.api;

import ai.api.*;
import ai.api.model.*;
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
    public ChatbotResponse converse(ChatbotRequest request) {
        logger.debug("ChatbotService converse -  message: '{}', from: '{}'",
                request.getMessage(),
                request.getSessionId());

        AIServiceContext context = (new AIServiceContextBuilder()).setSessionId(request.getSessionId()).build();
        AIDataService service = new AIDataService(aiConfig, context);

        try {
            AIResponse response = service.request(getAIRequest(request));
            logger.debug(response.getResult().toString());
            return getResponse(response);
        } catch (AIServiceException e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null; // FIXME
    }

    private AIRequest getAIRequest(ChatbotRequest request) {
        AIRequest aiRequest = new AIRequest();
        aiRequest.setSessionId(request.getSessionId());
        String eventName = request.getEvent();

        if (eventName != null && !eventName.isEmpty()) {
            aiRequest.setEvent(new AIEvent(eventName));
        }
        aiRequest.setQuery(request.getMessage());

        setContextFromChatbotRequest(aiRequest, request);

        return aiRequest;
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

    private void setContextFromChatbotRequest(AIRequest aiRequest, ChatbotRequest chatbotRequest) {
        List<AIContext> contexts = new ArrayList<>();

        List<ChatbotContext> chatbotContexts = chatbotRequest.getContextList();
        if (chatbotContexts != null) {
            for (ChatbotContext chatbotContext : chatbotContexts) {
                AIContext context = new AIContext(chatbotContext.getName());
                Map<String, String> chatbotContextData = chatbotContext.getData();
                if (chatbotContextData != null) {
                    context.setParameters(chatbotContextData);
                }
                context.setLifespan(chatbotContext.getLifespan());
                contexts.add(context);
            }
        }
        aiRequest.setContexts(contexts);
    }

}
