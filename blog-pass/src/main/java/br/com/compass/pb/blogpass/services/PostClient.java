package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.CommentDto;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "post-feign-client", url = "https://jsonplaceholder.typicode.com")
public interface PostClient {

    @GetMapping("/posts")
    List<Post> getAllPosts();

    @GetMapping("/posts/{postId}")
    Post getPostById(@PathVariable("postId") Long postId);

    @GetMapping("/posts/{postId}/comments")
    List<CommentDto> getCommentsByPostId(@PathVariable("postId") Long postId);

}
