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
public class CountEffectDto {
    @ApiModelProperty(value = "Event ㅑㅇ")
    private Long eventId;

    @ApiModelProperty(value = "animation count")
    int animationCount;

    @ApiModelProperty(value = "emoji")
    String emoji;

    @ApiModelProperty(value = "animation_color")
    String animationColor;

}
