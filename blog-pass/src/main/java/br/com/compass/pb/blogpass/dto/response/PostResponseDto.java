package br.com.compass.pb.blogpass.dto.response;

import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String body;
    private List<Comment> comments;
    private List<StatusHistory> history;

}
