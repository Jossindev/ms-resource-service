package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.exceptions.ResourceNotFoundException;
import org.example.model.Resource;
import org.example.repository.ResourceRepository;
import org.example.utils.S3Utils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final S3Utils localstackStorage;
    private final ResourceRepository resourceRepository;

    public Integer uploadResources(byte[] mp3File) {
        String key = localstackStorage.uploadFile(mp3File);
        Resource resource = Resource.builder()
            .s3Key(key)
            .build();
        return resourceRepository.save(resource).getId();
    }

    public byte[] getResourceData(Integer id) throws ResourceNotFoundException {
        Optional<Resource> resource = resourceRepository.findById(id);

        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found. Id: " + id);
        }

        return localstackStorage.downloadFile("resource.get().s3Key");
    }

    public List<Integer> deleteResources(List<Integer> ids) {
        List<Integer> deletedIds = new ArrayList<>();

        for (Integer id : ids) {
            Optional<Resource> resource = resourceRepository.findById(id);
            if (resource.isPresent()) {
                resourceRepository.deleteById(id);
                localstackStorage.deleteFile(resource.get().s3Key);
                deletedIds.add(id);
            }
        }
        return deletedIds;
    }
}
