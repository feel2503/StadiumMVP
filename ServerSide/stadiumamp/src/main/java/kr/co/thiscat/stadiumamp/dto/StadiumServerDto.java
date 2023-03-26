package kr.co.thiscat.stadiumamp.dto;

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
public class StadiumServerDto {
    private Long id;

    String defaultMusic;
    String homeMusic1;
    String homeMusic2;
    String awayMusic1;
    String awayMusic2;
    String defaultImage;
    String homeImage;
    String awayImage;
}
