package org.example.service;

import java.util.List;

import org.example.dto.StorageResponse;
import org.example.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceFacade {

    private final StorageService storageService;
    private final ResourceService resourceService;
    private final ResourceNotificationProducerService notificationService;

    public Integer saveFileToStagingStorage(byte[] mp3file) {
        StorageResponse stagingStorageDetails = storageService.getStagingStorageDetails();
        Integer resourceId = resourceService.uploadResource(mp3file, stagingStorageDetails);
        notificationService.notifyResourceUploaded(resourceId);
        return resourceId;
    }

    public void moveFileToPermanentStorage(byte[] mp3file, Integer resourceId) {
        StorageResponse permanentStorageDetails = storageService.getPermanentStorageDetails();
        resourceService.uploadResource(mp3file, permanentStorageDetails);
        //TODO maybe need to delete staging file by resourceID
    }

    public byte[] getResourceData(Integer id) throws ResourceNotFoundException {
        StorageResponse stagingStorageDetails = storageService.getStagingStorageDetails();
        return resourceService.getResourceData(id, stagingStorageDetails.getBucket());
    }

    public List<Integer> deleteResources(List<Integer> ids) {
        StorageResponse stagingStorageDetails = storageService.getStagingStorageDetails();
        return resourceService.deleteResources(ids, stagingStorageDetails.getBucket());
    }
}
