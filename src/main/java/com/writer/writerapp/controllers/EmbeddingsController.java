package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Embeddings")
@RequiredArgsConstructor
public class EmbeddingsController {
    private final EmbeddingService embeddingService;

    @GetMapping("/askGpt/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public String askChatGpt(@PathVariable String bookId,@RequestBody String question) throws Exception {
        return embeddingService.askGpt(question);
    }
}
