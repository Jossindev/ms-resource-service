package org.example.service;

import java.util.List;

import org.example.dto.StorageResponse;
import org.example.dto.StorageType;
import org.example.service.client.StorageServiceClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageServiceClient storageClient;

    @Cacheable("stagingDetails")
    public StorageResponse getStagingStorageDetails() {
        List<StorageResponse> storages = storageClient.getStorages().getBody();

        return storages.stream()
            .filter(f -> f.getStorageType().equals(StorageType.STAGING))
            .findFirst().orElseThrow();
    }

    @Cacheable("permanentDetails")
    public StorageResponse getPermanentStorageDetails() {
        List<StorageResponse> storages = storageClient.getStorages().getBody();

        return storages.stream()
            .filter(f -> f.getStorageType().equals(StorageType.PERMANENT))
            .findFirst().orElseThrow();
    }
}
