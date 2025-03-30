package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.EventMusic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventMusicRepository extends JpaRepository<EventMusic, Long> {
    List<EventMusic> findAllByEventOrderBySequenceAsc(Event event);

    @Query(
            value = " select em.event_id, em.music_id, em.team_type, em.sequence, m.music_name, m.music_url "      +
                    "   FROM event_music em  "                                                   +
                    "   left join music m on em.music_id=m.music_id "           +
                    "   WHERE em.event_id = :eventId order by sequence  ",
            nativeQuery = true)
    List<Object[]> findAllEventMusic(Long eventId);

    void deleteAllByEvent(Event event);
}