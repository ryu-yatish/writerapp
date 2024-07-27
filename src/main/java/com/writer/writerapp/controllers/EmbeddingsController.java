package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.ResponseVO.EmbeddingResponseVO;
import com.writer.writerapp.Service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Embeddings")
@RequiredArgsConstructor
public class EmbeddingsController {
    private final EmbeddingService embeddingService;

    @PostMapping("/askGpt/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public String askChatGpt(@PathVariable String bookId,@RequestBody String question) throws Exception {
        List<EmbeddingResponseVO> embeddingResponseVOList = embeddingService.getRelevantEmbeddingsContent(question, bookId);

        List<EmbeddingResponseVO> top5Embeddings = embeddingResponseVOList.stream()
                .limit(20)
                .collect(Collectors.toList());

        return embeddingService.askGpt(question, top5Embeddings);
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
