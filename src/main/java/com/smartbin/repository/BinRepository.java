package com.smartbin.repository;

import com.smartbin.entity.Bin;
import com.smartbin.entity.Bin.BinStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {

    Optional<Bin> findByCode(String code);

    List<Bin> findByAreaId(Long areaId);

    List<Bin> findByStatus(BinStatus status);

    List<Bin> findByOnline(Boolean online);

    @Query("SELECT b FROM Bin b JOIN FETCH b.area")
    List<Bin> findAllWithArea();

    @Query("SELECT b FROM Bin b JOIN FETCH b.area WHERE b.area.id = :areaId")
    List<Bin> findByAreaIdWithArea(@Param("areaId") Long areaId);

    @Query("SELECT b FROM Bin b JOIN FETCH b.area WHERE " +
            "(:search IS NULL OR LOWER(b.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:areaId IS NULL OR b.area.id = :areaId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:levelMin IS NULL OR b.level >= :levelMin) AND " +
            "(:levelMax IS NULL OR b.level <= :levelMax)")
    Page<Bin> findBinsWithFilters(@Param("search") String search,
            @Param("areaId") Long areaId,
            @Param("status") BinStatus status,
            @Param("levelMin") Integer levelMin,
            @Param("levelMax") Integer levelMax,
            Pageable pageable);

    // Statistics queries
    long countByStatus(BinStatus status);

    long countByOnline(Boolean online);

    @Query("SELECT COUNT(b) FROM Bin b WHERE b.level >= :level")
    long countByLevelGreaterThanEqual(@Param("level") Integer level);

    @Query("SELECT AVG(b.level) FROM Bin b WHERE b.online = true")
    Double getAverageLevel();

    boolean existsByCode(String code);
}
