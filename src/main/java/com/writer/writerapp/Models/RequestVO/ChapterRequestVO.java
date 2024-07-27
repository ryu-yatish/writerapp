package com.writer.writerapp.Models.RequestVO;

import com.writer.writerapp.Models.Chapter;
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
public class ChapterRequestVO {
    @Field("title")
    private String title;

    @Field("content")
    private String content;

}
