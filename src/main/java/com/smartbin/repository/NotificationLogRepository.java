package com.smartbin.repository;

import com.smartbin.entity.Bin;
import com.smartbin.entity.NotificationLog;
import com.smartbin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    Optional<NotificationLog> findFirstByUserAndBinAndTypeOrderBySentAtDesc(User user, Bin bin,
            NotificationLog.Type type);

    long countByUserAndBinAndTypeAndSentAtAfter(User user, Bin bin, NotificationLog.Type type, LocalDateTime after);
}
