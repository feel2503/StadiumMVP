package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventDto {
    Long eventId;

    @ApiModelProperty(value = "Music 재생 타입 (Time : 0 / Vote : 1) ")
    int triggerType;
    int triggerTime;
    int triggerVote;

    String webUrl;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

}
