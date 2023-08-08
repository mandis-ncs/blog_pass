package br.com.compass.pb.blogpass.dto.response;

import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.StatusHistory;

import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String body,
        List<Comment> comments,
        List<StatusHistory> history
) {
}
