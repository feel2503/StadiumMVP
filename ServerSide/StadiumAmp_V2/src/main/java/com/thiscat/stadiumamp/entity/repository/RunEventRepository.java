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

    Optional<RunEvent> findByEvent(Event event);

    @Query(
            value = " SELECT * FROM run_event " +
                    "   WHERE event_id=:eventId order by reg_date_time desc limit 1 ",
            nativeQuery = true)
    Optional<RunEvent> findByEventLimit(Long eventId);

    Optional<RunEvent> findFirstByEventOrderByIdDesc(Event event);


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
}