package com.mongodb.springai_mongodb.config;

import org.bson.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Configuration
@SpringBootConfiguration
@EnableAutoConfiguration
public class MongodbConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.ai.vectorstore.mongodb.collection-name:vector_store}")
    private String collectionName;

    @Value("${spring.ai.vectorstore.mongodb.indexName:vector_index}")
    private String indexName;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public VectorStore mongodbVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        boolean indexExists = false;

        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Retrieve search indexes
            try (MongoCursor<Document> resultsCursor = collection.listSearchIndexes()
                .iterator()) {
                while (resultsCursor.hasNext()) {
                    Document indexDocument = resultsCursor.next();
                    if (indexName.equals(indexDocument.getString("name"))) {
                        indexExists = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set initializeSchema based on whether the index exists
        boolean initializeSchema = !indexExists;

        return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel, MongoDBAtlasVectorStore.MongoDBVectorStoreConfig.builder()
            .build(), initializeSchema);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(new OpenAiApi(openAiKey));
    }
}
