package kr.co.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventInfoDto {
    private Long id;
    private Long stadiumServerId;

    Integer voteTime;
    Integer resultTime;
    Integer homeCount;
    Integer awayCount;
    String eventState;

    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    String result;

    String defaultMusic;
    String homeMusic1;
    String homeMusic2;
    String awayMusic1;
    String awayMusic2;
    String defaultImage;
    String homeImage;
    String awayImage;
    String webUrl;
}
