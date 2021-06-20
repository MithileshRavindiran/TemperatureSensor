package com.example.temperature.sensor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundExcpetion extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Exception Handler method for not found error
     * @param message not found exception message
     */
    public ResourceNotFoundExcpetion(String message) {
        super(message);
    }
}
