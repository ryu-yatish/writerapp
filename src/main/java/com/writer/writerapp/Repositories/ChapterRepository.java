package com.writer.writerapp.Repositories;

import com.writer.writerapp.Models.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChapterRepository extends MongoRepository<Chapter,String> {
}
