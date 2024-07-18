package com.mongodb.springai_mongodb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.springai_mongodb.model.DocumentRequest;
import com.mongodb.springai_mongodb.service.SimpleService;

@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    @Autowired
    private SimpleService simpleService;

    @PostMapping("/add-docs")
    public String addDocuments(@RequestBody List<DocumentRequest> documents) {
        simpleService.addDocuments(documents);
        return "Documents added successfully";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchDocuments(
            @RequestParam String query,
            @RequestParam int topK,
            @RequestParam double similarityThreshold) {
        return simpleService.searchDocuments(query, topK, similarityThreshold);
    }

    @GetMapping("/status")
    public String status() {
        return simpleService.getStatus();
    }
}
