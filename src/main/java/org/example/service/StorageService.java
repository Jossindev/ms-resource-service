package org.example.service;

import java.util.List;

import org.example.dto.StorageResponse;
import org.example.dto.StorageType;
import org.example.exceptions.StorageNotFoundException;
import org.example.service.client.StorageServiceClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {

    private final StorageServiceClient storageClient;

    public StorageResponse getStagingStorageDetails() {
        return getStorageResponse(StorageType.STAGING);
    }

    public StorageResponse getPermanentStorageDetails() {
        return getStorageResponse(StorageType.PERMANENT);
    }

    private StorageResponse getStorageResponse(StorageType storageType) {
        List<StorageResponse> storages = storageClient.getStorages();
        log.info("Got " + storageType.name() + " storage details : " + storages);

        return storages.stream()
            .filter(storage -> storageType.equals(storage.getStorageType()))
            .findFirst()
            .orElseThrow(() -> new StorageNotFoundException("No storage found for type: " + storageType));
    }
}
