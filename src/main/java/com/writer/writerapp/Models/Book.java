package com.writer.writerapp.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document("Book")
public class Book {
    @Id
    private String id;

    @Field("book_name")
    private String bookName;

    @Field("author")
    private String author;

    @Field("chapters")
    private int chapters =0;

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;
}
