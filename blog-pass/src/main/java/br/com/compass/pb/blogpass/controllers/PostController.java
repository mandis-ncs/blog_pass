package br.com.compass.pb.blogpass.controllers;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
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


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }


    @GetMapping
    public ResponseEntity<List<PostResponseDto>> listAllPosts() {
        List<PostResponseDto> posts = postService.getAllPostsFromExternalService();
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/{postId}")
    public ResponseEntity<String> processPost(@PathVariable Long postId) {
        postService.processPost(postId);
        return ResponseEntity.ok("Post processing initiated");
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
