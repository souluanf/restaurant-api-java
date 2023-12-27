package dev.luanfernandes.restaurant.common.exceptions;

import static java.lang.String.format;
import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;
import static org.springframework.http.ResponseEntity.status;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final String TIMESTAMP_PROPERTY = "timestamp";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, "Validation failed for argument");
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();
        problemDetail.setProperty(TIMESTAMP_PROPERTY, Instant.now());
        problemDetail.setProperty("stacktrace", errors);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException exception) {
        ProblemDetail problemDetail = exceptionToProblemDetailForStatusAndDetail(BAD_REQUEST, exception.getMessage());
        return status(BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException exception) {
        ProblemDetail problemDetail =
                exceptionToProblemDetailForStatusAndDetail(exception.getStatusCode(), exception.getReason());
        return status(exception.getStatusCode()).body(problemDetail);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException exception) {
        return exceptionToProblemDetailForStatusAndDetail(NOT_FOUND, exception.getMessage());
    }

    private ProblemDetail exceptionToProblemDetailForStatusAndDetail(HttpStatusCode status, String detail) {
        ProblemDetail problemDetail = forStatusAndDetail(status, detail);
        problemDetail.setProperty(TIMESTAMP_PROPERTY, now());
        return problemDetail;
    }
}
