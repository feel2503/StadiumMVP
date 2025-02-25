package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Cheertag;
import com.thiscat.stadiumamp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheertagRepository extends JpaRepository<Cheertag, Long> {
    Optional<Cheertag> findById(Long id);
    List<Cheertag> findAllByEventOrderById(Event event);

    void deleteAllByEvent(Event event);
}