package br.com.compass.pb.blogpass.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @NotNull(message = "Comment id should not be null")
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Comment body should not be null")
    @Size(max = 500, message = "Comment body can have 500 characters")
    @JsonProperty("body")
    private String body;

}
