package com.thiscat.stadiumamp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thiscat.stadiumamp.entity.EventImage;
import com.thiscat.stadiumamp.entity.value.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventTopDto {
    int sequence;
    String name;
    int count;

    public EventTopDto(int sequence, String name, int count) {
        this.sequence = sequence;
        this.name = removeExtension(name);
        this.count = count;
    }

    private String removeExtension(String fileName) {
        // 1. 확장자 제거
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            fileName = fileName.substring(0, lastIndex);
        }
        // 2. 괄호 ( ... ) 제거
        int openParen = fileName.lastIndexOf('(');
        if (openParen != -1) {
            fileName = fileName.substring(0, openParen).trim();
        }

        return fileName;
    }
}
