package com.example.bulletin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService; // 가짜 서비스 빈 주입

    // 게시글 목록 테스트
    @Test
    void testIndex() throws Exception {
        // 가짜 데이터 설정
        Post post1 = new Post("Title 1", "Author 1", "Content 1");
        Post post2 = new Post("Title 2", "Author 1", "Content 2");
        given(postService.getAllPosts()).willReturn(Arrays.asList(post1, post2));

        // 요청 및 검증
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) // HTTP 200 상태 확인
                .andExpect(view().name("index")) // 반환된 뷰 이름 확인
                .andExpect(model().attribute("posts", hasSize(2))) // 모델 속성 확인
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("title", is("Title 1")),
                                hasProperty("author", is("Author 1")),
                                hasProperty("content", is("Content 1"))
                        )
                )));
    }

    // 게시글 상세보기 테스트
    @Test
    void testShowPost() throws Exception {
        // 가짜 데이터 설정
        Post post = new Post(1L, "Title 1", "Author 1", "Content 1");
        given(postService.getPostById(1L)).willReturn(Optional.of(post));

        // 요청 및 검증
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk()) // HTTP 200 상태 확인
                .andExpect(view().name("post-detail")) // 반환된 뷰 이름 확인
                .andExpect(model().attribute("post", hasProperty("id", is(1L))))
                .andExpect(model().attribute("post", hasProperty("title", is("Title 1"))))
                .andExpect(model().attribute("post", hasProperty("author", is("Author 1"))))
                .andExpect(model().attribute("post", hasProperty("content", is("Content 1"))));
    }

    // 게시글이 없을 때 테스트
    @Test
    void testShowPost_NotFound() throws Exception {
        given(postService.getPostById(4L)).willReturn(Optional.empty());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk()) // HTTP 200 상태 (뷰 로드 확인)
                .andExpect(view().name("post-detail")) // 반환된 뷰 이름 확인
                .andExpect(model().attribute("post", nullValue())); // "post" 속성이 null인지 확인
    }
}