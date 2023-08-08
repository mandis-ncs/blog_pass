package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getAllPostsFromExternalService();

}
