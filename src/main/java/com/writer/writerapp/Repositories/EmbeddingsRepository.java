package com.writer.writerapp.Repositories;


import com.writer.writerapp.Models.Embedding;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmbeddingsRepository extends MongoRepository<Embedding,String> {
    List<Embedding> findByBookId(String bookId);
    void deleteByBookId(String bookId);

}
