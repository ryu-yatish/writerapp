package com.writer.writerapp.Repositories;


import com.writer.writerapp.Models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByUserId(String userId);

    Optional<Book> findByIdAndDeletedIsFalse(String id);
    // Method to find books by userId where deleted is false
    List<Book> findByUserIdAndDeletedIsFalse(String userId);
}