package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.ResponseVO.EmbeddingResponseVO;
import com.writer.writerapp.Service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Embeddings")
@RequiredArgsConstructor
public class EmbeddingsController {
    private final EmbeddingService embeddingService;

    @PostMapping("/askGpt/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public String askChatGpt(@PathVariable String bookId,@RequestBody String question) throws Exception {
        return embeddingService.askGpt(question, List.of());
    }
    @PostMapping("/analyzeBook/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus analyzeBook(@PathVariable String bookId) throws Exception {
        embeddingService.deleteEmbeddingsForBook(bookId);
        embeddingService.createEmbeddingsForBook(bookId);
        return HttpStatus.OK;
    }

    @PostMapping("/getRelevantData/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmbeddingResponseVO> getRelevantData(@PathVariable String bookId, @RequestBody String question) throws Exception {
        return embeddingService.getRelevantEmbeddingsContent(question,bookId);
    }

}
