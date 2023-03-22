package kr.co.thiscat.stadiumamp.rest;


import kr.co.thiscat.stadiumamp.system.common.ApiResultResponse;
import kr.co.thiscat.stadiumamp.system.common.ApiResultWithValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
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
