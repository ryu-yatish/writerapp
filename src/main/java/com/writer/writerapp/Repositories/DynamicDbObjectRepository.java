package com.writer.writerapp.Repositories;

import com.writer.writerapp.Models.DynamicDbObject;
import com.writer.writerapp.Models.Embedding;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DynamicDbObjectRepository extends MongoRepository<DynamicDbObject, String> {

    List<DynamicDbObject> findBySchemaId(String schemaId);

    @Query("{ 'bookId': ?0, 'deleted': false }")
    List<Embedding> findByBookIdAndDeletedIsFalse(String bookId);
}
