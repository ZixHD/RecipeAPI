package com.example.MobileAppBackend.dto.model;

import com.example.MobileAppBackend.model.Comment;

import com.example.MobileAppBackend.model.PostRecipe;
import com.example.MobileAppBackend.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithRecipe {

    private PostRecipe post;
    private List<Comment> comments = new ArrayList<>();
}
