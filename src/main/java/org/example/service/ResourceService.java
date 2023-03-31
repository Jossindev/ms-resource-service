package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.Resource;
import org.example.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public Integer uploadResources(byte[] mp3File) {
        return 1;
    }

    public Resource getResource(Integer id) throws ResourceNotFoundException {
        Optional<Resource> resource = resourceRepository.findById(id);

        if (resource.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found. Id: " + id);
        }

        //Get resource from S3
        return resource.get();
    }

    public List<Integer> deleteResources(List<Integer> ids) {
        List<Integer> deletedIds = new ArrayList<>();

        for (Integer id : ids) {
            Optional<Resource> resource = resourceRepository.findById(id);
            //delete from S3
            if (resource.isPresent()) {
                resourceRepository.deleteById(id);
                deletedIds.add(id);
            }
        }
        return deletedIds;
    }
}
