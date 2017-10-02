package com.github.scadete.regula.messenger.handler;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.github.scadete.regula.ai.ChatbotContext;
import com.github.scadete.regula.ai.ChatbotRequest;
import com.github.scadete.regula.ai.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RegulaAttachmentMessageEventHandler  extends RegulaEventHandler implements AttachmentMessageEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegulaAttachmentMessageEventHandler.class);

    @Autowired
    ChatbotService chatbot;

    public RegulaAttachmentMessageEventHandler() {
        logger.debug("new RegulaAttachmentMessageEventHandler");
    }

    @Override
    public void handle(AttachmentMessageEvent event) {
        logger.debug("Received AttachmentMessageEvent: {}", event);

        final String messageId = event.getMid();
        final List<AttachmentMessageEvent.Attachment> attachments = event.getAttachments();
        final String senderId = event.getSender().getId();
        final Date timestamp = event.getTimestamp();

        logger.info("Received message '{}' with attachments from user '{}' at '{}':",
                messageId, senderId, timestamp);

        Map<String, String> payloadMap = new HashMap<>();

        boolean hasAudio = false;
        boolean hasUnsupported = false;
        for (AttachmentMessageEvent.Attachment attachment: attachments) {
            final AttachmentMessageEvent.AttachmentType attachmentType = attachment.getType();
            final AttachmentMessageEvent.Payload payload = attachment.getPayload();

            String payloadAsString = "";
            if (payload.isBinaryPayload()) {
                payloadAsString = payload.asBinaryPayload().getUrl();
                if (attachmentType.equals(AttachmentMessageEvent.AttachmentType.AUDIO)) {
                    hasAudio = true;
                }
                payloadMap.put(UUID.randomUUID().toString(), payloadAsString);

            } else {
                hasUnsupported = true;
            }
            logger.info("Attachment of type '{}' with payload '{}'", attachmentType, payloadAsString);
        }

        ChatbotContext context = new ChatbotContext("attachment-urls", 10);
        context.getData().putAll(payloadMap);

        ChatbotRequest request = new ChatbotRequest(senderId);

        if (hasUnsupported) {
            // TODO
        } else if (hasAudio) {
            request.setMessage("audio"); //TODO
        } else {
            request.setEvent("ATTACHMENT_RECEIVED");
            request.addContext(context);
        }

        String response = chatbot.converse(request).getSpeech();

        sendTextMessage(senderId, response);
    }
}
