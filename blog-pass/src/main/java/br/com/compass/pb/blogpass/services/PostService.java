package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.PostDto;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.enums.PostStatus;
import br.com.compass.pb.blogpass.entities.StatusHistory;

import java.util.List;
import java.util.Optional;

public interface PostService {

    StatusHistory saveStatusHistory(PostStatus status, Post post);

    StatusHistory getMostRecentStatus(List<StatusHistory> historyList, Long postId);

    void validatePostIdBetween1And100(Long postId);

    Optional<Post> findExistentPostById(Long postId);

    void processPost(Long postId);

    Post populatePostById(Long postId);

    Post populateCommentByPostId(Long postId);

    void reprocessPost(Long postId);

    void disablePost(Long postId);

    List<Post> returnAllPostsFromAPI();

    List<PostDto> getAllPostsFromBD();
}
