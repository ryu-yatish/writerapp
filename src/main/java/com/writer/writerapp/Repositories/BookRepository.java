package com.writer.writerapp.Repositories;


import com.writer.writerapp.Models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book,Integer> {
}
