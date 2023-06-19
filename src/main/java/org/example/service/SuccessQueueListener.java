package org.example.service;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessQueueListener {

    private final ResourceFacade resourceFacade;

    @RabbitListener(queues = "${queue.success-queue}")
    public void listenMessage(int resourceId) {
        log.info("Received message from success queue. ResourceId = {}", resourceId);
        try {
            byte[] mp3File = resourceFacade.getResourceData(resourceId);
            resourceFacade.moveFileToPermanentStorage(mp3File, resourceId);
        } catch (final Exception ex) {
            log.error("Processing message {} with error: ", resourceId, ex);
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
