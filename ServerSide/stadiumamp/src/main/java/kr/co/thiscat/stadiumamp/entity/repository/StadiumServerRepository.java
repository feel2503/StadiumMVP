package kr.co.thiscat.stadiumamp.entity.repository;

import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StadiumServerRepository extends JpaRepository<Stadiumserver, Long> {
    @Override
    Optional<Stadiumserver> findById(Long aLong);

    List<Stadiumserver> findAllByOrderByIdAsc();
}
