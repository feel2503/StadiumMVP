package kr.co.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.thiscat.stadiumamp.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunEventDto  {
    private Long id;
    private Long stadiumServerId;

    Integer voteTime;
    Integer resultTime;

    Integer homeCount;
    String homeMusic;

    Integer awayCount;
    String awayMusic;

    String defaultMusic;

    String homeImg;
    String awayImg;
    String defaultImg;

    String eventState;

    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
}
