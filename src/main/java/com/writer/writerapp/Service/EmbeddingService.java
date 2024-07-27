package com.writer.writerapp.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Models.Embedding;
import com.writer.writerapp.Models.GptResponse;
import com.writer.writerapp.Models.ResponseVO.EmbeddingResponseVO;
import com.writer.writerapp.Repositories.EmbeddingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingService {
    private final ApiService apiService;
    private final BookService bookService;
    private final ChapterService chapterService;
    private final ObjectMapper objectMapper;
    private final EmbeddingsRepository embeddingsRepository;
    public String askGpt(String userMessageStr, List<EmbeddingResponseVO> embeddingList) throws Exception {
        String responseString = apiService.sendChatCompletionRequest(userMessageStr, embeddingList);
        GptResponse gptResponse = objectMapper.readValue(responseString, GptResponse.class);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
    public void deleteEmbeddingsForBook(String bookId){
        Optional<Book> bookOptional = bookService.getBookById(bookId);
        if (bookOptional.isEmpty()) {
            log.error("Book not found for ID: {}", bookId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        embeddingsRepository.deleteByBookId(bookId);
    }
    public void createEmbeddingsForBook(String bookId) {
        Optional<Book> bookOptional = bookService.getBookById(bookId);
        if (bookOptional.isEmpty()) {
            log.error("Book not found for ID: {}", bookId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Book book = bookOptional.get();
        for (String chapterId : book.getChapters()) {
            Optional<Chapter> chapterOptional = chapterService.getChapterById(chapterId);
            if (chapterOptional.isEmpty()) {
                log.error("Chapter not found for ID: {}", chapterId);
                continue;
            }
            generateChatGptEmbeddingsForChapter(chapterId,bookId);
        }
        bookService.updateLastAnalyzed(bookId);
    }

    private void generateChatGptEmbeddingsForChapter(String chapterId,String bookId) {
        List<String> content_list = chapterService.getMicroContentsList(chapterId);
        content_list.forEach(microContent->{
            List<Double> embeddings = null;
            try {
                embeddings = apiService.getEmbeddings(microContent);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            double[] vector = embeddings.stream()
                    .limit(1536)
                    .mapToDouble(Double::floatValue)
                    .toArray();
            Embedding embedding = Embedding.builder()
                    .vector(vector)
                    .dataString(microContent)
                    .createdDate(new Date())
                    .lastModified(new Date())
                    .dataType("ChapterData")
                    .chapterId(chapterId)
                    .bookId(bookId)
                    .build();
            embeddingsRepository.save(embedding);
        });

    }
    private double cosineSimilarity(double[] vector1, double[] vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public List<EmbeddingResponseVO> getMostRelevantEmbeddings(double[] targetVector, String bookId) {
        List<Embedding> embeddings = embeddingsRepository.findByBookId(bookId);
        List<EmbeddingResponseVO> relevantEmbeddings = new ArrayList<>();

        embeddings.parallelStream().forEach(embedding -> {
            double similarity = cosineSimilarity(targetVector, embedding.getVector());
            relevantEmbeddings.add(EmbeddingResponseVO.builder().
                    similarity(similarity)
                    .dataString(embedding.getDataString())
                    .build());
        });

        relevantEmbeddings.sort(Comparator.comparingDouble(EmbeddingResponseVO::getSimilarity).reversed());
        return relevantEmbeddings.subList(0, Math.min(relevantEmbeddings.size(), 20));
    }
    public List<EmbeddingResponseVO> getRelevantEmbeddingsContent(String inputText,String bookId) throws JsonProcessingException {
        List<Double> embeddings = apiService.getEmbeddings(inputText);
        double[] targetVector = embeddings.stream()
                .limit(1536)
                .mapToDouble(Double::floatValue)
                .toArray();
        return getMostRelevantEmbeddings(targetVector,bookId);
    }
}
