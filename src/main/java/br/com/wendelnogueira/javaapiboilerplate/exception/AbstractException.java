package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Getter
public class AbstractException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    @Autowired
    private static MessageExceptionFormatter messageExceptionFormatter;

    protected  AbstractException(String code, String message, HttpStatus httpStatus) {
        super(messageExceptionFormatter != null ? messageExceptionFormatter.getMessage(code, message) : message);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
