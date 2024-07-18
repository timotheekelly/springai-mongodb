package com.mongodb.springai_mongodb.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.springai_mongodb.model.DocumentRequest;
import com.mongodb.springai_mongodb.repository.SimpleRepository;

@Service
public class SimpleService {

    @Autowired
    private SimpleRepository simpleRepository;

    public void addDocuments(List<DocumentRequest> documents) {
        List<Document> docs = documents.stream()
            .map(doc -> new Document(doc.getContent(), doc.getMetadata()))
            .collect(Collectors.toList());
        simpleRepository.addDocuments(docs);
    }

    public List<Map<String, Object>> searchDocuments(String query, int topK, double similarityThreshold) {
        SearchRequest searchRequest = SearchRequest.query(query)
            .withTopK(topK)
            .withSimilarityThreshold(similarityThreshold);

        List<Document> results = simpleRepository.searchDocuments(searchRequest);

        return results.stream()
            .map(doc -> Map.of("content", doc.getContent(), "metadata", doc.getMetadata()))
            .collect(Collectors.toList());
    }

    public String getStatus() {
        return "Application is running!";
    }
}
