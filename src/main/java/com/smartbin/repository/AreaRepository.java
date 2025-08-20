package com.smartbin.repository;

import com.smartbin.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByName(String name);

    @Query("SELECT a FROM Area a LEFT JOIN FETCH a.bins")
    List<Area> findAllWithBins();

    @Query("SELECT a FROM Area a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Area> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
