package br.com.compass.pb.blogpass.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    @NotNull(message = "Comment id should not be null")
    private Long id;

    @NotNull(message = "Comment body should not be null")
    @Size(max = 500, message = "Comment body can have 500 characters")
    private String body;

}
