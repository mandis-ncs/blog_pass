package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
