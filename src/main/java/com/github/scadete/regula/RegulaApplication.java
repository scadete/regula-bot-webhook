package com.github.scadete.regula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegulaApplication {
    private static final Logger logger = LoggerFactory.getLogger(RegulaApplication.class);

    public static void main(String[] args) {
        logger.debug("RegulaApplication");
        SpringApplication.run(RegulaApplication.class, args);
    }
}
