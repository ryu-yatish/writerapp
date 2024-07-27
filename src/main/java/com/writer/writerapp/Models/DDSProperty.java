package com.writer.writerapp.Models;

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
public class DDSProperty {

    // The type of the field (e.g., String, Integer, Boolean, etc.)
    private String type;

    // A regular expression that the field's value must match
    private String regex;

    // Whether the field is required
    private boolean required;

    // The default value for the field
    private String defaultValue;

    // A description of the field
    private String description;

    // Minimum length for String fields
    private Integer minLength;

    // Maximum length for String fields
    private Integer maxLength;

    // Minimum value for numerical fields
    private Number minValue;

    // Maximum value for numerical fields
    private Number maxValue;

    // Allowed values (for enum-like behavior)
    private List<String> allowedValues;

    // Custom attributes can be added if needed
    private Map<String, Object> customAttributes;
}