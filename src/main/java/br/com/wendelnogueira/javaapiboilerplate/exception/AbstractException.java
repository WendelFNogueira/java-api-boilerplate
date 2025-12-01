package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AbstractException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    protected  AbstractException(String code, String message, HttpStatus httpStatus) {
        super(MessageExceptionFormatter.getMessage(code, message));
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
