package com.udacity.pricing.exception;

import com.udacity.pricing.enums.Status;
import com.udacity.pricing.payload.response.ErrorDetailResponse;
import com.udacity.pricing.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class PricingExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            PriceException.class
    })
    public ResponseEntity<?> handleNotFoundException(RuntimeException runtimeException, HttpServletRequest httpServletRequest) {
        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(Status.ERROR.toString()
                        .toLowerCase())
                .statusCode(HttpStatus.NOT_FOUND.getReasonPhrase())
                .error(
                        ErrorDetailResponse.builder()
                                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(runtimeException.getMessage())
                                .details(runtimeException.getMessage())
                                .timestamp(LocalDateTime.now())
                                .path(httpServletRequest.getRequestURI())
                                .build()
                )
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
