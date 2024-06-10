package com.mongodb.springai_mongodb.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    @Autowired
    private VectorStore vectorStore;

    @PostMapping("/add-docs")
    public String addDocuments(@RequestBody List<DocumentRequest> documents) {
        List<Document> docs = documents.stream()
            .map(doc -> new Document(doc.getContent(), doc.getMetadata()))
            .collect(Collectors.toList());
        vectorStore.add(docs);
        return "Documents added successfully";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchDocuments(
            @RequestParam String query,
            @RequestParam int topK,
            @RequestParam double similarityThreshold) {
    	
    	SearchRequest searchRequest = SearchRequest.query(query)
    	        .withTopK(topK)
    	        .withSimilarityThreshold(similarityThreshold);
    	
    	List<Document> results = vectorStore.similaritySearch(searchRequest);

        // Extract the content and metadata fields to return in the response
        return results.stream().map(doc -> Map.of(
                "content", doc.getContent(),
                "metadata", doc.getMetadata()
        )).collect(Collectors.toList());
    }

    @GetMapping("/status")
    public String status() {
        return "Application is running!";
    }
}

class DocumentRequest {
    private String content;
    private Map<String, Object> metadata;

    // Getters and Setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
