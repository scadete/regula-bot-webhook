package com.github.scadete.regula.messenger.handler;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.github.scadete.regula.ai.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

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


        attachments.forEach(attachment -> {
            final AttachmentMessageEvent.AttachmentType attachmentType = attachment.getType();
            final AttachmentMessageEvent.Payload payload = attachment.getPayload();
            String response = "";

            String payloadAsString = null;
            if (payload.isBinaryPayload()) {
                payloadAsString = payload.asBinaryPayload().getUrl();

                if (attachmentType.equals("AUDIO")) {
                    try {
                        InputStream voiceStream = new URL(payload.asBinaryPayload().getUrl()).openStream();
                        response = chatbot.audio(voiceStream, senderId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    response = chatbot.attachment(senderId);
                }
            }
            if (payload.isLocationPayload()) {
                payloadAsString = payload.asLocationPayload().getCoordinates().toString();
            }

            sendTextMessage(senderId, response);

            logger.info("Attachment of type '{}' with payload '{}'", attachmentType, payloadAsString);
        });
    }
}
