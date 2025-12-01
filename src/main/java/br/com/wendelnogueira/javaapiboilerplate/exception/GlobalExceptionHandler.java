package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageExceptionFormatter messageExceptionFormatter;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(401).body(message);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException ex) {
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(403).body(message);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(422).body(message);
    }
}
