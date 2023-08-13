package br.com.compass.pb.blogpass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BlogAPIException extends RuntimeException {
    public BlogAPIException(String message) {
        super(message);
    }
}
