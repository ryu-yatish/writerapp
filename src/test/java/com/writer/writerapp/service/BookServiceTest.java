package com.writer.writerapp.service;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Repositories.BookRepository;
import com.writer.writerapp.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private BookService bookService;

    String token="jwtToken";
    Jwt mockJwt = Jwt.withTokenValue(token)
            .claim("sub", "user123")
            .claim("roles", "ROLE_USER")
            .header("none","none")
            .build();
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);
        when(jwtDecoder.decode(token.substring(7))).thenReturn(mockJwt);
        List<Book> result = bookService.getAllBooks(token);

        assertEquals(books, result);
    }

    @Test
    public void testAddBook() {
        // Creating a book
        Book book = new Book();
        book.setBookName("Test Book");
        book.setAuthor("Test Author");

        // Mocking repository behavior
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(jwtDecoder.decode(token.substring(7))).thenReturn(mockJwt);
        // Calling the service method
        Book result = bookService.addBook(book,token);

        // Verifying the result
        assertEquals(book, result);
    }

    @Test
    public void testGetBookById() {
        // Creating a book
        Book book = new Book();
        book.setId("1");
        book.setBookName("Test Book");
        book.setAuthor("Test Author");

        // Mocking repository behavior
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));

        // Calling the service method
        Optional<Book> result = bookService.getBookById("1");

        // Verifying the result
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testUpdateBook() {
        // Creating a book
        Book book = new Book();
        book.setId("1");
        book.setBookName("Test Book");
        book.setAuthor("Test Author");

        // Calling the service method
        Book updatedBook = new Book();
        updatedBook.setBookName("Updated Book Name");
        updatedBook.setAuthor("Updated Author");
        // Mocking repository behavior
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        Book result = bookService.updateBook("1", updatedBook);

        // Verifying the result
        assertEquals(updatedBook.getBookName(), result.getBookName());
        assertEquals(updatedBook.getAuthor(), result.getAuthor());
    }

    @Test
    public void testDeleteBook() {
        // Mocking repository behavior
        when(bookRepository.findById("1")).thenReturn(Optional.ofNullable(Book.builder().build()));
        doNothing().when(bookRepository).deleteById("1");
        // Calling the service method
        bookService.deleteBook("1");

        // Verifying the method call
        verify(bookRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteBookNotFound() {
        // Mocking repository behavior
        when(bookRepository.existsById("1")).thenReturn(false);

        // Verifying that calling deleteBook with non-existing ID throws an exception
        assertThrows(Exception.class, () -> bookService.deleteBook("1"));
    }
}
