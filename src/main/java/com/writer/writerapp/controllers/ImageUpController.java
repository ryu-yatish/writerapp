package com.writer.writerapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imageupload")
@RequiredArgsConstructor
public class ImageUpController {
    @PostMapping("/{imgName}")
    @ResponseStatus(HttpStatus.OK)
    public String askChatGpt(@PathVariable String imgName) throws Exception {
        return "{ success: true, imageUrl: '/path/to/your/uploads/folder/' + filename }";
    }
}
