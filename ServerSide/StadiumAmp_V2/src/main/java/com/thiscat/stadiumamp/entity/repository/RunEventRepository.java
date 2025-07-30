package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.RunEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RunEventRepository extends JpaRepository<RunEvent, Long> {

    Optional<RunEvent> findById(Long id);

    List<RunEvent> findByEvent(Event event);

    @Query(
            value = " SELECT * FROM run_event " +
                    "   WHERE event_id=:eventId order by reg_date_time desc limit 1 ",
            nativeQuery = true)
    Optional<RunEvent> findByEventLimit(Long eventId);

    Optional<RunEvent> findFirstByEventOrderByIdDesc(Event event);


    Page<RunEvent> findAll(Pageable var1);


    List<RunEvent> findAllByOrderByIdAsc();
    List<RunEvent> findTop30ByOrderByIdDesc();

    @Query(
            value = " SELECT event_id FROM run_event " +
                    "   WHERE run_event_id=:runEventId ",
            nativeQuery = true)
    Long findEventId(Long runEventId);

    @Query(
            value = " SELECT * FROM run_event " +
            "   WHERE event_id=:eventId " +
                    " AND run_event_id > :runEventId order by run_event_id asc limit 1 ;",
            nativeQuery = true)
    Optional<RunEvent> findNextRunEvent(Long eventId, Long runEventId);

    //Optional<RunEvent> findFirstByIdGreaterThanAndByEventOrderByIdAsc(Long runEventId, Event event);


    @Query(value = "WITH home_counts  AS ( " +
            "    SELECT  unnest(ARRAY[ " +
            "        COALESCE(home1count, 0), COALESCE(home2count, 0), COALESCE(home3count, 0), " +
            "        COALESCE(home4count, 0), COALESCE(home5count, 0), COALESCE(home6count, 0), " +
            "        COALESCE(home7count, 0), COALESCE(home8count, 0), COALESCE(home9count, 0), " +
            "        COALESCE(home10count, 0), COALESCE(home11count, 0), COALESCE(home12count, 0), " +
            "        COALESCE(home13count, 0), COALESCE(home14count, 0), COALESCE(home15count, 0), " +
            "        COALESCE(home16count, 0), COALESCE(home17count, 0), COALESCE(home18count, 0), " +
            "        COALESCE(home19count, 0), COALESCE(home20count, 0) " +
            "    ]) AS count_value, generate_series(1, 20) AS sequence " +
            "    FROM run_event " +
            "    WHERE run_event_id = :runEventId and event_id = :eventId  " +
            ") " +
            "SELECT ec.sequence, m.music_name, ec.count_value " +
            "FROM home_counts ec " +
            "    JOIN public.event_music em ON em.event_id=:eventId AND em.sequence=ec.sequence " +
            "    JOIN public.music m ON em.music_id = m.music_id  " +
            "    WHERE em.team_type='TEAM_HOME' " +
            "    ORDER BY ec.count_value DESC LIMIT 3; ",

            nativeQuery = true)
    List<Object[]> findHomeTopCounts(Long runEventId, Long eventId);


    @Query(value = "WITH away_counts  AS ( " +
            "    SELECT  unnest(ARRAY[ " +
            "        COALESCE(away1count, 0), COALESCE(away2count, 0), COALESCE(away3count, 0), " +
            "        COALESCE(away4count, 0), COALESCE(away5count, 0), COALESCE(away6count, 0), " +
            "        COALESCE(away7count, 0), COALESCE(away8count, 0), COALESCE(away9count, 0), " +
            "        COALESCE(away10count, 0), COALESCE(away11count, 0), COALESCE(away12count, 0), " +
            "        COALESCE(away13count, 0), COALESCE(away14count, 0), COALESCE(away15count, 0), " +
            "        COALESCE(away16count, 0), COALESCE(away17count, 0), COALESCE(away18count, 0), " +
            "        COALESCE(away19count, 0), COALESCE(away20count, 0) " +
            "    ]) AS count_value, generate_series(1, 20) AS sequence " +
            "    FROM run_event " +
            "    WHERE run_event_id = :runEventId and event_id = :eventId  " +
            ") " +
            "SELECT ec.sequence, m.music_name, ec.count_value " +
            "FROM away_counts ec " +
            "    JOIN public.event_music em ON em.event_id=:eventId AND em.sequence=ec.sequence " +
            "    JOIN public.music m ON em.music_id = m.music_id  " +
            "    WHERE em.team_type='TEAM_AWAY' " +
            "    ORDER BY ec.count_value DESC LIMIT 3; ",

            nativeQuery = true)
    List<Object[]> findAwayTopCounts(Long runEventId, Long eventId);

}