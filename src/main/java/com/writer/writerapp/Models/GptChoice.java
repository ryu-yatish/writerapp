package com.writer.writerapp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GptChoice {

    @JsonProperty("index")
    private int index;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("logprobs")
    private Object logprobs; // This can be another custom class representing logprobs

    @JsonProperty("finish_reason")
    private String finishReason;
}
