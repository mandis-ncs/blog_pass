package br.com.compass.pb.blogpass.dto;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "post-feign-client", url = "https://jsonplaceholder.typicode.com/posts")
public interface PostClient {

    @GetMapping
    List<Post> getAllPosts();

    @GetMapping("/{postId}")
    Post getPostById(@PathVariable("postId") Long postId);

    @GetMapping("/{postId}/comments")
    List<Comment> getCommentsByPostId(@PathVariable("postId") Long postId);

}
