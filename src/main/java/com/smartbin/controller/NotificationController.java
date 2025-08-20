package com.smartbin.controller;

import com.smartbin.dto.NotificationSettingDTO;
import com.smartbin.entity.User;
import com.smartbin.service.EmailService;
import com.smartbin.service.NotificationSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationSettingService settingService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/settings")
    public ResponseEntity<NotificationSettingDTO> getSettings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(settingService.getForUser(user));
    }

    @PutMapping("/settings")
    public ResponseEntity<NotificationSettingDTO> updateSettings(@RequestBody NotificationSettingDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(settingService.updateForUser(user, dto));
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> sendTest(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        // Lấy email nhận thông báo từ cài đặt người dùng
        NotificationSettingDTO setting = settingService.getForUser(user);
        String recipientsCsv = (setting.getEmails() != null && !setting.getEmails().isBlank())
                ? setting.getEmails()
                : user.getEmail();
        emailService.send(recipientsCsv, "[SmartBin] Test thông báo", "Đây là email test từ SmartBin.");
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Sent to: " + recipientsCsv);
        return ResponseEntity.ok(resp);
    }
}
