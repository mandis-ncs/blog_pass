package br.com.compass.pb.blogpass.controllers;

import br.com.compass.pb.blogpass.dto.LoginDto;
import br.com.compass.pb.blogpass.dto.RegisterDto;
import br.com.compass.pb.blogpass.services.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // cache expire in 1h
@RequestMapping("/blog-pass/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PermitAll
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String response = authService.login(loginDto);
        return ResponseEntity.ok("Successfully login with username: " + response);
    }

    @PermitAll
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<LoginDto> register(@RequestBody RegisterDto registerDto){
        LoginDto userLoginInfo = authService.register(registerDto);
        return ResponseEntity.ok(userLoginInfo);
    }

}
