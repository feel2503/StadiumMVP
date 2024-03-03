package com.thiscat.stadiumamp.entity.repository;

import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.EventImage;
import com.thiscat.stadiumamp.entity.RunEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findAllByEventOrderByImageTypeAsc(Event event);

    @Query(
            value = " select i.image_url from event e "      +
                    "   left join event_image ei on e.event_id=ei.event_id  "                                                   +
                    "   left join image i on ei.image_id = i.image_id "           +
                    "   where e.event_id = :eventId and image_type=:type   ",
            nativeQuery = true)
    String findTypeEventImage(Long eventId, String type);

}