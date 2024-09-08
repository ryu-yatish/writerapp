package com.writer.writerapp.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.writer.writerapp.Models.ChatCompletionRequest;
import com.writer.writerapp.Models.Embedding;
import com.writer.writerapp.Models.Message;
import com.writer.writerapp.Models.ResponseVO.EmbeddingResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiService {
    @Value("${open.ai.api.key}")
    private String openaiApiKey;
    @Value("${gpt.three.five.model.name}")
    private String GPT3Point5;
    @Value("${gpt.completions.url}")
    private String gptBaseApiUrl;

    public String sendChatCompletionRequest(String userMessageString,List<EmbeddingResponseVO> embeddingList) throws Exception {

        ChatCompletionRequest requestBody = getChatCompletionRequest(userMessageString,embeddingList,GPT3Point5);

        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(gptBaseApiUrl+"/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        new ObjectMapper().writeValueAsString(requestBody)
                ))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        log.info("API response status code: " + statusCode);

        String responseBody = response.body();
        log.info("API response body: " + responseBody);

        return responseBody;
    }

    private ChatCompletionRequest getChatCompletionRequest(String userMessageStr, List<EmbeddingResponseVO> embeddingList, String model) {
        ChatCompletionRequest requestBody = new ChatCompletionRequest();
        requestBody.setModel(model);
        AtomicReference<String> EmbeddingData = new AtomicReference<>("");

        embeddingList.parallelStream().forEach(embedding -> {
            EmbeddingData.updateAndGet(currentStr->currentStr+"\n"+ embedding.getDataString());
        });
        String systemMessageString = "this is an api to analyze and answer questions from a book writing app. given below with system roles are select paragraphs selected using embeddings. based on that data in it please answer the following questions and whenever possible provide information that might inspire new ideas and provide more information helpful in writing the book further";
        Message systemMessage = new Message("system", systemMessageString);
        Message embeddingMessage = new Message("system", EmbeddingData.get());
        Message userMessage = new Message("user", userMessageStr);

        List<Message> messages = List.of(
                systemMessage,embeddingMessage,userMessage
        );
        requestBody.setMessages(messages);
        return requestBody;
    }
    public List<Double> getEmbeddings(String inputText) throws JsonProcessingException {
        String apiUrl = gptBaseApiUrl + "/v1/embeddings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("input", inputText);
        requestBodyMap.put("model", "text-embedding-3-small");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println("requestbody: " + requestEntity);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        log.info("response from embeddings api{}",response);
        return getEmbeddingValues(response);
    }

    private static List<Double> getEmbeddingValues(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            if (rootNode.has("data") && rootNode.get("data").isArray()) {
                JsonNode embeddingsNode = rootNode.get("data").get(0).get("embedding");
                if (embeddingsNode.isArray()) {
                    List<Double> embeddings = new ArrayList<>();
                    for (JsonNode valueNode : embeddingsNode) {
                        embeddings.add(valueNode.asDouble());
                    }
                    return embeddings;
                }
            }
        } catch (Exception e) {
            log.error("[ApiService.getEmbeddings()] Error While getting embeddings:",e);
        }
        return null;
    }
}
