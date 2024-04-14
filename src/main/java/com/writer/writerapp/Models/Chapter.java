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
@Document("Chapter")
public class Chapter {
    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;

}
