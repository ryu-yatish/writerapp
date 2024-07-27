package com.writer.writerapp.Models.RequestVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties
public class DynamicDbSchemaRequest {

    private String name;
    private String icon;
    private Map<String, DDSPropertyRequest> propertiesMap;
}

