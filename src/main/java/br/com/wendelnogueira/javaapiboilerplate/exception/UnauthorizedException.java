package br.com.wendelnogueira.javaapiboilerplate.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractException {
    public UnauthorizedException(String code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }
}