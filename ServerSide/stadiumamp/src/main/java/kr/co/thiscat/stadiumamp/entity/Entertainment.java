package kr.co.thiscat.stadiumamp.entity;

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
public class Entertainment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entertainment_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="event_id")
    private Stadiumserver stadiumserver;

    String ssaid;

    @Size(max = 500)
    String defaultMusic;
    @Size(max = 500)
    String homeMusic1;
    @Size(max = 500)
    String homeMusic2;
    @Size(max = 500)
    String awayMusic1;
    @Size(max = 500)
    String awayMusic2;
    @Size(max = 500)
    String defaultImage;
    @Size(max = 500)
    String homeImage;
    @Size(max = 500)
    String awayImage;
    @Size(max = 500)
    String webUrl;

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
