package com.github.scadete.regula.messenger;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MessengerConfiguration.class);

    @Bean
    public MessengerSendClient messengerSendClient(@Value("${messenger4j.pageAccessToken}") String pageAccessToken) {
        logger.debug("Initializing MessengerSendClient - pageAccessToken: '{}'", pageAccessToken);
        return MessengerPlatform.newSendClientBuilder(pageAccessToken).build();
    }
}
