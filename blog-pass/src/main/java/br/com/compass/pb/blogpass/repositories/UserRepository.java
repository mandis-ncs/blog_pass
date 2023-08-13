package br.com.compass.pb.blogpass.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.compass.pb.blogpass.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
