package br.com.softplan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ExceptionBuilder {

    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<StandardError> notFound(DataNotFoundException e, HttpServletRequest req) {
        String messageError = "Data not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                messageError,
                e.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({DataInvalidException.class})
    public ResponseEntity<StandardError> dataNotValid(DataInvalidException e, HttpServletRequest req) {
        String message = "Invalid specified data on request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                message,
                e.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<StandardError> globalException(HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Internal server error",
                "Aconteceu um problema interno não identificado. Tente novamente mais tarde ou fale com o suporte.",
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<StandardError> accessDenied(HttpServletRequest req) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Forbidden",
                "Você não está autorizado a acessar esse conteúdo.",
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> argumentsNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
        StringBuilder validation = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            validation.append("Erro no campo: ").append(errorMessage).append(", ");
        });
        StandardError error = new StandardError(
                Instant.now(),
                400,
                "Failed on validate form fields",
                validation.deleteCharAt(validation.lastIndexOf(",")).toString(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}