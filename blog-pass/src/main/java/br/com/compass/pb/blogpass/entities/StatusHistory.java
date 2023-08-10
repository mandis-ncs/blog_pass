package br.com.compass.pb.blogpass.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public StatusHistory(LocalDateTime date, PostStatus status, Post post) {
        this.date = date;
        this.status = status;
        this.post = post;
    }
}
