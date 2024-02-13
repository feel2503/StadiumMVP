package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class VoteResultDto {
    private String home;
    private String away;
    int homeCount;
    int awayCount;

    String eventState;
    boolean playVideo;

    public VoteResultDto(String home, String away, int homeCount, int awayCount)
    {
        this.home = home;
        this.away = away;
        this.homeCount = homeCount;
        this.awayCount = awayCount;
    }

}
