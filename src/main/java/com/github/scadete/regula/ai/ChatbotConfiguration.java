package com.github.scadete.regula.ai;

import ai.api.AIConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatbotConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotConfiguration.class);

    @Value("${ai.clientAccessToken}")
    private static String CLIENT_ACCESS_TOKEN;

    @Value("${ai.language}")
    private static String LANGUAGE_TAG;

    @Bean
    public AIConfiguration aiConfiguration() {
        logger.debug("Initializing ChatbotConfiguration - clientAccessToken: '{}', language: '{}'", CLIENT_ACCESS_TOKEN, LANGUAGE_TAG);
        return new AIConfiguration(CLIENT_ACCESS_TOKEN, AIConfiguration.SupportedLanguages.fromLanguageTag(LANGUAGE_TAG));
    }

}
