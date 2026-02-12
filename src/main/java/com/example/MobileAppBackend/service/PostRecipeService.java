package com.example.MobileAppBackend.service;

import com.example.MobileAppBackend.dto.create.CreatePostRequest;
import com.example.MobileAppBackend.dto.model.FilterRequest;
import com.example.MobileAppBackend.dto.model.PostWithRecipe;
import com.example.MobileAppBackend.dto.model.RecipeDto;
import com.example.MobileAppBackend.model.*;
import com.example.MobileAppBackend.repository.CommentRepository;
import com.example.MobileAppBackend.repository.PostRecipeRepository;
import com.example.MobileAppBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostRecipeService {

    private final PostRecipeRepository postRecipeRepository;

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;


    public List<Map<String, Object>> getSpecificRecipesExclude(String exclude) {
        Set<String> excludedFields = exclude != null
                ? Arrays.stream(exclude.split(","))
                .map(String::trim)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        List<PostRecipe> postRecipes = postRecipeRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();

        return postRecipes.stream()
                .map(recipe -> {
                    Map<String, Object> map = mapper.convertValue(recipe, new TypeReference<Map<String, Object>>() {});
                    excludedFields.forEach(map::remove); return map; })
                .collect(Collectors.toList()); }


    public List<Map<String, Object>> getSpecificRecipesInclude(String include) {

        Set<String> includedFields = include != null
                ? Arrays.stream(include.split(","))
                .map(String::trim)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        List<PostRecipe> postRecipes = postRecipeRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();

        return postRecipes.stream()
                .map(recipe -> {
                    Map<String, Object> map =
                            mapper.convertValue(recipe, new TypeReference<Map<String, Object>>() {});

                    if (includedFields.isEmpty()) {
                        return map;
                    }

                    map.keySet().retainAll(includedFields);
                    return map;
                })
                .collect(Collectors.toList());
    }


    public List<RecipeDto> getAllRecipes() {

        return postRecipeRepository.findAll()
                .stream()
                .map(this::getRecipeData)
                .collect(Collectors.toList());

    }
    public List<PostRecipe> getAllPosts(){
        return this.postRecipeRepository.findAll();
    }

    public PostRecipe getById(String id){
        PostRecipe post = postRecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        PostRecipe postRecipe = postRecipeRepository.findPostById(id);
        if (postRecipe == null) {
            log.warn("No comments found for this post");
            throw new RuntimeException("No post found for this post.");
        }
        return postRecipe;

    }

    public List<PostRecipe> filterPosts(FilterRequest filterRequest){

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (filterRequest.getTags() != null && !filterRequest.getTags().isEmpty()) {
            for (String tag : filterRequest.getTags()) {
                criteriaList.add(Criteria.where("tags").is(tag));
            }
        }
        if(filterRequest.getAllergies() != null && !filterRequest.getAllergies().isEmpty()){
            criteriaList.add(Criteria.where("allergies").nin(filterRequest.getAllergies()));
        }
        if(filterRequest.getDifficulty() != null && !filterRequest.getDifficulty().isEmpty()){
            criteriaList.add(Criteria.where("difficulty").in(filterRequest.getDifficulty()));
        }

        if(filterRequest.getCuisine() != null && !filterRequest.getCuisine().isEmpty()){
            criteriaList.add(Criteria.where("cuisine").in(filterRequest.getCuisine()));
        }

        if(filterRequest.getMinCalories() != null || filterRequest.getMaxCalories() != null){
            Criteria calories = Criteria.where("calories");
            if(filterRequest.getMinCalories() != null){
                calories = calories.gte(filterRequest.getMinCalories());
            }
            if(filterRequest.getMaxCalories() != null){
                calories = calories.lte(filterRequest.getMaxCalories());
            }
            criteriaList.add(calories);
        }

        if(filterRequest.getMinPrepTime() != null || filterRequest.getMaxPrepTime() != null){
            Criteria prep = Criteria.where("prep_time");
            if(filterRequest.getMinPrepTime() != null){
                prep = prep.gte(filterRequest.getMinPrepTime());
            }
            if(filterRequest.getMaxPrepTime() != null){
                prep = prep.lte(filterRequest.getMaxPrepTime());
            }
            criteriaList.add(prep);
        }

        if(!criteriaList.isEmpty()){
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, PostRecipe.class);

    }

    public List<RecipeDto> filterRecipes(FilterRequest filterRequest){
        return filterPosts(filterRequest)
                .stream()
                .map(this::getRecipeData)
                .collect(Collectors.toList());
    }

    public void toggleFavorite(String id){
        User user  = userRepository.findById(getCurrentUserId())
                .orElseThrow(() ->{
                    log.warn("User not found");
                    return new RuntimeException("User not found");
                });
        postRecipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found");
                    return new RuntimeException("Post not found");
                });

        if (user.getFavorites().contains(id)) {
            user.getFavorites().remove(id);
        } else {
            user.getFavorites().add(id);
        }

        userRepository.save(user);
    }

    public boolean isFavorited(String postId) {
        return userRepository.findById(getCurrentUserId())
                .map(user -> user.getFavorites()   // assuming favorites is a List<String> or Set<String>
                        .contains(postId))
                .orElse(false);
    }

    public PostRecipe createPost(CreatePostRequest createPostRequest){
        System.out.println("Hit");

        PostRecipe post = modelMapper.map(createPostRequest, PostRecipe.class);

        log.debug("Created post body {}", post);
        log.info("Post created successfully");
        return this.postRecipeRepository.save(post);
    }

    public PostRecipe editPost(String id, CreatePostRequest req) {

        PostRecipe existingPost = postRecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!existingPost.getAuthorId().equals(getCurrentUserId())) {
            log.warn("You are not allowed to edit this post");
            throw new RuntimeException("You are not allowed to edit this post");
        }

        Optional.ofNullable(req.getAuthorId()).ifPresent(existingPost::setAuthorId);
        Optional.ofNullable(req.getText()).ifPresent(existingPost::setText);
        Optional.ofNullable(req.getTitle()).ifPresent(existingPost::setTitle);
        Optional.ofNullable(req.getDescription()).ifPresent(existingPost::setDescription);
        Optional.ofNullable(req.getCuisine()).ifPresent(existingPost::setCuisine);
        Optional.ofNullable(req.getDifficulty()).ifPresent(existingPost::setDifficulty);

        log.debug("Post body {}", existingPost);
        log.info("Post updated successfully");

        return postRecipeRepository.save(existingPost);
    }


    public void deletePost(String id){
        Optional<PostRecipe> optionalPost = this.postRecipeRepository.findById(id);
        PostRecipe post = optionalPost.get();
        if(!post.getAuthorId().equals(getCurrentUserId())) {
            throw new RuntimeException("You are not allowed to remove this post");
        }
        optionalPost.ifPresent(postRecipeRepository::delete);
        log.info("Post deleted successfully");
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }


    private RecipeDto getRecipeData(PostRecipe postRecipe) {
        RecipeDto dto = new RecipeDto();
        dto.setTitle(postRecipe.getTitle());
        dto.setAuthorId(postRecipe.getAuthorId());
        dto.setDescription(postRecipe.getDescription());
        dto.setIngredients(postRecipe.getIngredients());
        dto.setSteps(postRecipe.getSteps());
        dto.setTags(postRecipe.getTags());
        dto.setCuisine(postRecipe.getCuisine());
        dto.setAllergies(postRecipe.getAllergies());
        dto.setDifficulty(postRecipe.getDifficulty());
        dto.setPrep_time(postRecipe.getPrep_time());
        dto.setCalories(postRecipe.getCalories());

        return dto;
    }

}
