package br.com.compass.pb.blogpass.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDto {
    private String name;
    private String username;
    private String email;
    private String password;
}
/*
{
        "name" : "mandis",
        "username" :  "mandis",
        "email" : "mandis@gmail.com",
        "password" : "mandis"
        }

 */