package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfiguration {

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
