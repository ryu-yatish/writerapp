package com.writer.writerapp.Service;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks(){

        return bookRepository.findAll();
    }
    public Book addBook(){

        return bookRepository.save(Book.builder().bookName("testBook").build());
    }
}
