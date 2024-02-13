package com.thiscat.stadiumamp.rest;



import com.thiscat.stadiumamp.system.common.ApiResultResponse;
import com.thiscat.stadiumamp.system.common.ApiResultWithValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
    public ResponseEntity<ApiResultResponse> getResponseEntity(String returnMessage, HttpStatus httpStatus) {
        return new ResponseEntity<ApiResultResponse>(
                ApiResultResponse.builder()
                        .message(returnMessage)
                        .build(),
                httpStatus
        );
    }

    public ResponseEntity<ApiResultWithValue> getResponseEntity(Object data, String returnMessage, HttpStatus httpStatus) {
        return new ResponseEntity<ApiResultWithValue>(
                ApiResultWithValue.builder()
                        .data(data)
                        .result(ApiResultResponse.builder()
                                //.code(HttpStatus.OK.toString())
                                .message(returnMessage)
                                .build()
                        )
                        .build(),
                httpStatus
        );
    }



}
