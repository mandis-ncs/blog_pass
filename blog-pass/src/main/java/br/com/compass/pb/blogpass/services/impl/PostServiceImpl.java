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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    @Override
    @Async
    public void processPost(Long postId) {

        Post post = new Post();
        post.setId(postId);

        // status history CREATED
        List<StatusHistory> arrayStatus = new ArrayList<>();
        StatusHistory createdPost  = new StatusHistory(LocalDateTime.now(), PostStatus.CREATED, post);
        arrayStatus.add(createdPost);

        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }

        if (postRepository.findById(postId).isPresent()) {
            throw new DuplicatedPostException("This post if id already exists: " + postId);
        }

        // status history FIND
        StatusHistory findingPost  = new StatusHistory(LocalDateTime.now(), PostStatus.POST_FIND, post); // CAN BE FAILED -> THEN DISABLE !!!
        arrayStatus.add(findingPost);

        post = postClient.getPostById(postId);

        StatusHistory findOkPost  = new StatusHistory(LocalDateTime.now(), PostStatus.POST_OK, post);
        arrayStatus.add(findOkPost);

        StatusHistory findingComment  = new StatusHistory(LocalDateTime.now(), PostStatus.COMMENTS_FIND, post); // CAN BE FAILED -> THEN DISABLE !!!
        arrayStatus.add(findingComment);

        List<Comment> arrayComments =new ArrayList<>();
        List<Comment> fetchedComments = postClient.getCommentsByPostId(postId);

        for (Comment fetchedComment : fetchedComments) {
            Comment comment = new Comment(fetchedComment.getBody(), post);
            arrayComments.add(comment);
        }
        post.setComments(arrayComments);

        StatusHistory findOkComments  = new StatusHistory(LocalDateTime.now(), PostStatus.COMMENTS_OK, post);
        arrayStatus.add(findOkComments);

        StatusHistory enabledPost  = new StatusHistory(LocalDateTime.now(), PostStatus.ENABLED, post); //CAN BE DISABLE !!!
        post.setHistory(arrayStatus);
        arrayStatus.add(enabledPost);

        postRepository.save(post);

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

        if (mostRecentHistory.getStatus() == PostStatus.ENABLED) {
            StatusHistory disableHistory = new StatusHistory(LocalDateTime.now(), PostStatus.DISABLED, post);
            historyList.add(disableHistory);
            postRepository.save(post);
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
}
