package com.thiscat.stadiumamp.system.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResult {
    private String code;
    private String message;
    private HttpStatus httpStatus;
}
