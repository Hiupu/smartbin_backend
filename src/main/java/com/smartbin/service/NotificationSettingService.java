package com.smartbin.service;

import com.smartbin.dto.NotificationSettingDTO;
import com.smartbin.entity.Bin;
import com.smartbin.entity.NotificationSetting;
import com.smartbin.entity.User;
import com.smartbin.repository.BinRepository;
import com.smartbin.repository.NotificationSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationSettingService {

    @Autowired
    private NotificationSettingRepository settingRepository;

    @Autowired
    private BinRepository binRepository;

    public NotificationSettingDTO getForUser(User user) {
        NotificationSetting setting = settingRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationSetting s = new NotificationSetting();
                    s.setUser(user);
                    s.setEmails(user.getEmail());
                    s.setNotifyAllBins(true);
                    return settingRepository.save(s);
                });
        return toDTO(setting);
    }

    public NotificationSettingDTO updateForUser(User user, NotificationSettingDTO dto) {
        NotificationSetting setting = settingRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationSetting s = new NotificationSetting();
                    s.setUser(user);
                    return s;
                });

        if (dto.getEmails() != null)
            setting.setEmails(dto.getEmails());
        if (dto.getNotifyAllBins() != null)
            setting.setNotifyAllBins(dto.getNotifyAllBins());
        if (dto.getThresholdPercent() != null)
            setting.setThresholdPercent(dto.getThresholdPercent());
        if (dto.getFullPercent() != null)
            setting.setFullPercent(dto.getFullPercent());
        if (dto.getCooldownMinutes() != null)
            setting.setCooldownMinutes(dto.getCooldownMinutes());
        if (dto.getHysteresisPercent() != null)
            setting.setHysteresisPercent(dto.getHysteresisPercent());

        if (Boolean.FALSE.equals(setting.getNotifyAllBins())) {
            List<Bin> bins = binRepository.findAllById(dto.getSelectedBinIds());
            setting.setSelectedBins(new HashSet<>(bins));
        } else {
            // Use mutable empty set to avoid ImmutableCollections in JPA merge
            setting.setSelectedBins(new HashSet<>());
        }

        setting = settingRepository.save(setting);
        return toDTO(setting);
    }

    private NotificationSettingDTO toDTO(NotificationSetting s) {
        NotificationSettingDTO dto = new NotificationSettingDTO();
        dto.setEmails(s.getEmails());
        dto.setNotifyAllBins(s.getNotifyAllBins());
        dto.setThresholdPercent(s.getThresholdPercent());
        dto.setFullPercent(s.getFullPercent());
        dto.setCooldownMinutes(s.getCooldownMinutes());
        dto.setHysteresisPercent(s.getHysteresisPercent());
        dto.setSelectedBinIds(s.getSelectedBins().stream().map(Bin::getId).collect(Collectors.toList()));
        return dto;
    }
}
