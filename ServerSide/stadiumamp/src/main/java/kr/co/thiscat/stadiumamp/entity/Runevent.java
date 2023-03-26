package kr.co.thiscat.stadiumamp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

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
public class Runevent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "run_event_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="event_id")
    private Stadiumserver stadiumserver;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    Integer voteTime;
    Integer resultTime;

    Integer homeCount;
    Integer homeMusic1;
    Integer homeMusic2;

    Integer awayCount;
    Integer awayMusic1;
    Integer awayMusic2;

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
