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
    List<EventImage> findAllByEventOrderByIdDesc(Event event);

    @Query(
            value = " select * from event_image  "      +
                    "   where event_id = :eventId order by image_id  ",
            nativeQuery = true)
    List<EventImage> findAllByEventOrderByImageId(Long eventId);

    @Query(
            value = " select i.image_url from event e "      +
                    "   left join event_image ei on e.event_id=ei.event_id  "                                                   +
                    "   left join image i on ei.image_id = i.image_id "           +
                    "   where e.event_id = :eventId and image_type=:type limit 1  ",
            nativeQuery = true)
    String findTypeEventImage(Long eventId, String type);

}