package com.mongodb.springai_mongodb.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootConfiguration
@EnableAutoConfiguration
public class MongodbConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    // This now appears to be done automatically
    // Currently receives error Command failed with error 68 (IndexAlreadyExists): 'Duplicate Index' on server
//    @Bean
//    public VectorStore mongodbVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
//        return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel,
//                MongoDBAtlasVectorStore.MongoDBVectorStoreConfig.builder()
//                        .build(),
//                true);
//    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(new OpenAiApi(openAiKey));
    }
}
