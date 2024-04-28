package com.writer.writerapp.Models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Message {
    private String role;
    private String content;
}