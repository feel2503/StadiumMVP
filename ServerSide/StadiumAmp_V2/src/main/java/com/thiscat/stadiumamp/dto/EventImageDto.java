package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thiscat.stadiumamp.entity.EventImage;
import com.thiscat.stadiumamp.entity.EventMusic;
import com.thiscat.stadiumamp.entity.value.ImageType;
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
public class EventImageDto {
    private Long eventId;
    private Long imageId;

    ImageType imageType;

    String imageUrl;
    String imageName;

    public EventImageDto(EventImage eventImage){
        this.eventId = eventImage.getEvent().getId();

        this.imageType = eventImage.getImageType();

        this.imageId = eventImage.getImage().getId();
        this.imageUrl = eventImage.getImage().getImageUrl();
        this.imageName = eventImage.getImage().getImageName();
    }
}
