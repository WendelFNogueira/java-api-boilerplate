package br.com.wendelnogueira.javaapiboilerplate.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractException {
    public ForbiddenException(String code, String message) {
        super(code, message, HttpStatus.FORBIDDEN);
    }
}
