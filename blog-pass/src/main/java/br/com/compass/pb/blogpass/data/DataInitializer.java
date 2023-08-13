package br.com.compass.pb.blogpass.data;

import br.com.compass.pb.blogpass.entities.Role;
import br.com.compass.pb.blogpass.entities.UserModel;
import br.com.compass.pb.blogpass.enums.RoleName;
import br.com.compass.pb.blogpass.repositories.RoleRepository;
import br.com.compass.pb.blogpass.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initRolesAndUsers();
    }

    private void initRolesAndUsers() {
        // Roles
        Role adminRole = new Role(null, RoleName.ROLE_ADMIN);
        Role userRole = new Role(null, RoleName.ROLE_USER);

        roleRepository.saveAll(Arrays.asList(adminRole, userRole));

        // Users
        UserModel mariaUser = new UserModel(null, "admin", passwordEncoder.encode("admin123"), Arrays.asList(adminRole));
        UserModel joaoUser = new UserModel(null, "user", passwordEncoder.encode("user123"), Arrays.asList(userRole));

        userRepository.saveAll(Arrays.asList(mariaUser, joaoUser));
    }
}

