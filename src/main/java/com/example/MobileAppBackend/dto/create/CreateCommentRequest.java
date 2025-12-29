package com.example.MobileAppBackend.dto.create;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ToString
@Slf4j
public class CreateCommentRequest {

    private String id;

    @NotBlank(message = "Comment must have an author id.")
    private String authorId;

    @NotBlank(message = "Comment must have a post id.")
    private String postId;

    @NotBlank(message = "Comment must have text.")
    @Size(min = 2, max = 100, message = "Comment must be between 2 and 50 characters long.")
    private String text;
    private int views;
    private LocalDateTime created_at;



}
