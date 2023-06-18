package org.example.service.client;

import java.util.List;

import org.example.config.RetryConfiguration;
import org.example.dto.StorageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "ms-resource-service",
    url = "${service.storage.endpoint}",
    configuration = RetryConfiguration.class)
public interface StorageServiceClient {

    @GetMapping(path = "/storages", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<StorageResponse>> getStorages();

}
