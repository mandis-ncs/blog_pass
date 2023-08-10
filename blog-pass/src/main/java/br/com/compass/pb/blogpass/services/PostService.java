package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.entities.Post;

import java.util.List;

public interface PostService {

    Post getPostById(Long postId);

    void processPost(Long postId);

    void reprocessPost(Long postId);

    void disablePost(Long postId);

    List<Post> returnAllPostsFromAPI();

    List<PostResponseDto> getAllPostsFromBD();
}
