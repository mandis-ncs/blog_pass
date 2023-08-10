package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.PostClient;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.entities.PostStatus;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import br.com.compass.pb.blogpass.exceptions.DuplicatedPostException;
import br.com.compass.pb.blogpass.exceptions.InvalidPostException;
import br.com.compass.pb.blogpass.exceptions.ResourceNotFoundException;
import br.com.compass.pb.blogpass.repositories.CommentsRepository;
import br.com.compass.pb.blogpass.repositories.HistoryRepository;
import br.com.compass.pb.blogpass.repositories.PostRepository;
import br.com.compass.pb.blogpass.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostClient postClient;
    private final PostRepository postRepository;
    private final HistoryRepository historyRepository;
    private final CommentsRepository commentsRepository;

    public PostServiceImpl(PostClient postClient, PostRepository postRepository, HistoryRepository historyRepository,
                           CommentsRepository commentsRepository) {
        this.postClient = postClient;
        this.postRepository = postRepository;
        this.historyRepository = historyRepository;
        this.commentsRepository = commentsRepository;
    }


    @Async
    public Post getPostById(Long postId) {
        return postClient.getPostById(postId);
    }


    @Override
    @Async
    public List<Post> getAllPostsFromExternalService() {
        return postClient.getAllPosts();
    }


    private StatusHistory saveStatusHistory(PostStatus status, Post post) {
        StatusHistory history = new StatusHistory(LocalDateTime.now(), status, post);
        return historyRepository.save(history);
    }


    @Override
    @Transactional
    @Async
    public void processPost(Long postId) {
        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }

        if (postRepository.findById(postId).isPresent()) {
            throw new DuplicatedPostException("This post if id already exists: " + postId);
        }

        Post post = new Post();
        post.setId(postId);
        postRepository.save(post);

        // status history CREATED
        StatusHistory createdPost = saveStatusHistory(PostStatus.CREATED, post);
        historyRepository.save(createdPost);

        // JUST FOR TEST -> CALLING FindPostById Method
        log.info("calling POST FINDING history");
        Post populatedPost = populatePostById(post);

        log.info("calling populate COMMENTS");
        populateCommentByPostId(populatedPost);
    }



    public Post populatePostById(Post post) {

        // status history FIND
        saveStatusHistory(PostStatus.POST_FIND, post);

        log.info("setting POST FIND history");

        post = postClient.getPostById(post.getId());

        if (post.getTitle().isEmpty() || post.getBody().isEmpty()) {

            log.info("saving history FAILED");
            saveStatusHistory(PostStatus.FAILED, post);

            disablePost(post.getId());
            throw new ResourceNotFoundException("Post title or body is empty. Status: FAILED. Disabling post.");
        }

        saveStatusHistory(PostStatus.POST_OK, post);

        log.info("returning");
        return postRepository.save(post);
    }


    public Post populateCommentByPostId(Post post) {

        saveStatusHistory(PostStatus.COMMENTS_FIND, post);
        log.info("saving history COMMENTS_FIND");

        List<Comment> arrayComments = new ArrayList<>();
        List<Comment> fetchedComments = postClient.getCommentsByPostId(post.getId());

        if (fetchedComments.isEmpty()) {
            saveStatusHistory(PostStatus.FAILED, post);
            disablePost(post.getId());
            throw new ResourceNotFoundException("Comments are empty. Status: FAILED. Disabling post.");
        }

        for (Comment fetchedComment : fetchedComments) {
            Comment comment = new Comment(fetchedComment.getBody(), post);
            arrayComments.add(comment);
        }
        post.setComments(arrayComments);

        saveStatusHistory(PostStatus.COMMENTS_OK, post);

        saveStatusHistory(PostStatus.ENABLED, post);

        return postRepository.save(post);
    }


    @Override
    @Async
    public void reprocessPost(Long postId) {

        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("This post id does not exists: " + postId);
        }

        Post post = postOptional.get();
        List<StatusHistory> historyList = post.getHistory();

        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("Empty status story for post id: " + postId);
        }

        log.info("percorrendo lista");
        StatusHistory mostRecentHistory = historyList.get(historyList.size() - 1);

        if (mostRecentHistory.getStatus() != PostStatus.ENABLED && mostRecentHistory.getStatus() != PostStatus.DISABLED) {
            throw new InvalidPostException("Could not update post. The actual status is: " + mostRecentHistory);
        }
        Post postPopulated = populatePostById(post);
        populateCommentByPostId(postPopulated);

    }


    @Override
    @Async
    public void disablePost(Long postId) {

        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("This post id does not exists: " + postId);
        }

        Post post = postOptional.get();
        List<StatusHistory> historyList = post.getHistory();

        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("Empty status story for post id: " + postId);
        }

        log.info("percorrendo lista");
        StatusHistory mostRecentHistory = historyList.get(historyList.size() - 1);

        if (mostRecentHistory.getStatus() == PostStatus.ENABLED || mostRecentHistory.getStatus() == PostStatus.FAILED) {
            saveStatusHistory(PostStatus.DISABLED, post);
        } else {
            throw new InvalidPostException("Could not disable post. The actual status is: " + mostRecentHistory);
        }
    }


    @Override
    public List<Post> returnAllPostsFromAPI() {
        return postClient.getAllPosts();
    }

    @Override
    public List<Post> getAllPostsFromBD() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new ResourceNotFoundException("No posts were found!");
        }
        return posts;
    }

}
