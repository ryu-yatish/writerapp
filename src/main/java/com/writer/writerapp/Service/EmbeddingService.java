package com.writer.writerapp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.writer.writerapp.Models.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingService {
    private final ApiService apiService;
    private final ObjectMapper objectMapper;
    public String askGpt(String userMessageStr) throws Exception {
        String responseString = apiService.sendChatCompletionRequest(userMessageStr, List.of());
        GptResponse gptResponse = objectMapper.readValue(responseString, GptResponse.class);

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
