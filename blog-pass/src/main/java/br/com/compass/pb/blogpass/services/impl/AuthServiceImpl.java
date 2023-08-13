package br.com.compass.pb.blogpass.services.impl;

import br.com.compass.pb.blogpass.dto.LoginDto;
import br.com.compass.pb.blogpass.dto.PostDto;
import br.com.compass.pb.blogpass.dto.RegisterDto;
import br.com.compass.pb.blogpass.entities.Post;
import br.com.compass.pb.blogpass.entities.Role;
import br.com.compass.pb.blogpass.entities.UserModel;
import br.com.compass.pb.blogpass.enums.RoleName;
import br.com.compass.pb.blogpass.exceptions.DuplicatedPostException;
import br.com.compass.pb.blogpass.exceptions.DuplicatedUsernameException;
import br.com.compass.pb.blogpass.exceptions.UsernameNotFoundException;
import br.com.compass.pb.blogpass.repositories.RoleRepository;
import br.com.compass.pb.blogpass.repositories.UserRepository;
import br.com.compass.pb.blogpass.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("This username does not exists: " + username);
        }
        return username;
    }

    @Override
    public LoginDto register(RegisterDto registerDto) {

        String username = registerDto.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicatedUsernameException("This username already exists: " + username);
        }

        // role
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found."));

        List<Role> roleList = new ArrayList<>();
        roleList.add(userRole);

        // new user
        UserModel newUser = modelMapper.map(registerDto, UserModel.class);
        newUser.setRoles(roleList);

        UserModel savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, LoginDto.class);

    }
}
