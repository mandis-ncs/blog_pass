package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.Role;
import br.com.compass.pb.blogpass.entities.UserModel;
import br.com.compass.pb.blogpass.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
