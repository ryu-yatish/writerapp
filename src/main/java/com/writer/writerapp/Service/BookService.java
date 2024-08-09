package com.writer.writerapp.Service;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Models.DynamicDbSchema;
import com.writer.writerapp.Models.ResponseVO.BookResponseVO;
import com.writer.writerapp.Repositories.BookRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ChapterService chapterService;
    private final JwtDecoder jwtDecoder;
    public List<Book> getAllBooks(String token) {
        String userId = getUserIDFromPrinciple(token);
        return bookRepository.findByUserId(userId);
    }

    public Book addBook(Book book,String token) {
        String userId = getUserIDFromPrinciple(token);
        book.setCreatedDate(new Date());
        book.setLastModified(new Date());
        book.setUserId(userId);
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public void updateLastAnalyzed(String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        bookOptional.ifPresent(book ->{
            book.setLastAnalyzed(new Date());
            bookRepository.save(book);
        } );
    }
    public void addDDSSchema(DynamicDbSchema schema, String bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        book.get().addDynamicDbSchema(schema);
        bookRepository.save(book.get());
    }

    public void updateDDSSchema(String bookId, String schemaId, DynamicDbSchema updatedSchema) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        book.get().updateDynamicDbSchema(schemaId, updatedSchema);
        bookRepository.save(book.get());
    }

    public void deleteDDSSchema(String bookId, String schemaId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        book.get().deleteDynamicDbSchema(schemaId);
        bookRepository.save(book.get());
    }
    public BookResponseVO getBookResponseVOById(String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Book book = bookOptional.get();
        BookResponseVO bookResponseVO = BookResponseVO.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .chapterCount(book.getChapterCount())
                .lastModified(book.getLastModified())
                .createdDate(book.getCreatedDate())
                .lastAnalyzed(book.getLastAnalyzed())
                .dynamicDbSchemaList(book.getDynamicDbSchemaList())
                .build();
        List<Chapter> chapterList = new ArrayList<>();
        book.getChapters().forEach(chapterId->{
            chapterService.getChapterById(chapterId).ifPresent(chapterList::add);
        });
        bookResponseVO.setChapters(chapterList);
        return bookResponseVO;
    }

    public Book updateBook(String id, Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            if(!StringUtils.isEmpty(updatedBook.getBookName()))
                existingBook.setBookName(updatedBook.getBookName());
            if(!StringUtils.isEmpty(updatedBook.getAuthor()))
                existingBook.setAuthor(updatedBook.getAuthor());
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
    public void addChapterToBook(String chapterId,String BookId){
        Optional<Book> bookOptional = bookRepository.findById(BookId);
        bookOptional.ifPresent(book->{
            List<String> chapters = book.getChapters();
            if(chapters==null) chapters = new ArrayList<>();
            chapters.add(chapterId);
            book.setChapters(chapters);
            book.setChapterCount(chapters.size());
            bookRepository.save(book);
        });
    }

    public void deleteChapterFromBook(String chapterId,String BookId){
        Optional<Book> bookOptional = bookRepository.findById(BookId);
        bookOptional.ifPresent(book->{
            List<String> chapters = book.getChapters();
            chapters.remove(chapterId);
            book.setChapters(chapters);
            book.setChapterCount(chapters.size());
            bookRepository.save(book);
        });
    }
    public String getUserIDFromPrinciple(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            Jwt decodedJwt = jwtDecoder.decode(token.substring(7));
            return decodedJwt.getSubject();
        }
        return "";
    }
}