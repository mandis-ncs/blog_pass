package br.com.compass.pb.blogpass.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.compass.pb.blogpass.entities.UserModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByUsername(String username);

}
