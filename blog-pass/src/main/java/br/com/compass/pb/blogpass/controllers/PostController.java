package br.com.compass.pb.blogpass.controllers;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/{postId}")
    public void processPost(@PathVariable Long postId) {
        postService.processPost(postId);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<String> reprocessPost(@PathVariable Long postId) {
        postService.reprocessPost(postId);
        return ResponseEntity.ok("Post reprocessing initiated.");
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<String> disablePost(@PathVariable Long postId) {
        postService.disablePost(postId);
        return ResponseEntity.ok("Post disabled.");
    }


    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }



}
