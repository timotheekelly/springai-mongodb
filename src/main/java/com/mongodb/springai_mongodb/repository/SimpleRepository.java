package com.mongodb.springai_mongodb.repository;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

public interface SimpleRepository {

    void addDocuments(List<Document> documents);

    List<Document> searchDocuments(SearchRequest searchRequest);
}
