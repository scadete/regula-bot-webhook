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
        "com.github.scadete.regula.ai.api",
        "com.github.scadete.regula.ai.wit"
})
public class ChatbotConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotConfiguration.class);
}
