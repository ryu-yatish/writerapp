package com.writer.writerapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties
public class DynamicDbSchema {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("icon")
    private String icon;

    @Field("properties_map")
    private Map<String,DDSProperty> propertiesMap;
}
