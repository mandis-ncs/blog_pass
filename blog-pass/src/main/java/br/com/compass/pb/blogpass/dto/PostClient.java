package br.com.compass.pb.blogpass.dto;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "post-feign-client", url = "https://jsonplaceholder.typicode.com")
public interface PostClient {

    @GetMapping("/posts")
    List<PostResponseDto> getAllPosts();
}
