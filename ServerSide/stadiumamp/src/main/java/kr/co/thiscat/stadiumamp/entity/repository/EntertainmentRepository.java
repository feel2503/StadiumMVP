package kr.co.thiscat.stadiumamp.entity.repository;

import kr.co.thiscat.stadiumamp.entity.Entertainment;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    Optional<Entertainment> findById(Long aLong);

    @Query(
            value = " SELECT * FROM entertainment " +
                    "   WHERE ssaid=:ssaid AND event_id=:event_id " ,
            nativeQuery = true)
    Optional<Entertainment> findByServerAndSsaid(@Param("ssaid")String ssaid, @Param("event_id")Long event_id);

}
