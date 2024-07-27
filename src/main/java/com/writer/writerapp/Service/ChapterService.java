package com.writer.writerapp.Service;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Models.RequestVO.ChapterRequestVO;
import com.writer.writerapp.Repositories.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    public Chapter addChapter(ChapterRequestVO chapterRequestVO, String BookId) {
        Chapter chapter = Chapter.builder()
                .content(chapterRequestVO.getContent())
                .title(chapterRequestVO.getTitle())
                .lastModified(new Date())
                .createdDate(new Date())
                .bookId(BookId)
                .build();
        return chapterRepository.save(chapter);
    }

    public Optional<Chapter> getChapterById(String id) {
        return chapterRepository.findById(id);
    }

    public Chapter updateChapter(String id, ChapterRequestVO updatedChapter) {
        Optional<Chapter> optionalChapter = chapterRepository.findById(id);
        if (optionalChapter.isPresent()) {
            Chapter existingChapter = optionalChapter.get();
            existingChapter.setTitle(StringUtils.isEmpty(updatedChapter.getTitle())? existingChapter.getTitle() : updatedChapter.getTitle());
            existingChapter.setContent(updatedChapter.getContent());
            existingChapter.setLastModified(new Date());
            return chapterRepository.save(existingChapter);
        } else {
            // Handle case when chapter with given id is not found
            return null; // Or throw an exception
        }
    }

    public String deleteChapter(String id) {
        Optional<Chapter> optionalChapter = chapterRepository.findById(id);
        String bookId = null;
        if (optionalChapter.isPresent()) {
            bookId = optionalChapter.get().getBookId();
            chapterRepository.deleteById(id);
        }
        return bookId;
    }

    public List<String> getMicroContentsList(String chapterId){
        Optional<Chapter> chapterOptional = chapterRepository.findById(chapterId);
        List<String> microContentList= new ArrayList<>();
        StringBuilder currentContentBuffer= new StringBuilder();

        chapterOptional.ifPresent(chapter -> {
            Document doc = Jsoup.parse(chapter.getContent());
            for (Element paragraph : doc.select("p, br")) {
                log.info(paragraph.text());
                if(currentContentBuffer.length()<500){
                    currentContentBuffer.append(paragraph.text());
                }else {
                    microContentList.add(currentContentBuffer.toString());
                    currentContentBuffer.setLength(0);
                }
            }
            microContentList.add(currentContentBuffer.toString());
            chapter.setLastAnalyzed(new Date());
            chapterRepository.save(chapter);
        });
        return microContentList;
    }
}