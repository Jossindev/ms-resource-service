package org.example.service.client;

import java.util.Arrays;
import java.util.List;

import org.example.dto.StorageResponse;
import org.example.dto.StorageType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceClient {

    private final StorageClient storageClient;

    @CircuitBreaker(name = "storage", fallbackMethod = "fallback")
    public List<StorageResponse> getStorages() {
        ResponseEntity<List<StorageResponse>> storages = storageClient.getStorages();
        if (storages.getBody() == null) {
            throw new RuntimeException("No storages found, use stub storage details...");
        }

        return storages.getBody();
    }

    public List<StorageResponse> fallback(Throwable t) {
        log.error("Error occurred when calling the method ", t);
        return Arrays.asList(StorageResponse.builder()
                .storageType(StorageType.STAGING)
                .bucket("stage-bucket")
                .path("/staging")
                .build(),
            StorageResponse.builder()
                .storageType(StorageType.PERMANENT)
                .bucket("perm-bucket")
                .path("/permanent")
                .build());
    }
}
