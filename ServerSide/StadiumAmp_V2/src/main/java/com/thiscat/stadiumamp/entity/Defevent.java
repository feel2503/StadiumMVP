package com.thiscat.stadiumamp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;


@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Defevent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Size(max = 200)
    @Column(unique = true)
    String name;

    String homeName;
    String awayName;

    int triggerType;
    int triggerTime;
    int triggerVote;

    @Column(columnDefinition = "TEXT")
    String webUrl;

    @Column(columnDefinition = "TEXT")
    String adUrl;

    int continuityType;
    int continuityTime;

    String homeColor;
    String homeFont;
    String awayColor;
    String awayFont;

    @Column(columnDefinition = "TEXT")
    String openchatUrl;

    Integer volumeValue;

    String eventBkcolor;

    @Column(name = "auto_run_state")
    Integer autoRunState;

    @Column(columnDefinition = "TEXT")
    String openchatImg;
    @Column(columnDefinition = "TEXT")
    String webImg;

    @Column(columnDefinition = "TEXT")
    String cheerUrl1;
    @Column(columnDefinition = "TEXT")
    String cheerUrl2;

    @Column(columnDefinition = "TEXT")
    Integer animationCount;
    @Column(columnDefinition = "TEXT")
    String emoji;
    @Column(columnDefinition = "TEXT")
    String animationColor;

    Boolean volumeSync;
}
