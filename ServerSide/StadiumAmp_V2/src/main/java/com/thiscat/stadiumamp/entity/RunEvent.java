package com.thiscat.stadiumamp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;


@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class RunEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "run_event_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="event_id")
    private Event event;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    Integer homeCount;
    Integer home1Count;
    Integer home2Count;
    Integer home3Count;
    Integer home4Count;
    Integer home5Count;
    Integer home6Count;
    Integer home7Count;
    Integer home8Count;
    Integer home9Count;
    Integer home10Count;
    Integer home11Count;
    Integer home12Count;
    Integer home13Count;
    Integer home14Count;
    Integer home15Count;
    Integer home16Count;
    Integer home17Count;
    Integer home18Count;
    Integer home19Count;
    Integer home20Count;

    Integer awayCount;
    Integer away1Count;
    Integer away2Count;
    Integer away3Count;
    Integer away4Count;
    Integer away5Count;
    Integer away6Count;
    Integer away7Count;
    Integer away8Count;
    Integer away9Count;
    Integer away10Count;
    Integer away11Count;
    Integer away12Count;
    Integer away13Count;
    Integer away14Count;
    Integer away15Count;
    Integer away16Count;
    Integer away17Count;
    Integer away18Count;
    Integer away19Count;
    Integer away20Count;

    String eventState;

    Integer tag0;
    Integer tag1;
    Integer tag2;
    Integer tag3;
    Integer tag4;
    Integer tag5;
    Integer tag6;
    Integer tag7;
    Integer tag8;
    Integer tag9;



//    @Column(unique = true)
//    private String loginId; // VARCHAR(20) NULL DEFAULT NULL,
//    private String password; // VARCHAR(100) NULL DEFAULT NULL,
//    // @NotNull
//    private String name;
//
//    @NotNull
//    private String useYN; // CHAR(1) NOT NULL,
//
//
//    String mustChangePwd;
//    // @NotNull
//    String email;
//    String description;
//    String timeZone;
//
//    // for partner admin
//    @Column(name = "manage_account_id")
//    Long manageAccountId;
//
//    @Column(name = "refresh_token")
//    String refreshToken;

}
