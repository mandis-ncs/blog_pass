package br.com.compass.pb.blogpass.controllers;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPostsFromExternalService();
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }



}
