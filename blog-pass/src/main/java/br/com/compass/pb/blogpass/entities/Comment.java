package br.com.compass.pb.blogpass.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Comment {

    @Id
    private Long id;

    @Column(length = 500)
    private String body;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String body, Post post) {
        this.body = body;
        this.post = post;
    }
}
