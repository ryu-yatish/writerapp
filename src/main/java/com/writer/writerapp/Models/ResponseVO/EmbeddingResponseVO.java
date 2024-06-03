package com.writer.writerapp.Models.ResponseVO;

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
public class EmbeddingResponseVO {



    @Field("data_string")
    private String dataString;

    @Field("similarity")
    private double similarity;

}
