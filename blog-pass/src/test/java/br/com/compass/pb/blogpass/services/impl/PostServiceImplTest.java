package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.CommentDto;
import br.com.compass.pb.blogpass.dto.PostDto;
import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import br.com.compass.pb.blogpass.enums.PostStatus;
import br.com.compass.pb.blogpass.exceptions.InvalidPostException;
import br.com.compass.pb.blogpass.exceptions.ResourceNotFoundException;
import br.com.compass.pb.blogpass.repositories.HistoryRepository;
import br.com.compass.pb.blogpass.repositories.PostRepository;
import br.com.compass.pb.blogpass.services.PostClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostClient postClient;

    @Mock
    private PostRepository postRepository;

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Comment> someListOfComments() {
        List<Comment> comments = new ArrayList<>();

        Comment comment1 = new Comment("1", somePost());
        Comment comment2 = new Comment("2", somePost());

        comments.add(comment1);
        comments.add(comment2);

        return comments;
    }

    private List<CommentDto> someListOfCommentsDto() {
        List<CommentDto> comments = new ArrayList<>();

        CommentDto comment1 = new CommentDto(1L, "algum commet");
        CommentDto comment2 = new CommentDto(2L, "algum commet2");

        comments.add(comment1);
        comments.add(comment2);

        return comments;
    }

    private Post somePost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Sample Post Title");
        post.setBody("This is the body of the post");
//        post.setComments(someListOfComments());

        return post;
    }


    @Test
    public void shouldThrowAnExceptionIfCommentsArrayIsEmpty() {
        // Mock the behavior of postClient to return an empty list of comments
        when(postClient.getCommentsByPostId(anyLong())).thenReturn(Collections.emptyList());

        // Call the method to test
        assertThrows(ResourceNotFoundException.class, () -> postService.fetchAndSaveCommentsArray(somePost()));

        // Verify that the necessary methods were called
        verify(postClient, times(1)).getCommentsByPostId(anyLong());
        verify(historyRepository, times(1)).save(any(StatusHistory.class));
    }


    @Test
    public void shouldThrowAnExceptionIfPostTitleIsEmpty() {
        // Arrange
        PostClient postClient = Mockito.mock(PostClient.class);
        PostRepository postRepository = Mockito.mock(PostRepository.class);
        HistoryRepository historyRepository = Mockito.mock(HistoryRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        PostServiceImpl postService = new PostServiceImpl(postClient, postRepository, historyRepository,
                null, modelMapper, null);

        Long postId = 1L;

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle("");  // Empty title
        postDto.setBody("This is the body of the post");

        Mockito.when(postClient.getPostById(anyLong())).thenReturn(postDto);

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.populatePostById(postId);
        });
    }

    @Test
    public void shouldThrowAnExceptionIfStatusIsNotEnableOrDisable() {
        Long postId = 1L;

        Post post = new Post();
        post.setId(postId);
        post.setHistory(Collections.singletonList(new StatusHistory(LocalDateTime.now(), PostStatus.UPDATING, post)));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(InvalidPostException.class, () -> postService.disablePost(postId));
    }

}