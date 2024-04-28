package com.writer.writerapp.Repositories;


import com.writer.writerapp.Models.Embedding;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmbeddingsRepository extends MongoRepository<Embedding,String> {
}
