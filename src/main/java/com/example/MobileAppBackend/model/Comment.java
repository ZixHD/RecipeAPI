package com.example.MobileAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

    @Id
    private String id;

    private String authorId;
    private String postId;
    private String text;
    private LocalDateTime created_at;
}
