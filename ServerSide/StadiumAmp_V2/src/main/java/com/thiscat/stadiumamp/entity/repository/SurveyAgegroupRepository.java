package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.SurveyAgegroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyAgegroupRepository extends JpaRepository<SurveyAgegroup, Long> {
    Optional<SurveyAgegroup> findByRunEvent(RunEvent runEvent);
}