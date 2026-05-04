package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.dto.EventStatisticsDto;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.RunEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface RunEventRepository extends JpaRepository<RunEvent, Long> {

    Optional<RunEvent> findById(Long id);
    @Query("select a from RunEvent a where a.event.id=:eventId order by a.startDateTime desc ")
    List<RunEvent> findByEvent(@Param("eventId") Long eventId, Pageable page );

    @Query(
            value = " SELECT * FROM run_event " +
                    "   WHERE event_id=:eventId order by reg_date_time desc limit 1 ",
            nativeQuery = true)
    Optional<RunEvent> findByEventLimit(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            value = "SELECT * FROM run_event " +
                    " WHERE event_id=:eventId order by reg_date_time desc limit 1",
            nativeQuery = true
    )
    Optional<RunEvent> findByEventLimitForUpdate(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RunEvent> findFirstByEventIdAndEventStateOrderByIdDesc(Long eventId, String eventState);

    Optional<RunEvent> findFirstByEventOrderByIdDesc(Event event);

    boolean existsByEventIdAndEventState(Long eventId, String eventState);

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
            "    ]) AS count_value, generate_series(0, 19) AS sequence " +
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
            "    ]) AS count_value, generate_series(0, 19) AS sequence " +
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

    @Query("select a from RunEvent a where a.event.id=:eventId and a.id between :fromId and :toId order by a.startDateTime desc ")
    List<RunEvent> findByEventAndId(Long eventId, Long fromId, Long toId);


    @Query(value = " select m.youtube_url " +
            " from ( " +
            " select " +
            "    re.event_id,  " +
            "    array_position( " +
            "        array[ " +
            "            re.home1count, re.home2count, re.home3count, re.home4count, re.home5count, " +
            "            re.home6count, re.home7count, re.home8count, re.home9count, re.home10count, " +
            "            re.home11count, re.home12count, re.home13count, re.home14count, re.home15count, " +
            "            re.home16count, re.home17count, re.home18count, re.home19count, re.home20count " +
            "        ], " +
            "        greatest( " +
            "            re.home1count, re.home2count, re.home3count, re.home4count, re.home5count, " +
            "            re.home6count, re.home7count, re.home8count, re.home9count, re.home10count, " +
            "            re.home11count, re.home12count, re.home13count, re.home14count, re.home15count, " +
            "            re.home16count, re.home17count, re.home18count, re.home19count, re.home20count " +
            "        ) " +
            "    ) as max_home_index " +
            " from public.run_event re " +
            " where re.run_event_id = :runEventId and re.event_id = :eventId " +
            " ) as sub " +
            " join public.event_music em " +
            " on em.event_id = sub.event_id " +
            " and em.team_type = 'TEAM_HOME' " +
            " and em.sequence + 1 = sub.max_home_index " +
            " join public.music m " +
            " on em.music_id = m.music_id ",
         nativeQuery = true)
    String findHomeYoutubeUrlByRunEventIdAndEventId(Long runEventId, Long eventId);

    @Query(value = " select m.youtube_url " +
            " from ( " +
            " select " +
            "    re.event_id,  " +
            "    array_position( " +
            "        array[ " +
            "            re.away1count, re.away2count, re.away3count, re.away4count, re.away5count, " +
            "            re.away6count, re.away7count, re.away8count, re.away9count, re.away10count, " +
            "            re.away11count, re.away12count, re.away13count, re.away14count, re.away15count, " +
            "            re.away16count, re.away17count, re.away18count, re.away19count, re.away20count " +
            "        ], " +
            "        greatest( " +
            "            re.away1count, re.away2count, re.away3count, re.away4count, re.away5count, " +
            "            re.away6count, re.away7count, re.away8count, re.away9count, re.away10count, " +
            "            re.away11count, re.away12count, re.away13count, re.away14count, re.away15count, " +
            "            re.away16count, re.away17count, re.away18count, re.away19count, re.away20count " +
            "        ) " +
            "    ) as max_home_index " +
            " from public.run_event re " +
            " where re.run_event_id = :runEventId and re.event_id = :eventId " +
            " ) as sub " +
            " join public.event_music em " +
            " on em.event_id = sub.event_id " +
            " and em.team_type = 'TEAM_AWAY' " +
            " and em.sequence + 1 = sub.max_home_index " +
            " join public.music m " +
            " on em.music_id = m.music_id ",
            nativeQuery = true)
    String findAwayYoutubeUrlByRunEventIdAndEventId(Long runEventId, Long eventId);


    @Query(
            value = "" +
                    "WITH LastStart AS (  " +
                    "        SELECT reg_date_time, event_id  " +
                    "        FROM public.run_event   " +
                    "        WHERE event_id = :eventId AND event_state = 'START'  " +
                    "        ORDER BY reg_date_time DESC   " +
                    "        LIMIT 1  " +
                    "    ),  " +
                    "    TargetEvents AS (  " +
                    "        SELECT *  " +
                    "        FROM public.run_event  " +
                    "        WHERE event_id = :eventId   " +
                    "          AND event_state = 'STOP'  " +
                    "          AND reg_date_time < (SELECT reg_date_time FROM LastStart)  " +
                    "        ORDER BY reg_date_time DESC  " +
                    "        LIMIT 5  " +
                    "    ),  " +
                    "    MaxColumnStats AS (  " +
                    "        SELECT   " +
                    "            t.*,  " +
                    "            mx.col_name AS max_column_name,  " +
                    "            mx.max_val AS max_count_value,  " +
                    "            CASE WHEN mx.col_name LIKE 'home%' THEN 'TEAM_HOME' ELSE 'TEAM_AWAY' END as target_team_type,  " +
                    "            (CAST(SUBSTRING(mx.col_name FROM '[0-9]+') AS INTEGER) - 1) as target_sequence,  " +
                    "            ROW_NUMBER() OVER (ORDER BY t.reg_date_time DESC) as row_num  " +
                    "        FROM TargetEvents t  " +
                    "        CROSS JOIN LATERAL (  " +
                    "            SELECT col_name, max_val  " +
                    "            FROM (VALUES   " +
                    "                ('home1count', t.home1count), ('home2count', t.home2count), ('home3count', t.home3count), ('home4count', t.home4count), ('home5count', t.home5count),  " +
                    "                ('home6count', t.home6count), ('home7count', t.home7count), ('home8count', t.home8count), ('home9count', t.home9count), ('home10count', t.home10count),  " +
                    "                ('home11count', t.home11count), ('home12count', t.home12count), ('home13count', t.home13count), ('home14count', t.home14count), ('home15count', t.home15count),  " +
                    "                ('home16count', t.home16count), ('home17count', t.home17count), ('home18count', t.home18count), ('home19count', t.home19count), ('home20count', t.home20count),  " +
                    "                ('away1count', t.away1count), ('away2count', t.away2count), ('away3count', t.away3count), ('away4count', t.away4count), ('away5count', t.away5count),  " +
                    "                ('away6count', t.away6count), ('away7count', t.away7count), ('away8count', t.away8count), ('away9count', t.away9count), ('away10count', t.away10count),  " +
                    "                ('away11count', t.away11count), ('away12count', t.away12count), ('away13count', t.away13count), ('away14count', t.away14count), ('away15count', t.away15count),  " +
                    "                ('away16count', t.away16count), ('away17count', t.away17count), ('away18count', t.away18count), ('away19count', t.away19count), ('away20count', t.away20count)  " +
                    "            ) AS v(col_name, max_val)  " +
                    "            ORDER BY max_val DESC  " +
                    "            LIMIT 1  " +
                    "        ) mx  " +
                    "    )  " +
                    "    SELECT   " +
                    "        m.run_event_id as runEventId,  " +
                    "        m.reg_date_time as regDateTime,  " +
                    "        m.max_column_name as maxColumnName,  " +
                    "        m.max_count_value as maxCountValue,  " +
                    "        ms.music_id as musicId,  " +
                    "        ms.music_name as musicName,  " +
                    "        ms.youtube_url as youtubeUrl,  " +
                    "        CASE   " +
                    "            WHEN m.row_num = 1 THEN '이전 이벤트 결과'  " +
                    "            ELSE CAST(TRUNC(EXTRACT(EPOCH FROM ((SELECT reg_date_time FROM LastStart) - m.reg_date_time)) / 60) AS TEXT)  " +
                    "        END AS diffFromLastStart  " +
                    "    FROM MaxColumnStats m  " +
                    "    LEFT JOIN public.event_music em ON   " +
                    "        em.event_id = m.event_id   " +
                    "        AND em.team_type = m.target_team_type   " +
                    "        AND em.sequence = m.target_sequence  " +
                    "    LEFT JOIN public.music ms ON ms.music_id = em.music_id  " +
                    "    ORDER BY m.reg_date_time DESC",
            nativeQuery = true)
    List<EventStatisticsDto> findTop5EventStatistics(Long eventId);


}