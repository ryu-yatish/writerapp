package com.writer.writerapp.Repositories;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends MongoRepository<Chapter,String> {
    List<Chapter> findAllByDeletedIsFalse();

    Optional<Chapter> findByIdAndDeletedIsFalse(String id);
}
