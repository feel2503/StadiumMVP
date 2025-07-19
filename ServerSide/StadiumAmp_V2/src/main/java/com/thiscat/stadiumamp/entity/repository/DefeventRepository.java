package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Defevent;
import com.thiscat.stadiumamp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DefeventRepository extends JpaRepository<Defevent, Long> {
    Optional<Defevent> findById(Long id);
}