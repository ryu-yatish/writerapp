package com.writer.writerapp.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document("DynamicDbObject")
public class DynamicDbObject extends BaseEntity {
    @Id
    private String id;

    @Field("schema_id")
    @Indexed
    private String schemaId;

    @Field("data")
    private Map<String,String> data;
}
