package com.writer.writerapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document("Book")
@JsonIgnoreProperties
public class Book {
    @Id
    private String id;

    @Field("book_name")
    private String bookName;

    @Field("author")
    private String author;

    @Field("chapter_count")
    private int chapterCount =0;

    @Field("chapters")
    private List<String> chapters;

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;

    @Field("embedding_ids")
    private String[] embeddingIds;

}
