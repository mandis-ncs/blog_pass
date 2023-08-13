package br.com.compass.pb.blogpass.services;


import br.com.compass.pb.blogpass.dto.LoginDto;
import br.com.compass.pb.blogpass.dto.RegisterDto;
import br.com.compass.pb.blogpass.entities.User;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
