package com.writer.writerapp.Models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Document("Embedding")
public class Embedding {
    @Id
    private String id;

    @Field("vector")
    private double[] vector;

    @Field("data_string")
    private String dataString;

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;

    @Field("data_type")
    private String dataType;

    @Field("chapter_id")
    private String chapterId;

    @Field("book_id")
    private String bookId;


}
