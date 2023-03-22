package kr.co.thiscat.stadiumamp.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Stadiumserver extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(unique = true)
    String name;


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
