package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
