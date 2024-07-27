package com.writer.writerapp.Models.RequestVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties
public class DDSPropertyRequest {

    private String type;
    private String regex;
    private boolean required;
    private String defaultValue;
    private String description;
    private Integer minLength;
    private Integer maxLength;
    private Number minValue;
    private Number maxValue;
    private List<String> allowedValues;
    private Map<String, Object> customAttributes;
}
