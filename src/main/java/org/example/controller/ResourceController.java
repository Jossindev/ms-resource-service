package org.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Size;

import org.example.dto.DeletedResourceResponse;
import org.example.dto.ResourceResponse;
import org.example.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<ResourceResponse> uploadResource(@RequestBody byte[] mp3File) {
        if (mp3File == null || mp3File.length == 0) {
            return ResponseEntity.badRequest().build();
        }
        Integer id = resourceService.uploadResources(mp3File);
        ResourceResponse response = new ResourceResponse(id);
        return ResponseEntity.ok(response);
    }

    //Add header "Range":"bytes=0-999"
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Integer id,
                                              @RequestHeader(value = "Range", required = false) String range) {
        byte[] resourceData = resourceService.getResourceData(id);

        if (range != null) {
            String[] parts = range.split("=");
            String[] rangeParts = parts[1].split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = resourceData.length - 1;
            if (rangeParts.length > 1) {
                end = Integer.parseInt(rangeParts[1]);
            }
            resourceData = Arrays.copyOfRange(resourceData, start, end + 1);
            return new ResponseEntity<>(resourceData, HttpStatus.PARTIAL_CONTENT);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resourceData);
    }

    @DeleteMapping
    public ResponseEntity<DeletedResourceResponse> getResource(@RequestParam("id") @Size(max = 200) String id) {
        List<Integer> ids = Stream.of(id.split(","))
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        List<Integer> deletedIds = resourceService.deleteResources(ids);
        DeletedResourceResponse deletedResourceResponse = new DeletedResourceResponse(deletedIds);
        return ResponseEntity.ok(deletedResourceResponse);
    }
}
