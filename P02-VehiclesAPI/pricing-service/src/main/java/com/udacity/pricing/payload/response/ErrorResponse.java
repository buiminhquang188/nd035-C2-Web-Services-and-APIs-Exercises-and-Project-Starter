package com.udacity.pricing.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse<T> {
    private String statusCode;
    private String status;
    private ErrorDetailResponse<T> error;
}
