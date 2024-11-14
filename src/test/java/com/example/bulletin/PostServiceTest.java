package com.example.bulletin;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private final PostRepository postRepository = mock(PostRepository.class);
    private final PostService postService = new PostService(postRepository);

    @Test
    void testGetAllPosts() throws Exception {
        // Arrange
        Post post1 = new Post("Title 1", "Author 1", "Content 1");
        Post post2 = new Post("Title 2", "Author 1", "Content 2");
        List<Post> mockPosts = Arrays.asList(post1, post2);

        when(postRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<Post> posts = postService.getAllPosts();

        // Assert
        assertEquals(2, posts.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testGetPostById() throws Exception {
        // Arrange
        Long postId = 1L;
        Post mockPost = new Post(postId, "Title", "Author", "Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        // Act
        Optional<Post> post = postService.getPostById(postId);

        // Assert
        assertTrue(post.isPresent());
        assertEquals(postId, post.get().getId());
        assertEquals("Title", post.get().getTitle());
        verify(postRepository, times(1)).findById(postId);
    }
}