package com.writer.writerapp.Service;

import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Repositories.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final BookService bookService;
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    public Chapter addChapter(Chapter chapter,String BookId) {
        chapter.setCreatedDate(new Date());
        chapter.setLastModified(new Date());
        Chapter chapterNew =chapterRepository.save(chapter);
        bookService.addChapterToBook(chapterNew.getId(),BookId);
        return chapterNew;
    }

    public Optional<Chapter> getChapterById(String id) {
        return chapterRepository.findById(id);
    }

    public Chapter updateChapter(String id, Chapter updatedChapter) {
        Optional<Chapter> optionalChapter = chapterRepository.findById(id);
        if (optionalChapter.isPresent()) {
            Chapter existingChapter = optionalChapter.get();
            existingChapter.setTitle(updatedChapter.getTitle());
            existingChapter.setContent(updatedChapter.getContent());
            existingChapter.setLastModified(new Date());
            return chapterRepository.save(existingChapter);
        } else {
            // Handle case when chapter with given id is not found
            return null; // Or throw an exception
        }
    }

    public void deleteChapter(String id) {
        chapterRepository.deleteById(id);
    }
}