package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import feign.Retryer;

@Configuration
public class RetryConfiguration {

    @Value("${feign.retryAttempts}")
    private int retryAttempts;

    @Value("${feign.retryPeriod}")
    private long retryPeriod;

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(retryPeriod, retryPeriod, retryAttempts);
    }
    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
