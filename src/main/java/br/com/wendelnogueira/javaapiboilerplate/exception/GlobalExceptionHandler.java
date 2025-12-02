package br.com.wendelnogueira.javaapiboilerplate.exception;

import br.com.wendelnogueira.javaapiboilerplate.util.MessageExceptionFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageExceptionFormatter messageExceptionFormatter;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.error("Not found: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(401).body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        log.error("Forbidden: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(403).body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage("06", ex.getMessage());
        return ResponseEntity.status(403).body(new ErrorResponse("06", message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.error("Business exception: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(422).body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.error("Conflict: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(409).body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage("07", ex.getMessage());
        return ResponseEntity.status(409).body(new ErrorResponse("07", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        String message = messageExceptionFormatter.getMessage("08", ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("08", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        String message = messageExceptionFormatter.getMessage("07", ex.getMessage());
        return ResponseEntity.status(500).body(new ErrorResponse("07", message));
    }
}
