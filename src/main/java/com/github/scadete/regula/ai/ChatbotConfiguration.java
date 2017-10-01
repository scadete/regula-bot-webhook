package com.github.scadete.regula.ai;

import ai.api.AIConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.github.scadete.regula.ai"
})
public class ChatbotConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotConfiguration.class);

    @Value("${ai.clientAccessToken}")
    private String clientAccessToken;

    @Value("${ai.language}")
    private String languageTag;

    @Bean
    public AIConfiguration aiConfiguration() {
        logger.debug("Initializing ChatbotConfiguration - clientAccessToken: '{}', language: '{}'", clientAccessToken, languageTag);
        return new AIConfiguration(clientAccessToken, AIConfiguration.SupportedLanguages.fromLanguageTag(languageTag));
    }

}
