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

    //    @Retry(name = "storageConfig", fallbackMethod = "storageFallback")
    @CircuitBreaker(name = "storageConfig", fallbackMethod = "storageFallback")
    public List<StorageResponse> getStorages() {
        log.info("Try to get storage details...");
        ResponseEntity<List<StorageResponse>> storages = storageClient.getStorages();
        if (storages == null || storages.getBody() == null) {
            log.warn("No storages found, use stub storage details...");
            throw new RuntimeException("No storages found, use stub storage details...");
        }

        return storages.getBody();
    }

    public List<StorageResponse> storageFallback(Throwable t) {
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
