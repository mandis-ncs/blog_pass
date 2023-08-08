package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.PostClient;
import br.com.compass.pb.blogpass.dto.response.PostResponseDto;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.exceptions.DuplicatedPostException;
import br.com.compass.pb.blogpass.exceptions.InvalidPostException;
import br.com.compass.pb.blogpass.exceptions.ResourceNotFoundException;
import br.com.compass.pb.blogpass.repositories.PostRepository;
import br.com.compass.pb.blogpass.services.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostClient postClient;
    private final PostRepository postRepository;

    public PostServiceImpl(PostClient postClient, PostRepository postRepository) {
        this.postClient = postClient;
        this.postRepository = postRepository;
    }


    @Override
    public PostResponseDto getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        // map to response
        PostResponseDto response = null;
        return response;
//                .orElseThrow(
//                () -> new ResourceNotFoundException("Could not found any post with this id: " + postId));
    }

    public List<PostResponseDto> getAllPostsFromExternalService() {
        return postClient.getAllPosts();
//        - **`title`**: Can be null or empty depending on the state.
//        - **`body`**: Can be null or empty depending on the state.
//        - **`comments`**: Can be null or empty depending on the state.
//        - **`history`**: Cannot be null or empty; it must always have a value.
        // CAN RETRIEVE DISABLE POSTS TOO?
    }

    @Override
    public void processPost(Long postId) {

        if (postId < 1 || postId > 100) {
            throw new InvalidPostException("Post id invalid. Try a post between id 1 and 100.");
        }

        if (postRepository.findById(postId) != null) {
            throw new DuplicatedPostException("This post was already published!");
        }

        // Pass id in request, map to post and return as a response to the controller
        // post.setStatus(PostStatus.PROCESSING);
        // post.getHistory().add(new StatusHistory(...));
        // postRepository.save(post);

        return;
    }

    @Override
    public void reprocessPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found any post with this id: " + postId));

        if (postId < 1 || postId > 100) {
            throw new InvalidPostException("Post id invalid. Try a post between id 1 and 100.");
        }

        // if (post.getStatus() != PostStatus.ENABLED || post.getStatus() != PostStatus.DISABLED) {
        //      throw new InvalidPostException("Post is not ABLE to update.");
        // }
        // WHEN DISABLE SHOULD CHANGE TO ENABLE STATUS AND PROCESS AGAIN?
        //     Perform reprocessing actions
        //     post.setStatus(PostStatus.UPDATING);
        //     post.getHistory().add(new StatusHistory(...));
        //     postRepository.save(post);
        return;
    }

    @Override
    public void disablePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found any post with this id: " + postId));

        /* Check if the post is in "ENABLED" state
        if (post.getStatus() == PostStatus.ENABLED) {
            // Soft delete by changing the status to "DISABLED"
            post.setStatus(PostStatus.DISABLED);
            post.getHistory().add(new StatusHistory(...)); // Add history entry for the status change
            postRepository.save(post);
        }*/
    }
}
