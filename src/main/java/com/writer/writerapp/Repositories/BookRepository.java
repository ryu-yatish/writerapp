package com.writer.writerapp.Repositories;


import com.writer.writerapp.Models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book,String> {
    List<Book> findByUserId(String userId);
}
