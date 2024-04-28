package com.writer.writerapp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.writer.writerapp.Models.ChatCompletionRequest;
import com.writer.writerapp.Models.Embedding;
import com.writer.writerapp.Models.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiService {
    @Value("${open.ai.api.key}")
    private String apiKey;
    @Value("${gpt.three.five.model.name}")
    private String GPT3Point5;
    @Value("${gpt.completions.url}")
    private String gptCompletionsApiUrl;

    public String sendChatCompletionRequest(String userMessageString,List<Embedding> embeddingList) throws Exception {

        ChatCompletionRequest requestBody = getChatCompletionRequest(userMessageString,embeddingList,GPT3Point5);

        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(gptCompletionsApiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        new ObjectMapper().writeValueAsString(requestBody)
                ))
                .build();

        // Send HTTP request and handle response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check response status code
        int statusCode = response.statusCode();
        log.info("API response status code: " + statusCode);

        // Check response body
        String responseBody = response.body();
        log.info("API response body: " + responseBody);

        // Return API response body (you can further process or parse this response as needed)
        return responseBody;
    }

    private static ChatCompletionRequest getChatCompletionRequest(String userMessageStr, List<Embedding> embeddingList, String model) {
        ChatCompletionRequest requestBody = new ChatCompletionRequest();
        requestBody.setModel(model);
        AtomicReference<String> EmbeddingData = new AtomicReference<>("");

        embeddingList.parallelStream().forEach(embedding -> {
            EmbeddingData.updateAndGet(currentStr->currentStr+"\n"+ embedding.getDataString());
        });

        Message systemmessage = new Message("system", EmbeddingData.get());
        Message userMessage = new Message("user", userMessageStr);

        List<Message> messages = List.of(
                systemmessage,userMessage
        );
        requestBody.setMessages(messages);
        return requestBody;
    }
}
