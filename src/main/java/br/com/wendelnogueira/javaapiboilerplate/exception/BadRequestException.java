package br.com.wendelnogueira.javaapiboilerplate.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractException {
    public BadRequestException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
