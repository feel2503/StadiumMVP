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

    private String serverName;
    private String homeName;
    private String awayName;

    Integer homeCount;
    Integer home1Count;
    Integer home2Count;
    Integer home3Count;
    Integer home4Count;
    Integer home5Count;
    Integer home6Count;
    Integer home7Count;
    Integer home8Count;
    Integer home9Count;
    Integer home10Count;
    Integer home11Count;
    Integer home12Count;
    Integer home13Count;
    Integer home14Count;
    Integer home15Count;
    Integer home16Count;
    Integer home17Count;
    Integer home18Count;
    Integer home19Count;
    Integer home20Count;

    Integer awayCount;
    Integer away1Count;
    Integer away2Count;
    Integer away3Count;
    Integer away4Count;
    Integer away5Count;
    Integer away6Count;
    Integer away7Count;
    Integer away8Count;
    Integer away9Count;
    Integer away10Count;
    Integer away11Count;
    Integer away12Count;
    Integer away13Count;
    Integer away14Count;
    Integer away15Count;
    Integer away16Count;
    Integer away17Count;
    Integer away18Count;
    Integer away19Count;
    Integer away20Count;

    String eventState;

    String webUrl;
    String openchatUrl;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String strStartDateTime;
    private String strEndDateTime;
    private String duration;

    int triggerType;
    int triggerTime;
    int triggerVote;

    ArrayList<EventImageDto> eventImageList;
    ArrayList<EventMusicDto> eventMusicList;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

    String result;

    int volumeValue = 5;

    Integer tag0;
    Integer tag1;
    Integer tag2;
    Integer tag3;
    Integer tag4;
    Integer tag5;
    Integer tag6;
    Integer tag7;
    Integer tag8;
    Integer tag9;
}
