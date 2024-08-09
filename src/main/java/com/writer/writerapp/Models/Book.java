package com.writer.writerapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    @Field("user_id")
    private String userId;

    @Field("book_name")
    private String bookName;

    @Field("author")
    private String author;

    @Field("chapter_count")
    private int chapterCount =0;

    @Field("chapters")
    private List<String> chapters = new ArrayList<>();

    @Field("created_date")
    private Date createdDate;

    @Field("last_modified")
    private Date lastModified;

    @Field("last_analyzed")
    private Date lastAnalyzed;

    @Field("dynamic_database_schemas")
    private List<DynamicDbSchema> dynamicDbSchemaList;

    // Add DynamicDbSchema
    public void addDynamicDbSchema(DynamicDbSchema schema) {
        if (dynamicDbSchemaList == null) {
            dynamicDbSchemaList = new ArrayList<>();
        }
        dynamicDbSchemaList.add(schema);
    }

    // Update DynamicDbSchema
    public void updateDynamicDbSchema(String schemaId, DynamicDbSchema updatedSchema) {
        if (dynamicDbSchemaList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schema list is empty");
        }
        for (int i = 0; i < dynamicDbSchemaList.size(); i++) {
            if (dynamicDbSchemaList.get(i).getId().equals(schemaId)) {
                dynamicDbSchemaList.set(i, updatedSchema);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schema not found");
    }

    // Delete DynamicDbSchema
    public void deleteDynamicDbSchema(String schemaId) {
        if (dynamicDbSchemaList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schema list is empty");
        }
        dynamicDbSchemaList.removeIf(schema -> schema.getId().equals(schemaId));
    }
}
