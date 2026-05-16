package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thiscat.stadiumamp.dao.TagDao;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewEventDto {
    String eventName;
    String homeName;
    String awayName;

    @ApiModelProperty(value = "Music 재생 타입 (Time : 0 / Vote : 1) ")
    int triggerType;
    int triggerTime;
    int triggerVote;

    String webUrl;
    String webImg;

    @ApiModelProperty(value = "이벤트 반복 타입 (반복 : 1 / 반복 안함 : 0)")
    int continuityType;
    int continuityTime;

    ArrayList<NewEventImageDto> eventImageList;
    ArrayList<NewEventMusicDto> eventMusicList;

    String homeColor;
    String homeFont;
    String awayColor;
    String awayFont;

    String openchatUrl;
    String openchatImg;

    int volumeValue = -1;

    String eventBkcolor;
    Integer autoRunState;

    String cheerUrl1;
    String cheerUrl2;

    Boolean volumeSync;

    List<NewEventTagDto> tags;

    Integer animationCount;
    String emoji;
    String animationColor;
    String qrText;

    String logoImg;
    String bottomAd;
    String homeTitleImg;
    String awayTitleImg;
    Boolean awayShowState;
    String bottomAdUrl;
}
