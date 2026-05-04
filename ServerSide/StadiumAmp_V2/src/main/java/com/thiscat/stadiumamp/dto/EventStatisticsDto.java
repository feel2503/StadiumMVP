package com.thiscat.stadiumamp.dto;

import java.time.LocalDateTime;


public interface EventStatisticsDto {
    Long getRunEventId();
    LocalDateTime getRegDateTime();
    String getMaxColumnName();
    Integer getMaxCountValue();
    Long getMusicId();
    String getMusicName();
    String getYoutubeUrl();
    String getDiffFromLastStart();
}
