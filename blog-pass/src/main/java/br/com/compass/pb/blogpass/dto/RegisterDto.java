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
    private String username;
    private String password;
}


/*
{
        "username" :  "mandis",
        "password" : "mandis"
        }
 */