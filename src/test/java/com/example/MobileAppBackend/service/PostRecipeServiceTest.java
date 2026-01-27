package com.example.MobileAppBackend.service;

import com.example.MobileAppBackend.dto.create.CreatePostRequest;
import com.example.MobileAppBackend.dto.model.FilterRequest;
import com.example.MobileAppBackend.model.*;
import com.example.MobileAppBackend.repository.CommentRepository;
import com.example.MobileAppBackend.repository.PostRecipeRepository;
import com.example.MobileAppBackend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostRecipeServiceTest {

    @Mock
    private PostRecipeRepository postRecipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PostRecipeService postRecipeService;

    // ---------------- helpers ----------------

    private void mockAuthenticatedUser(String userId) {
        User user = new User();
        user.setId(userId);

        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);

        when(auth.getPrincipal()).thenReturn(user);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getAllPosts_returnsAllPosts() {
        when(postRecipeRepository.findAll()).thenReturn(List.of(new PostRecipe(), new PostRecipe()));

        List<PostRecipe> result = postRecipeService.getAllPosts();

        assertEquals(2, result.size());
        verify(postRecipeRepository).findAll();
    }


    @Test
    void getById_returnsPostWithComments() {
        PostRecipe post = new PostRecipe();
        post.setId("p1");

        Comment c1 = new Comment();
        Comment c2 = new Comment();

        when(postRecipeRepository.findById("p1")).thenReturn(Optional.of(post));
        when(commentRepository.findCommentsByPostId("p1")).thenReturn(List.of(c1, c2));

        var result = postRecipeService.getById("p1");

        assertEquals(post, result.getPost());
        assertEquals(2, result.getComments().size());
    }


    @Test
    void toggleFavorite_addsFavorite_whenNotPresent() {
        mockAuthenticatedUser("u1");

        User user = new User();
        user.setId("u1");
        user.setFavorites(new HashSet<>());

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(postRecipeRepository.findById("p1")).thenReturn(Optional.of(new PostRecipe()));

        postRecipeService.toggleFavorite("p1");

        assertTrue(user.getFavorites().contains("p1"));
        verify(userRepository).save(user);
    }

    @Test
    void toggleFavorite_removesFavorite_whenPresent() {
        mockAuthenticatedUser("u1");

        User user = new User();
        user.setId("u1");
        user.setFavorites(new HashSet<>());


        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(postRecipeRepository.findById("p1")).thenReturn(Optional.of(new PostRecipe()));

        postRecipeService.toggleFavorite("p1");

        assertFalse(user.getFavorites().contains("p1"));
        verify(userRepository).save(user);
    }


    @Test
    void createPost_savesMappedPost() {
        CreatePostRequest req = new CreatePostRequest();
        req.setRatings(new ArrayList<>());

        PostRecipe mapped = new PostRecipe();

        when(modelMapper.map(req, PostRecipe.class)).thenReturn(mapped);
        when(postRecipeRepository.save(mapped)).thenReturn(mapped);

        PostRecipe result = postRecipeService.createPost(req);

        assertNotNull(result);
        verify(postRecipeRepository).save(mapped);
    }

    @Test
    void deletePost_deletes_whenAuthorMatches() {
        mockAuthenticatedUser("u1");

        PostRecipe post = new PostRecipe();
        post.setAuthorId("u1");

        when(postRecipeRepository.findById("p1")).thenReturn(Optional.of(post));

        postRecipeService.deletePost("p1");

        verify(postRecipeRepository).delete(post);
    }

    @Test
    void deletePost_throws_whenNotAuthor() {
        mockAuthenticatedUser("u1");

        PostRecipe post = new PostRecipe();
        post.setAuthorId("u2");

        when(postRecipeRepository.findById("p1")).thenReturn(Optional.of(post));

        assertThrows(RuntimeException.class,
                () -> postRecipeService.deletePost("p1"));
    }
    

    @Test
    void filterPosts_usesMongoTemplate() {
        FilterRequest filter = new FilterRequest();

        when(mongoTemplate.find(any(), eq(PostRecipe.class)))
                .thenReturn(List.of(new PostRecipe()));

        List<PostRecipe> result = postRecipeService.filterPosts(filter);

        assertEquals(1, result.size());
        verify(mongoTemplate).find(any(), eq(PostRecipe.class));
    }
}