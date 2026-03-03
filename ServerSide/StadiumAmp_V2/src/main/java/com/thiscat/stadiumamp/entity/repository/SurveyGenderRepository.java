package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Music;
import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.SurveyGender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyGenderRepository extends JpaRepository<SurveyGender, Long> {
    Optional<SurveyGender> findByRunEvent(RunEvent runEvent);
}