package br.com.compass.pb.blogpass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidPostException extends RuntimeException{
    public InvalidPostException(String message) {
        super(message);
    }
}
