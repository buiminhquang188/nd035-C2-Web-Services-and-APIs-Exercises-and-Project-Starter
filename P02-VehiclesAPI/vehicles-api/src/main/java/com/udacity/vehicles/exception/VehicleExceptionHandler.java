package com.udacity.vehicles.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class VehicleExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            CarNotFoundException.class,
            ManufacturerNotFoundException.class,
    })
    public ResponseEntity<?> handleNotFoundException(RuntimeException runtimeException, HttpServletRequest request) {
        return ResponseEntity.notFound()
                .build();
    }
}
