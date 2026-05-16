package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.thiscat.stadiumamp.entity.EventMusic;
import com.thiscat.stadiumamp.entity.value.TeamType;
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
public class NewEventMusicDto {

    @ApiModelProperty(value = "Team Type (Home : TEAM_HOME / TEAM_AWAY)")
    TeamType teamType;

    @ApiModelProperty(value = "재생 순서")
    int sequence;

    String musicUrl;
    String musicName;
    String musicYoutube;

}
