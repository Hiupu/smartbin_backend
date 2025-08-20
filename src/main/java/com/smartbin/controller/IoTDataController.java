package com.smartbin.controller;

import com.smartbin.dto.IoTDataRequest;
import com.smartbin.entity.BinDataRecord;
import com.smartbin.service.IoTDataService;

import com.smartbin.repository.BinRepository;
import com.smartbin.repository.DeviceRepository;
import com.smartbin.repository.BinDataRecordRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/iot")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class IoTDataController {

    @Autowired
    private IoTDataService ioTDataService;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BinDataRecordRepository dataRecordRepository;

    /**
     * Nhận dữ liệu IoT từ thiết bị
     */
    @PostMapping("/data")
    public ResponseEntity<?> receiveIoTData(@RequestBody @Valid IoTDataRequest request) {
        try {
            BinDataRecord record = ioTDataService.processSensorData(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "IoT data received successfully");
            response.put("recordId", record.getId());
            response.put("binLevel", record.getLevel());
            response.put("timestamp", record.getRecordedAt());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error processing IoT data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Device heartbeat/ping
     */
    @PutMapping("/devices/{deviceCode}/ping")
    public ResponseEntity<Map<String, Object>> devicePing(@PathVariable String deviceCode) {
        try {
            ioTDataService.updateDevicePing(deviceCode);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Device ping received");
            response.put("deviceCode", deviceCode);
            response.put("timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error processing device ping: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Lấy dữ liệu sensor gần đây của một bin
     */
    @GetMapping("/data/{binId}/recent")
    public ResponseEntity<?> getRecentSensorData(@PathVariable Long binId,
            @RequestParam(defaultValue = "24") int hours) {
        try {
            // TODO: Implement service method to get recent sensor data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recent sensor data retrieved");
            response.put("binId", binId);
            response.put("hours", hours);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error getting recent sensor data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Kiểm tra database status
     */
    @GetMapping("/database-status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        try {
            Map<String, Object> response = new HashMap<>();

            // Đếm records trong các bảng
            long binCount = binRepository.count();
            long deviceCount = deviceRepository.count();
            long dataRecordCount = dataRecordRepository.count();

            response.put("success", true);
            response.put("binCount", binCount);
            response.put("deviceCount", deviceCount);
            response.put("dataRecordCount", dataRecordCount);
            response.put("timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error checking database: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Health check cho IoT endpoints
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "IoT Data Service");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("endpoints", new String[] {
                "POST /api/iot/data",
                "PUT /api/iot/devices/{code}/ping",
                "GET /api/iot/data/{binId}/recent"
        });

        return ResponseEntity.ok(response);
    }

}
