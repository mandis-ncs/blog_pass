package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
