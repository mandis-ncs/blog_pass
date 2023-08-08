package br.com.compass.pb.blogpass.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter

public class ExceptionResponse {

    private LocalDateTime timestamp;

    private String message;

    private String details;

}
