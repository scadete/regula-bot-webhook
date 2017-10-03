package com.github.scadete.regula;

import com.github.scadete.regula.stt.SpeechToTextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RegulaApplication {
    private static final Logger logger = LoggerFactory.getLogger(RegulaApplication.class);

    public static void main(String[] args) {
        logger.debug("RegulaApplication");
        SpringApplication.run(RegulaApplication.class, args);
    }
}
