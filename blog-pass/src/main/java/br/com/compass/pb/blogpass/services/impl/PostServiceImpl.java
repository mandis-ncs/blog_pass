package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.PostClient;
import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.services.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostClient postClient;

    public PostServiceImpl(PostClient postClient) {
        this.postClient = postClient;
    }

    public List<PostResponseDto> getAllPostsFromExternalService() {
        return postClient.getAllPosts();
    }
}
