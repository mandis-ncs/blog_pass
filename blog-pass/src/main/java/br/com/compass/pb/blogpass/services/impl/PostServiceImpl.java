package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.PostClient;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.entities.PostStatus;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import br.com.compass.pb.blogpass.repositories.CommentsRepository;
import br.com.compass.pb.blogpass.repositories.HistoryRepository;
import br.com.compass.pb.blogpass.repositories.PostRepository;
import br.com.compass.pb.blogpass.services.PostService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        Post post = postClient.getPostById(postId);

        // status history created
        List<StatusHistory> arrayStatus = new ArrayList<>();
        StatusHistory createdHistory  = new StatusHistory(LocalDateTime.now(), PostStatus.CREATED, post);
        arrayStatus.add(createdHistory);

        // comments
        List<Comment> arrayComments =new ArrayList<>();
        List<Comment> fetchedComments = postClient.getCommentsByPostId(postId);

        for (Comment fetchedComment : fetchedComments) {
            Comment comment = new Comment(fetchedComment.getBody(), post);
            arrayComments.add(comment);
        }

        // status history find
        StatusHistory commentsFindHistory = new StatusHistory(LocalDateTime.now(), PostStatus.COMMENTS_FIND, post);
        arrayStatus.add(commentsFindHistory);

        post.setHistory(arrayStatus);
        post.setComments(arrayComments);
        postRepository.save(post);

    }

    @Override
    @Async
    public void reprocessPost(Long postId) {

    }

    @Override
    @Async
    public void disablePost(Long postId) {

    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
