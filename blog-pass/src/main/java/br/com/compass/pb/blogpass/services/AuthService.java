package br.com.compass.pb.blogpass.services;

import br.com.compass.pb.blogpass.dto.LoginDto;
import br.com.compass.pb.blogpass.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    LoginDto register(RegisterDto registerDto);
}
