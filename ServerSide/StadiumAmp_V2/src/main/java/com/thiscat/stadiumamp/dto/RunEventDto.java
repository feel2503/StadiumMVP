package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thiscat.stadiumamp.entity.Event;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunEventDto {
    private Long id;

    private long eventId;

    Integer homeCount;
    Integer home1Count;
    Integer home2Count;
    Integer home3Count;
    Integer home4Count;
    Integer home5Count;

    Integer awayCount;
    Integer away1Count;
    Integer away2Count;
    Integer away3Count;
    Integer away4Count;
    Integer away5Count;

    String eventState;

    String webUrl;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    int triggerType;
    int triggerTime;
    int triggerVote;

    ArrayList<EventImageDto> eventImageList;
    ArrayList<EventMusicDto> eventMusicList;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

    String result;
}
