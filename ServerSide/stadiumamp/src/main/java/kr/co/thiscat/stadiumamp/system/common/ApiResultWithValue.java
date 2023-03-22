package kr.co.thiscat.stadiumamp.system.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResultWithValue {
    private ApiResultResponse result;
    private Object data;
}
