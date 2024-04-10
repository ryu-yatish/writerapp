package com.writer.writerapp.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document("Book")
public class Book {
    @Field("id")
    @Id
    private int id;

    @Field("book_name")
    private String bookName;
}
