package com.writer.writerapp.Models.ResponseVO;

import com.writer.writerapp.Models.Chapter;
import com.writer.writerapp.Models.DynamicDbSchema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BookResponseVO {
    @Field("id")
    private String id;

    @Field("book_name")
    private String bookName;

    @Field("author")
    private String author;

    @Field("chapter_count")
    private int chapterCount =0;

    @Field("chapters")
    private List<Chapter> chapters;

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;

    @Field("last_analyzed")
    private Date lastAnalyzed;

    @Field("dynamic_database_schemas")
    private List<DynamicDbSchema> dynamicDbSchemaList;

}
