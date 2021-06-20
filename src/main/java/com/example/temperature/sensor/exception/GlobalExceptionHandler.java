package com.example.temperature.sensor.exception;

import com.example.temperature.sensor.domain.dto.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * Exception handler for CSP validation
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Method to handle validation failure exceptions which returns 500 server error
     * @return response entity with internal server error status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Error error = Error.builder()
                .message(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .date(new Date()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    /**
     * Method to handle validation failure exceptions which returns 400 bad request
     * @return response entity with internal server error status code
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Error error = Error.builder()
                .message(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .code(HttpStatus.BAD_REQUEST.toString())
                .date(new Date()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Method to handle validation failure exceptions which returns 500 server error
     * @return response entity with internal server error status code
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalSateException(Exception e) {
        Error error = Error.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.toString())
                .date(new Date()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    /**
     * Method to handle resource not found exception which returns 404
     * @param ex the resource not found exception
     * @return response entity with exception message and status code
     */
    @ExceptionHandler(value = ResourceNotFoundExcpetion.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundExcpetion ex) {
        Error error = Error.builder()
                .message(ex.getLocalizedMessage())
                .code(HttpStatus.NOT_FOUND.toString())
                .date(new Date()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
