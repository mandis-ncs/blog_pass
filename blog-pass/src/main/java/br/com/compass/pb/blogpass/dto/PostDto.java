package br.com.compass.pb.blogpass.dto;

import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @NotNull(message = "Post id should not be null")
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Post title should not be null")
    @JsonProperty("title")
    private String title;

    @NotNull(message = "Post body should not be null")
    @JsonProperty("body")
    private String body;

    @JsonProperty("comments")
    private List<Comment> comments;

    @JsonProperty("history")
    private List<StatusHistory> history;

}
