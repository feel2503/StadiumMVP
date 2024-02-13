package com.thiscat.stadiumamp.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.querydsl.core.annotations.QueryProjection;
import com.thiscat.stadiumamp.entity.BaseEntity;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.EventMusic;
import com.thiscat.stadiumamp.entity.Music;
import com.thiscat.stadiumamp.entity.value.TeamType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMusicDao  {
    private Long eventId;
    private Long musicId;

    @ApiModelProperty(value = "Team Type (Home : TEAM_HOME / TEAM_AWAY)")
    TeamType teamType;

    @ApiModelProperty(value = "재생 순서")
    int sequence;

    String musicUrl;
    String musicName;

    public EventMusicDao(EventMusic eventMusic){
        this.eventId = eventMusic.getEvent().getId();

        this.teamType = eventMusic.getTeamType();
        this.sequence = eventMusic.getSequence();

        this.musicId = eventMusic.getMusic().getId();
        this.musicUrl = eventMusic.getMusic().getMusicUrl();
        this.musicName = eventMusic.getMusic().getMusicName();
    }

    @QueryProjection
    public EventMusicDao(Long eventId, Long musicId, String teamType, int sequence, String musicUrl, String musicName){
        this.eventId = eventId;
        this.musicId = musicId;
        this.teamType = TeamType.valueOf(teamType);
        this.sequence = sequence;

        this.musicUrl = musicUrl;
        this.musicName = musicName;
    }
}