package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
