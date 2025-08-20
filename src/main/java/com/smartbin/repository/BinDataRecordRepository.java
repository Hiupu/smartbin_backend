package com.smartbin.repository;

import com.smartbin.entity.BinDataRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BinDataRecordRepository extends JpaRepository<BinDataRecord, Long> {

        List<BinDataRecord> findByBinIdOrderByRecordedAtDesc(Long binId);

        List<BinDataRecord> findByDeviceIdOrderByRecordedAtDesc(Long deviceId);

        Page<BinDataRecord> findByBinIdOrderByRecordedAtDesc(Long binId, Pageable pageable);

        @Query("SELECT bdr FROM BinDataRecord bdr WHERE bdr.bin.id = :binId AND " +
                        "bdr.recordedAt BETWEEN :startDate AND :endDate ORDER BY bdr.recordedAt DESC")
        List<BinDataRecord> findByBinIdAndDateRange(@Param("binId") Long binId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT bdr FROM BinDataRecord bdr WHERE bdr.recordedAt >= :since ORDER BY bdr.recordedAt DESC")
        List<BinDataRecord> findRecentRecords(@Param("since") LocalDateTime since);

        @Query("SELECT AVG(bdr.level) FROM BinDataRecord bdr WHERE bdr.bin.id = :binId AND " +
                        "bdr.recordedAt >= :since")
        Double getAverageLevelForBin(@Param("binId") Long binId, @Param("since") LocalDateTime since);

        @Query("SELECT bdr FROM BinDataRecord bdr WHERE bdr.bin.id = :binId ORDER BY bdr.recordedAt DESC LIMIT 1")
        BinDataRecord findLatestByBinId(@Param("binId") Long binId);

        @Query("SELECT COUNT(bdr) FROM BinDataRecord bdr WHERE bdr.recordedAt >= :since")
        long countRecordsSince(@Param("since") LocalDateTime since);

        // Count records for a specific bin
        long countByBinId(Long binId);

        // Delete old records (for data cleanup)
        void deleteByRecordedAtBefore(LocalDateTime cutoffDate);
}
