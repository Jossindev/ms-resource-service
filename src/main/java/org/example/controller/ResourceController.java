package org.example.controller;

import org.springframework.web.bind.annotation.*;

@RestController("/api1")
public class ResourceController {

    @PostMapping("/resources")
    private void uploadResource(@RequestBody String data) {

    }

    @GetMapping("/resources/{id}")
    private void getResource(@PathVariable String id) {

    }
}
