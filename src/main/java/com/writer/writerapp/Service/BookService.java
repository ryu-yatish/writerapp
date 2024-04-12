package com.writer.writerapp.Service;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Repositories.BookRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Book updateBook(String id, Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            if(StringUtils.isEmpty(updatedBook.getBookName()))
                existingBook.setBookName(updatedBook.getBookName());
            if(StringUtils.isEmpty(updatedBook.getAuthor()))
                existingBook.setAuthor(updatedBook.getAuthor());
            if(updatedBook.getChapters()>=0)
                existingBook.setChapters(updatedBook.getChapters());
            existingBook.setLastModified(new Date());
            return bookRepository.save(existingBook);
        } else {
            throw new  ResponseStatusException(HttpStatus.NOT_FOUND,"Book with id "+ id + "Not found");
        }
    }

    public void deleteBook(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Book with id "+ id + "Not found");
        }
    }
}