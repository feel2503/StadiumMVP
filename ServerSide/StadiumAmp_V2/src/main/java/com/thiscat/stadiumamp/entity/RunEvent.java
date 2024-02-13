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

    Integer awayCount;
    Integer away1Count;
    Integer away2Count;
    Integer away3Count;
    Integer away4Count;
    Integer away5Count;

    String eventState;



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
