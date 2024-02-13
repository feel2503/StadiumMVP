package com.thiscat.stadiumamp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import static javax.persistence.GenerationType.IDENTITY;


@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Event extends BaseEntity {
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
    String awayColor;
}
