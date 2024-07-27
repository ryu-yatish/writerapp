package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Models.RequestVO.ChapterRequestVO;
import com.writer.writerapp.Service.BookService;
import com.writer.writerapp.Service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;
    private final BookService bookService;
    @GetMapping("/all")
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @PostMapping("/add/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Chapter addChapter(@RequestBody ChapterRequestVO chapterRequestVo, @PathVariable String bookId) {
        Chapter chapter = chapterService.addChapter(chapterRequestVo,bookId);
        bookService.addChapterToBook(chapter.getId(),bookId);
        return chapter;

    }

    @GetMapping("/{id}")
    public Chapter getChapterById(@PathVariable String id) {
        return chapterService.getChapterById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found with id: " + id));
    }

    @PutMapping("/{id}")
    public Chapter updateChapter(@PathVariable String id, @RequestBody ChapterRequestVO updatedChapter) {
        return chapterService.updateChapter(id, updatedChapter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChapter(@PathVariable String id) {

        String bookId= chapterService.deleteChapter(id);
        if(bookId!=null)bookService.deleteChapterFromBook(id,bookId);
    }
}
