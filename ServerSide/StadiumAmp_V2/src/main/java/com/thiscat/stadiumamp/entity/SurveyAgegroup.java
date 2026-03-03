package com.thiscat.stadiumamp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class SurveyAgegroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_agegroup_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="run_event_id")
    private RunEvent runEvent;

    Integer ageValueNull = 0;
    Integer ageValue0 = 0;
    Integer ageValue1 = 0;
    Integer ageValue2 = 0;
    Integer ageValue3 = 0;
    Integer ageValue4 = 0;
    Integer ageValue5 = 0;
    Integer ageValue6 = 0;
}
