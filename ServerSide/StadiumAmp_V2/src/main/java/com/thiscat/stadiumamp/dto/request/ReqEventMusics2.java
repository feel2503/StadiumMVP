package com.thiscat.stadiumamp.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thiscat.stadiumamp.dto.EventMusicDto;
import com.thiscat.stadiumamp.dto.EventMusicDto2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqEventMusics2 {
    Long eventId;
//    ArrayList<EventMusicDto2> homeMusics;
//    ArrayList<EventMusicDto2> awayMusics;

    ArrayList<Long> homeMusics;
    ArrayList<Long> awayMusics;
}
