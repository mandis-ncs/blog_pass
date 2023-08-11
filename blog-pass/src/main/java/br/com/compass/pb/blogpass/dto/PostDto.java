package br.com.compass.pb.blogpass.dto;

import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDto {

    @NotNull
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private String body;
    private List<Comment> comments;
    private List<StatusHistory> history;

}
