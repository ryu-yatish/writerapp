package com.writer.writerapp.Repositories;

import com.writer.writerapp.Models.DynamicDbObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DynamicDbObjectRepository extends MongoRepository<DynamicDbObject, String> {

    List<DynamicDbObject> findBySchemaId(String schemaId);
}
