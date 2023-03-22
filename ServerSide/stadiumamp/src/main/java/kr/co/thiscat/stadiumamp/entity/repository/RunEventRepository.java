package kr.co.thiscat.stadiumamp.entity.repository;

import kr.co.thiscat.stadiumamp.entity.Runevent;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RunEventRepository extends JpaRepository<Runevent, Long> {
    Optional<Runevent> findById(Long aLong);
    List<Runevent> findAllByStadiumserver(Stadiumserver event);
    List<Runevent> findAllByStadiumserver(Stadiumserver event, Sort sort);

    Optional<Runevent> findFirstByStadiumserverOrderByIdDesc(Stadiumserver event);

    //List<Runevent> findAllOrderByStadiumserver();
}
