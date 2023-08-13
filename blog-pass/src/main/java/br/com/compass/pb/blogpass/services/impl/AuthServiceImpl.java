package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.LoginDto;
import br.com.compass.pb.blogpass.dto.PostDto;
import br.com.compass.pb.blogpass.dto.RegisterDto;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.entities.Role;
import br.com.compass.pb.blogpass.entities.UserModel;
import br.com.compass.pb.blogpass.enums.RoleName;
import br.com.compass.pb.blogpass.exceptions.DuplicatedUsernameException;
import br.com.compass.pb.blogpass.exceptions.UsernameNotFoundException;
import br.com.compass.pb.blogpass.repositories.RoleRepository;
import br.com.compass.pb.blogpass.repositories.UserRepository;
import br.com.compass.pb.blogpass.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public String login(LoginDto loginDto) {
        String username = loginDto.getUsername();

        userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This username does not exist: " + username));

        return username;
    }


    @Override
    public LoginDto register(RegisterDto registerDto) {
        String username = registerDto.getUsername();
        validateUsernameAvailability(username);

        Role userRole = getOrCreateUserRole();

        UserModel newUser = createUserFromDto(registerDto, userRole);

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        newUser.setPassword(encodedPassword);

        UserModel savedUser = userRepository.save(newUser);

        return mapUserToLoginDto(savedUser);
    }

    private void validateUsernameAvailability(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicatedUsernameException("This username already exists: " + username);
        }
    }

    private Role getOrCreateUserRole() {
        return roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found."));
    }

    private UserModel createUserFromDto(RegisterDto registerDto, Role userRole) {
        UserModel newUser = modelMapper.map(registerDto, UserModel.class);
        newUser.setRoles(Collections.singletonList(userRole));
        return newUser;
    }

    private LoginDto mapUserToLoginDto(UserModel user) {
        return modelMapper.map(user, LoginDto.class);
    }
}
