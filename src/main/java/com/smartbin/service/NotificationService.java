package com.smartbin.service;

import com.smartbin.entity.Bin;
import com.smartbin.entity.NotificationLog;
import com.smartbin.entity.NotificationSetting;
import com.smartbin.entity.User;
import com.smartbin.repository.NotificationLogRepository;
import com.smartbin.repository.NotificationSettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationSettingRepository settingRepository;

    @Autowired
    private NotificationLogRepository logRepository;

    @Autowired
    private EmailService emailService;

    @Async
    public void evaluateAndNotify(Bin bin, int previousLevel, int currentLevel) {
        try {
            List<NotificationSetting> settings = settingRepository.findAll();
            for (NotificationSetting setting : settings) {
                if (!shouldConsider(setting, bin))
                    continue;

                // FULL check first (higher priority)
                if (crossesUp(previousLevel, currentLevel, setting.getFullPercent())) {
                    if (isAllowedByCooldown(setting.getUser(), bin, NotificationLog.Type.FULL,
                            setting.getCooldownMinutes())) {
                        sendAndLog(setting, bin, NotificationLog.Type.FULL, currentLevel,
                                String.format("[SmartBin] Thùng %s ĐẦY (%d%%)", safeName(bin), currentLevel),
                                buildBody(bin, currentLevel, "đầy", setting.getFullPercent()));
                    }
                    continue; // avoid sending both in same tick
                }

                // Threshold check
                if (crossesUp(previousLevel, currentLevel, setting.getThresholdPercent())) {
                    if (isAllowedByCooldown(setting.getUser(), bin, NotificationLog.Type.THRESHOLD,
                            setting.getCooldownMinutes())) {
                        sendAndLog(setting, bin, NotificationLog.Type.THRESHOLD, currentLevel,
                                String.format("[SmartBin] Thùng %s chạm ngưỡng %d%% (%d%%)", safeName(bin),
                                        setting.getThresholdPercent(), currentLevel),
                                buildBody(bin, currentLevel, "chạm ngưỡng", setting.getThresholdPercent()));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error in evaluateAndNotify: {}", ex.getMessage(), ex);
        }
    }

    private boolean shouldConsider(NotificationSetting s, Bin bin) {
        if (Boolean.TRUE.equals(s.getNotifyAllBins()))
            return true;
        return s.getSelectedBins().contains(bin);
    }

    private boolean crossesUp(int prev, int curr, int threshold) {
        return prev < threshold && curr >= threshold;
    }

    private boolean isAllowedByCooldown(User user, Bin bin, NotificationLog.Type type, int cooldownMinutes) {
        LocalDateTime after = LocalDateTime.now().minusMinutes(cooldownMinutes);
        long count = logRepository.countByUserAndBinAndTypeAndSentAtAfter(user, bin, type, after);
        return count == 0;
    }

    private void sendAndLog(NotificationSetting setting, Bin bin, NotificationLog.Type type, int level,
            String subject, String body) {
        emailService.send(setting.getEmails(), subject, body);
        NotificationLog entry = new NotificationLog();
        entry.setUser(setting.getUser());
        entry.setBin(bin);
        entry.setType(type);
        entry.setLevelPercent(level);
        entry.setSentAt(LocalDateTime.now());
        logRepository.save(entry);
    }

    private String buildBody(Bin bin, int level, String typeLabel, int threshold) {
        return "Thùng: " + safeName(bin) +
                "\nMã: " + nullSafe(bin.getCode()) +
                "\nKhu vực: " + (bin.getArea() != null ? nullSafe(bin.getArea().getName()) : "(N/A)") +
                "\nMức hiện tại: " + level + "%" +
                "\nNgưỡng: " + threshold + "%" +
                "\nThời điểm: " + LocalDateTime.now() +
                "\n\nVui lòng kiểm tra Dashboard SmartBin.";
    }

    private String safeName(Bin bin) {
        String name = bin.getName();
        if (name == null || name.isBlank())
            return nullSafe(bin.getCode());
        return name;
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
