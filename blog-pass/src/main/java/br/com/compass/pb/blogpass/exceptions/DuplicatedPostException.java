package br.com.compass.pb.blogpass.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedPostException extends RuntimeException {
    public DuplicatedPostException(String message) {
        super(message);
    }
}
