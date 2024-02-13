package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunEventWebDto {
    private Long id;

    private long eventId;

    Integer homeCount;
    Integer home1Count;
    String home1Name;
    Integer home2Count;
    String home2Name;
    Integer home3Count;
    String home3Name;
    Integer home4Count;
    String home4Name;
    Integer home5Count;
    String home5Name;

    Integer awayCount;
    Integer away1Count;
    String away1Name;
    Integer away2Count;
    String away2Name;
    Integer away3Count;
    String away3Name;
    Integer away4Count;
    String away4Name;
    Integer away5Count;
    String away5Name;

    String eventState;

    String webUrl;

    private LocalDateTime startDateTime;
    int triggerType;
    int triggerTime;
    int triggerVote;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

}
