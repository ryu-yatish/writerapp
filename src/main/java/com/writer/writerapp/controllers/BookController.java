package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.ResponseVO.BookResponseVO;
import com.writer.writerapp.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/getById/{id}")
    public BookResponseVO getBookById(@PathVariable String id) {
        return bookService.getBookResponseVOById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        return bookService.updateBook(id, updatedBook);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}