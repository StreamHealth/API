package com.streamhealth.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @GetMapping("/api/v1")
    public ResponseEntity<List<String>> index() {
        return ResponseEntity.ok(List.of("StreamHealth API is running!"));
    }
}
