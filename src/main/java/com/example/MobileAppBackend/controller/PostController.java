package com.example.MobileAppBackend.controller;


import com.example.MobileAppBackend.dto.create.CreatePostRequest;
import com.example.MobileAppBackend.dto.model.FilterRequest;
import com.example.MobileAppBackend.dto.model.PostWithRecipe;
import com.example.MobileAppBackend.model.PostRecipe;
import com.example.MobileAppBackend.service.PostRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRecipeService postRecipeService;

    @GetMapping
    public ResponseEntity<List<PostRecipe>> getAllPosts(){
        List<PostRecipe> posts =  postRecipeService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRecipe> getPostById(@PathVariable String id){
        PostRecipe post = postRecipeService.getById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PostRecipe>> filterPosts(@RequestBody FilterRequest filterRequest){
        return ResponseEntity.ok(postRecipeService.filterPosts(filterRequest));
    }

    @PutMapping("/favorite/{id}")
    public ResponseEntity<PostRecipe> favoritePost(@PathVariable String id){
        this.postRecipeService.toggleFavorite(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<PostRecipe> createPost(@Valid @RequestBody CreatePostRequest createPostRequest){
        System.out.println("Create Post: " + createPostRequest);
        PostRecipe post = postRecipeService.createPost(createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<PostRecipe> editPost(@PathVariable String id,  @Valid @RequestBody CreatePostRequest createPostRequest){
        PostRecipe updated = postRecipeService.editPost(id, createPostRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostRecipe> deletePost(@PathVariable String id){
        this.postRecipeService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
