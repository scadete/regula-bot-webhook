package com.github.scadete.regula.messenger.handler;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.scadete.regula.ai.ChatbotContext;
import com.github.scadete.regula.ai.ChatbotRequest;
import com.github.scadete.regula.ai.ChatbotResponse;
import com.github.scadete.regula.ai.ChatbotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public abstract class RegulaEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegulaEventHandler.class);


    @Autowired
    @Resource(name="apiaiChatbotService")
    ChatbotService chatbot;

    @Autowired
    protected MessengerSendClient sendClient;

    protected void sendTextMessage(String recipientId, String text) {
        logger.debug("sendTextMessage: '{}' to: '{}'", text, recipientId);
        try {
            final Recipient recipient = Recipient.newBuilder().recipientId(recipientId).build();
            final NotificationType notificationType = NotificationType.REGULAR;
            final String metadata = "DEVELOPER_DEFINED_METADATA";

            this.sendClient.sendTextMessage(recipient, notificationType, text, metadata);
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    protected ChatbotResponse fullfillResponse(ChatbotResponse initialResponse) {
        ChatbotResponse response = new ChatbotResponse();

        String action = initialResponse.getAction();
        logger.debug("Action: '{}'", action);

        switch (action) {
            case "process.solve":
                response = processSolveAction(initialResponse);
                break;
            case "process.status":
                response = processStatusAction(initialResponse);
                break;
            case "attachment.send":
                response = attachmentSendAction(initialResponse);
                break;
            default:
                return initialResponse;
        }

        return response;
    }

    private ChatbotResponse processSolveAction(ChatbotResponse initialResponse) {
        ChatbotRequest request = new ChatbotRequest(initialResponse.getSessionId());

        Map<String, String> requestParameters = new HashMap<>();

        // TODO Sample Business Rule
        String event = "RETURN_PROCESS_NOT_FOUND";
        String processId = initialResponse.getData().get("businessKey").getAsString();
        if (processId.endsWith("F")) {

            event = "RETURN_PROCESS_FOUND";
            requestParameters.put("reviewText", "Este é um exemplo de texto de revisão\n\nAtt.,\nRevisor.");

            request.addContext(new ChatbotContext("process-solve-params", requestParameters));
        }

        request.setEvent(event);
        ChatbotResponse response = chatbot.converse(request);
        return response;
    }

    private ChatbotResponse processStatusAction(ChatbotResponse initialResponse) {
        return initialResponse; // TODO
    }

    private ChatbotResponse attachmentSendAction(ChatbotResponse initialResponse) {
        ChatbotRequest request = new ChatbotRequest(initialResponse.getSessionId());
        request.setEvent("ATTACHMENT_SEND_SUCCESS");
        ChatbotResponse response = chatbot.converse(request);
        return response;
    }

    private void handleSendException(Exception e) {
        logger.error("An unexpected error occurred.", e);
    }
}
