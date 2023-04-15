package org.example.service;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResourceNotificationProducerService {

    private final RabbitMessagingTemplate rabbitTemplate;
    private final String processingQueue;
    private final ObjectMapper objectMapper;

    @Autowired
    public ResourceNotificationProducerService(@Value("queue.processing-queue") String processingQueue,
                                               RabbitMessagingTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.processingQueue = processingQueue;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void notifyResourceUploaded(int resourceId) {
        try {
            String json = objectMapper.writeValueAsString(resourceId);
            rabbitTemplate.convertAndSend(processingQueue, json);
        } catch (Exception e) {
            log.error("Exception occurred while sending resource with id:{} to the {}", resourceId, processingQueue, e);
        }
    }
}
