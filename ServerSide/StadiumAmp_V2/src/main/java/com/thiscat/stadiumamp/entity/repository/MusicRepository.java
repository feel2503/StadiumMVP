package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
}