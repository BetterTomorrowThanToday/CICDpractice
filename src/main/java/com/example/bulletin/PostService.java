package com.example.bulletin;

//import com.example.bulletin.Post;
//import com.example.bulletin.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 모든 게시글 가져오기
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 ID로 조회하기
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
}