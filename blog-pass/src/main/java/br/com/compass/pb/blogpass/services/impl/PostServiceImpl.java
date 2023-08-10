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

import java.io.FileFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostClient postClient;
    private final PostRepository postRepository;
    private final HistoryRepository historyRepository;
    private final CommentsRepository commentsRepository;

    List<StatusHistory> arrayStatus = new ArrayList<>();

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

    @Override
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

        // status history CREATED
        StatusHistory createdPost  = new StatusHistory(LocalDateTime.now(), PostStatus.CREATED, post);
        arrayStatus.add(createdPost);

        log.info("saving history in process post");

        post.setHistory(arrayStatus);
        postRepository.save(post);

        // JUST FOR TEST -> CALLING FindPostById Method
        log.info("calling POST FINDING history");
        Post populatedPost = populatePostById(post);

        log.info("calling populate COMMENTS");
        populateCommentByPostId(populatedPost);
    }

    @Override
    @Async
    public void reprocessPost(Long postId) {

    }

    @Override
    @Async
    public void disablePost(Long postId) {

        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new DuplicatedPostException("This does not exists: " + postId);
        }

        Post post = postOptional.get();
        List<StatusHistory> historyList = post.getHistory();

        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("Empty status story for post id: " + postId);
        }

        StatusHistory mostRecentHistory = historyList.get(historyList.size() - 1);

        log.info("percorrendo lista");
        if (mostRecentHistory.getStatus() == PostStatus.ENABLED) {
            StatusHistory disableHistory = new StatusHistory(LocalDateTime.now(), PostStatus.DISABLED, post);
            historyList.add(disableHistory);

            log.info("tentativa de salvar status disable");
            historyRepository.save(disableHistory);

            log.info("status disable salvo");
        } else {
            throw new InvalidPostException("The actual status is not ENABLE, but: " + mostRecentHistory);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new ResourceNotFoundException("No posts were found!");
        }
        return posts;
    }

    public Post populatePostById(Post post) {

        // status history FIND
        StatusHistory findingPost  = new StatusHistory(LocalDateTime.now(), PostStatus.POST_FIND, post);
        historyRepository.save(findingPost);

        log.info("setting POST FIND history");

        post = postClient.getPostById(post.getId());

        if (post.getTitle().isEmpty() || post.getBody().isEmpty()) {
            log.info("saving history FAILED");
            StatusHistory failedPost  = new StatusHistory(LocalDateTime.now(), PostStatus.FAILED, post);
            historyRepository.save(failedPost);
            disablePost(post.getId());
            throw new ResourceNotFoundException("Post title or body is empty. Status: FAILED. Disabling post.");
        }

        StatusHistory findOkPost  = new StatusHistory(LocalDateTime.now(), PostStatus.POST_OK, post);
        historyRepository.save(findOkPost);

        log.info("returning");
        return postRepository.save(post);
    }


    public Post populateCommentByPostId(Post post) {

        StatusHistory findingComment  = new StatusHistory(LocalDateTime.now(), PostStatus.COMMENTS_FIND, post);
        historyRepository.save(findingComment);
        log.info("saving history COMMENTS_FIND");

        List<Comment> arrayComments = new ArrayList<>();
        List<Comment> fetchedComments = postClient.getCommentsByPostId(post.getId());

        if (fetchedComments.isEmpty()) {
            StatusHistory failedComment  = new StatusHistory(LocalDateTime.now(), PostStatus.FAILED, post);
            historyRepository.save(failedComment);
            disablePost(post.getId());
            throw new ResourceNotFoundException("Comments are empty. Status: FAILED. Disabling post.");
        }

        for (Comment fetchedComment : fetchedComments) {
            Comment comment = new Comment(fetchedComment.getBody(), post);
            arrayComments.add(comment);
        }
        post.setComments(arrayComments);

        StatusHistory findOkComments  = new StatusHistory(LocalDateTime.now(), PostStatus.COMMENTS_OK, post);
        historyRepository.save(findOkComments);

        StatusHistory enabledPost  = new StatusHistory(LocalDateTime.now(), PostStatus.ENABLED, post);
        historyRepository.save(enabledPost);

        return postRepository.save(post);
    }
}
