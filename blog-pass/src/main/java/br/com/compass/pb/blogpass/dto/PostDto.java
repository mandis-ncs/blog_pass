package br.com.compass.pb.blogpass.dto;

import br.com.compass.pb.blogpass.entities.Comment;
import br.com.compass.pb.blogpass.entities.StatusHistory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

public class PostDto {

    private Long id;

    private String title;

    private String body;

    private List<Comment> comments;

    private List<StatusHistory> history;

}
