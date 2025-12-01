package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
  private final String code;
  private final MessageExceptionFormatter messageExceptionFormatter;
    protected BusinessException(String code, String message, MessageExceptionFormatter messageExceptionFormatter) {
        super(messageExceptionFormatter.getMessage(code, message));
        this.code = code;
        this.messageExceptionFormatter = messageExceptionFormatter;
    }
}
