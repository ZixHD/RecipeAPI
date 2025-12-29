package com.example.MobileAppBackend.repository;

import com.example.MobileAppBackend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findCommentsByAuthorId(String author_id);
    List<Comment> findCommentsByPostId(String post_id);

}
