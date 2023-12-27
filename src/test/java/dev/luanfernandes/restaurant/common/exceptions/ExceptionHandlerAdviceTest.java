package dev.luanfernandes.restaurant.common.exceptions;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ExceptionHandlerAdvice")
class ExceptionHandlerAdviceTest {

    @InjectMocks
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Mock
    private MethodParameter methodParameter;

    @Test
    @DisplayName("Should return a bad model when handle a ConstraintViolationException")
    void handleConstraintViolationException() {
        var exception = new ConstraintViolationException("message", null);
        var responseEntity = exceptionHandlerAdvice.handleConstraintViolationException(exception);
        var body = responseEntity.getBody();
        assertEquals(requireNonNull(body).getStatus(), BAD_REQUEST.value());
        assertNotNull(requireNonNull(body).getProperties());
        assertTrue(requireNonNull(body).getProperties().containsKey("timestamp"));
    }

    @Test
    @DisplayName("Should handle EntityNotFoundException and return not found response")
    void handleEntityNotFoundException() {
        var message = "Entity not found";
        var exception = new EntityNotFoundException(message);
        var problemDetail = exceptionHandlerAdvice.handleEntityNotFoundException(exception);
        assertEquals(NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
    }

    @Test
    @DisplayName("Should handle ResponseStatusException and return appropriate response")
    void handleResponseStatusException() {
        String reason = "Detailed reason for the exception";
        ResponseStatusException exception = new ResponseStatusException(BAD_REQUEST, reason);
        ResponseEntity<ProblemDetail> responseEntity = exceptionHandlerAdvice.handleResponseStatusException(exception);
        assertNotNull(responseEntity);
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        ProblemDetail problemDetail = responseEntity.getBody();
        assertNotNull(problemDetail);
        assertEquals(reason, problemDetail.getDetail());
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
    }

    @Test
    @DisplayName("Should return validation errors when handle a MethodArgumentNotValidException")
    void handleMethodArgumentNotValidException() {
        Object target = new Object();
        String objectName = "objectName";
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, objectName);
        bindingResult.addError(new FieldError(objectName, "field", "must not be null"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        ProblemDetail problemDetail = exceptionHandlerAdvice.handleMethodArgumentNotValidException(exception);
        assertNotNull(problemDetail);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertTrue(requireNonNull(problemDetail.getProperties()).containsKey("stacktrace"));
        List<String> errors = (List<String>) problemDetail.getProperties().get("stacktrace");
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("field: must not be null"));
    }

    @Test
    @DisplayName("Should handle HttpClientErrorException and return appropriate response")
    void handleHttpClientErrorException() {
        HttpClientErrorException exception = new HttpClientErrorException(UNAUTHORIZED, "Unauthorized");
        ResponseEntity<ProblemDetail> responseEntity = exceptionHandlerAdvice.handleHttpClientErrorException(exception);
        assertNotNull(responseEntity);
        assertEquals(UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        ProblemDetail problemDetail = responseEntity.getBody();
        assertNotNull(problemDetail);
        assertEquals(UNAUTHORIZED.value(), problemDetail.getStatus());
    }
}
