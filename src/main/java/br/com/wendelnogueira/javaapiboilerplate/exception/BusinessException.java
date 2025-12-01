package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final String code;
    protected BusinessException(String code, String message) {
        super(MessageExceptionFormatter.getMessage(code, message));
        this.code = code;
    }
}
