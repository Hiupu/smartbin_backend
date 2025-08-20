package com.smartbin.service;

import com.smartbin.entity.*;
import com.smartbin.repository.NotificationLogRepository;
import com.smartbin.repository.NotificationSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private NotificationSettingRepository settingRepository;
    private NotificationLogRepository logRepository;
    private FakeEmailService emailService;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService();
        settingRepository = mock(NotificationSettingRepository.class);
        logRepository = mock(NotificationLogRepository.class);
        emailService = new FakeEmailService();

        // inject mocks via reflection (since fields are package-private and annotated)
        TestUtils.setField(notificationService, "settingRepository", settingRepository);
        TestUtils.setField(notificationService, "logRepository", logRepository);
        TestUtils.setField(notificationService, "emailService", emailService);
    }

    @Test
    void sendsThresholdEmailOnCrossing() {
        User user = new User();
        user.setId(1L);
        user.setEmail("u@example.com");

        Bin bin = new Bin();
        bin.setId(10L);
        bin.setCode("B1");
        bin.setName("Bin One");

        NotificationSetting s = new NotificationSetting();
        s.setUser(user);
        s.setEmails("u@example.com");
        s.setNotifyAllBins(true);
        s.setThresholdPercent(70);
        s.setFullPercent(90);

        when(settingRepository.findAll()).thenReturn(List.of(s));
        when(logRepository.countByUserAndBinAndTypeAndSentAtAfter(any(), any(), any(), any())).thenReturn(0L);

        notificationService.evaluateAndNotify(bin, 65, 72);

        assertTrue(emailService.lastSubject != null && emailService.lastSubject.contains("chạm ngưỡng"));
        verify(logRepository).save(any(NotificationLog.class));
    }

    @Test
    void sendsFullEmailOnCrossing() {
        User user = new User();
        user.setId(1L);
        user.setEmail("u@example.com");

        Bin bin = new Bin();
        bin.setId(10L);
        bin.setCode("B1");
        bin.setName("Bin One");

        NotificationSetting s = new NotificationSetting();
        s.setUser(user);
        s.setEmails("u@example.com");
        s.setNotifyAllBins(true);
        s.setThresholdPercent(70);
        s.setFullPercent(90);

        when(settingRepository.findAll()).thenReturn(List.of(s));
        when(logRepository.countByUserAndBinAndTypeAndSentAtAfter(any(), any(), any(), any())).thenReturn(0L);

        notificationService.evaluateAndNotify(bin, 85, 95);

        assertTrue(emailService.lastSubject != null && emailService.lastSubject.contains("ĐẦY"));
        verify(logRepository).save(any(NotificationLog.class));
    }

    static class FakeEmailService extends EmailService {
        String lastRecipients;
        String lastSubject;
        String lastBody;

        @Override
        public void send(String recipientsCsv, String subject, String body) {
            this.lastRecipients = recipientsCsv;
            this.lastSubject = subject;
            this.lastBody = body;
        }
    }
}
