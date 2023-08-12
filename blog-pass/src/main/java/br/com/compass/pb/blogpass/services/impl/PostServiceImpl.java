package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.CommentDto;
import br.com.compass.pb.blogpass.messaging.producers.MessageProducerPost;
import br.com.compass.pb.blogpass.services.PostClient;
import br.com.compass.pb.blogpass.dto.PostDto;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.enums.PostStatus;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import br.com.compass.pb.blogpass.exceptions.DuplicatedPostException;
import br.com.compass.pb.blogpass.exceptions.InvalidPostException;
import br.com.compass.pb.blogpass.exceptions.ResourceNotFoundException;
import br.com.compass.pb.blogpass.repositories.CommentsRepository;
import br.com.compass.pb.blogpass.repositories.HistoryRepository;
import br.com.compass.pb.blogpass.repositories.PostRepository;
import br.com.compass.pb.blogpass.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private final ModelMapper modelMapper;
    private final MessageProducerPost messageProducer;

    public PostServiceImpl(PostClient postClient, PostRepository postRepository, HistoryRepository historyRepository,
                           CommentsRepository commentsRepository, ModelMapper modelMapper, MessageProducerPost messageProducer) {
        this.postClient = postClient;
        this.postRepository = postRepository;
        this.historyRepository = historyRepository;
        this.commentsRepository = commentsRepository;
        this.modelMapper = modelMapper;
        this.messageProducer = messageProducer;
    }


    @Override
    public StatusHistory saveStatusHistory(PostStatus status, Post post) {
        StatusHistory history = new StatusHistory(LocalDateTime.now(), status, post);
        return historyRepository.save(history);
    }


    @Override
    public StatusHistory getMostRecentStatus(List<StatusHistory> historyList, Long postId) {
        if (historyList.isEmpty()) {
            throw new ResourceNotFoundException("Empty status story for post id: " + postId);
        }

        log.info("percorrendo lista");
        return historyList.get(historyList.size() - 1);
    }


    @Override
    public void validatePostIdBetween1And100(Long postId) {
        if (postId < 0 || postId > 100) {
            throw new InvalidPostException("The post id should be between 0 and 100.");
        }
    }


    @Override
    public Optional<Post> findExistentPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("This post id does not exist: " + postId);
        }
        return postOptional;
    }


    public void fetchAndSaveCommentsArray (Post post) {
        List<Comment> arrayComments = new ArrayList<>();
        List<CommentDto> fetchedComments = postClient.getCommentsByPostId(post.getId());

        if (fetchedComments.isEmpty()) {
            saveStatusHistory(PostStatus.FAILED, post);
            disablePost(post.getId());
            throw new ResourceNotFoundException("Comments are empty. Status: FAILED. Disabling post.");
        }

        for (CommentDto fetchedComment : fetchedComments) {
            Comment comment = new Comment(fetchedComment.getBody(), post);
            comment.setId(fetchedComment.getId());
            arrayComments.add(comment);
        }
        post.setComments(arrayComments);

        saveStatusHistory(PostStatus.COMMENTS_OK, post);
    }


    @Override
    @Transactional
    public void processPost(Long postId) {

        validatePostIdBetween1And100(postId);

        if (postRepository.findById(postId).isPresent()) {
            throw new DuplicatedPostException("This post if id already exists: " + postId);
        }

        Post post = new Post();
        post.setId(postId);
        postRepository.save(post);

        StatusHistory createdPost = saveStatusHistory(PostStatus.CREATED, post);
        historyRepository.save(createdPost);

        // sending to first queue
        String message = "Post " + postId + " created";
        messageProducer.sendMessageToDestination("post_population", message);

    }


    @Override
    public Post populatePostById(Long postId) {

        PostDto postDto = postClient.getPostById(postId);
        Post post = modelMapper.map(postDto, Post.class);

        saveStatusHistory(PostStatus.POST_FIND, post);

        if (post.getTitle().isEmpty() || post.getBody().isEmpty()) {
            saveStatusHistory(PostStatus.FAILED, post);
            disablePost(post.getId());
            throw new ResourceNotFoundException("Post title or body is empty. Status: FAILED. Disabling post.");
        }

        saveStatusHistory(PostStatus.POST_OK, post);

        log.info("returning, deu ruim aqui no SERVICE");
        return postRepository.save(post);
    }


    @Override
    public PostDto populateCommentByPostId(Long postId) {

        Post post = findExistentPostById(postId).get();

        saveStatusHistory(PostStatus.COMMENTS_FIND, post);

        fetchAndSaveCommentsArray(post);

        saveStatusHistory(PostStatus.ENABLED, post);

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }


    @Override
    public void reprocessPost(Long postId) {

        validatePostIdBetween1And100(postId);

        Post post = findExistentPostById(postId).get();

        StatusHistory mostRecentHistory = getMostRecentStatus(post.getHistory(), postId);

        if (mostRecentHistory.getStatus() != PostStatus.ENABLED && mostRecentHistory.getStatus() != PostStatus.DISABLED) {
            throw new InvalidPostException("Could not update post. The actual status is: " + mostRecentHistory);
        }

        saveStatusHistory(PostStatus.UPDATING, post);

        Post postPopulated = populatePostById(postId);

        populateCommentByPostId(postId);

    }


    @Override
    public void disablePost(Long postId) {

        validatePostIdBetween1And100(postId);

        Post post = findExistentPostById(postId).get();

        StatusHistory mostRecentHistory = getMostRecentStatus(post.getHistory(), postId);

        if (mostRecentHistory.getStatus() == PostStatus.ENABLED || mostRecentHistory.getStatus() == PostStatus.FAILED) {
            saveStatusHistory(PostStatus.DISABLED, post);
        } else {
            throw new InvalidPostException("Could not disable post. The actual status is: " + mostRecentHistory);
        }
    }

    @Override
    public List<PostDto> getAllPostsFromBD() {
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            throw new ResourceNotFoundException("No posts were found!");
        }

        List<PostDto> responseDtoList = modelMapper.map(
                posts, new TypeToken<List<PostDto>>() {}.getType());

        return responseDtoList;
    }

}
