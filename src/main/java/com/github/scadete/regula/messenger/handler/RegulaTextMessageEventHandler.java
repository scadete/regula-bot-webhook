package com.github.scadete.regula.messenger.handler;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.github.messenger4j.send.SenderAction;
import com.github.scadete.regula.ai.ChatbotRequest;
import com.github.scadete.regula.ai.ChatbotResponse;
import com.github.scadete.regula.ai.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RegulaTextMessageEventHandler extends RegulaEventHandler implements TextMessageEventHandler{
    private static final Logger logger = LoggerFactory.getLogger(RegulaTextMessageEventHandler.class);


    public RegulaTextMessageEventHandler() {
        logger.debug("new RegulaTextMessageEventHandler");
    }

    @Override
    public void handle(TextMessageEvent event) {
        logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.getMid();
        final String messageText = event.getText();
        final String senderId = event.getSender().getId();

        final Date timestamp = event.getTimestamp();

        logger.info("Received message '{}' with text '{}' from user '{}' at '{}'",
                messageId, messageText, senderId, timestamp);

        try {
            this.sendClient.sendSenderAction(senderId, SenderAction.MARK_SEEN);
            ChatbotRequest request = new ChatbotRequest(messageText, senderId);

            ChatbotResponse response = chatbot.converse(new ChatbotRequest(messageText, senderId));

            sendTextMessage(senderId, response.getSpeech());

            String action  = response.getAction();
            if (action != null && !action.isEmpty() && !action.startsWith("input.")) {
                ChatbotResponse finalResponse = fullfillResponse(response);
                sendTextMessage(senderId, finalResponse.getSpeech());
            }
        } catch (MessengerApiException | MessengerIOException e) {
            logger.error("Message could not be sent. An unexpected error occurred.", e); }
    }

}
