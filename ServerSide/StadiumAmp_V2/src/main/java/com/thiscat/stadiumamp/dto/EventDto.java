package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thiscat.stadiumamp.entity.Event;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {
//    private Long id;
    private Long eventId;

    String eventName;

    @ApiModelProperty(value = "Music 재생 타입 (Time : 0 / Vote : 1) ")
    int triggerType;
    int triggerTime;
    int triggerVote;

    String webUrl;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

    ArrayList<EventImageDto> eventImageList;
    ArrayList<EventMusicDto> eventMusicList;

    Long runEvent;
    String eventState;

    String homeColor;
    String homeFont;
    String awayColor;
    String awayFont;

    String openchatUrl;

    int volumeValue = -1;

    String eventBkcolor;

    String cheerUrl1;
    String cheerUrl2;

    Boolean volumeSync;
}
