package com.example.bulletin;

import com.example.bulletin.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 목록을 보여주는 메서드
    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts); // "posts" 속성에 게시글 목록을 추가
        return "index"; // "index.html" 템플릿을 반환
    }

    // 게시글 상세보기 페이지
    @GetMapping("/posts/{id}")
    public String showPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElse(null);
        model.addAttribute("post", post); // "post" 속성에 게시글을 추가
        return "post-detail"; // "post-detail.html" 템플릿을 반환
    }
}