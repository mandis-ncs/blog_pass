package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {

    PostResponseDto getPostById(Long postId);

    List<PostResponseDto> getAllPostsFromExternalService();

    void processPost(Long postId);

    void reprocessPost(Long postId);

    void disablePost(Long postId);

}
