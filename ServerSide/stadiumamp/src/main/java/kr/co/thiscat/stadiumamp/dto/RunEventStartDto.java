package kr.co.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.thiscat.stadiumamp.entity.BaseEntity;
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
public class RunEventStartDto {
    private Long stadiumServerId;
    String ssaid;

    Integer voteTime;
    Integer resultTime;

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
