package com.writer.writerapp.Models;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;
}
