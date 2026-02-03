package com.example.MobileAppBackend.controller;

import com.example.MobileAppBackend.dto.create.CreateCommentRequest;
import com.example.MobileAppBackend.model.Comment;
import com.example.MobileAppBackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/user/{author_id}")
    public ResponseEntity<List<Comment>> getAllUserComments(@PathVariable String author_id) {
        return ResponseEntity.ok(commentService.getAllUserComments(author_id));
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<List<Comment>> getAllPostComments(@PathVariable String post_id) {
        return ResponseEntity.ok(commentService.getAllPostComments(post_id));
    }

    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return ResponseEntity.ok(commentService.createComment(createCommentRequest));

    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Comment> editComment(@PathVariable String id,  @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        return ResponseEntity.ok(commentService.editComment(id, createCommentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable String id) {
        this.commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

}
