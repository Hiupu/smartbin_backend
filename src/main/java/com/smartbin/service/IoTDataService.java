package com.smartbin.service;

import com.smartbin.dto.IoTDataRequest;
import com.smartbin.entity.Bin;
import com.smartbin.entity.BinDataRecord;
import com.smartbin.entity.Device;
import com.smartbin.repository.BinDataRecordRepository;
import com.smartbin.repository.BinRepository;
import com.smartbin.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class IoTDataService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private BinDataRecordRepository dataRecordRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Xử lý dữ liệu từ IoT device
     * Chỉ cần level và deviceCode
     */
    // ... existing code ...

    public BinDataRecord processSensorData(IoTDataRequest request) {
        System.out.println("=== PROCESSING IOT DATA ===");
        System.out.println("Device ID: " + request.getDeviceId() + ", Bin ID: " + request.getBinId() + ", Waste Level: "
                + request.getWasteLevel());

        try {
            // Tìm device
            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElseThrow(() -> new RuntimeException("Device not found: " + request.getDeviceId()));

            // Tìm bin
            Bin bin = binRepository.findById(request.getBinId())
                    .orElseThrow(() -> new RuntimeException("Bin not found: " + request.getBinId()));

            System.out.println("Found device: " + device.getDeviceCode() + ", bin: " + bin.getCode() + " (old level: "
                    + bin.getLevel() + ")");

            // Update device
            device.setLastPing(LocalDateTime.now());
            device.setIsActive(true);
            deviceRepository.save(device);

            // Update bin
            int previousLevel = bin.getLevel() != null ? bin.getLevel() : 0;
            bin.setLevel(request.getWasteLevel());
            bin.setOnline(true);
            binRepository.save(bin);

            System.out.println("Updated device ping and bin level to: " + bin.getLevel());

            // Trigger notifications async
            notificationService.evaluateAndNotify(bin, previousLevel, request.getWasteLevel());

            // Create data record
            BinDataRecord dataRecord = new BinDataRecord();
            dataRecord.setLevel(request.getWasteLevel());
            dataRecord.setBin(bin);
            dataRecord.setDevice(device); // Set device_id to link with sensor
            dataRecord.setRecordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now());

            // Set optional fields
            if (request.getTemperature() != null)
                dataRecord.setTemperature(request.getTemperature());
            if (request.getHumidity() != null)
                dataRecord.setHumidity(request.getHumidity());
            if (request.getBatteryLevel() != null)
                dataRecord.setBatteryLevel(request.getBatteryLevel());
            if (request.getSignalStrength() != null)
                dataRecord.setSignalStrength(request.getSignalStrength());
            if (request.getIsDoorOpen() != null)
                dataRecord.setIsDoorOpen(request.getIsDoorOpen());
            if (request.getWeightKg() != null)
                dataRecord.setWeightKg(request.getWeightKg());

            dataRecordRepository.save(dataRecord);

            System.out.println("Saved data record with ID: " + dataRecord.getId());

            // Log data processing completion
            System.out.println("Data processing completed successfully");

            return dataRecord;

        } catch (Exception e) {
            System.err.println("ERROR in processSensorData: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Update device ping status
     */
    public void updateDevicePing(String deviceCode) {
        Device device = deviceRepository.findByDeviceCode(deviceCode)
                .orElseThrow(() -> new RuntimeException("Device not found: " + deviceCode));

        device.setLastPing(LocalDateTime.now());
        device.setIsActive(true);
        deviceRepository.save(device);

        // Update associated bin online status
        if (device.getBin() != null) {
            device.getBin().setOnline(true);
            binRepository.save(device.getBin());
        }
    }

    /**
     * Check for offline devices (no ping in last 30 minutes)
     */
    public void checkOfflineDevices() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(30);
        var inactiveDevices = deviceRepository.findInactiveDevices(cutoffTime);

        for (Device device : inactiveDevices) {
            device.setIsActive(false);

            // Mark associated bin as offline
            if (device.getBin() != null) {
                device.getBin().setOnline(false);
                binRepository.save(device.getBin());
            }
        }

        if (!inactiveDevices.isEmpty()) {
            deviceRepository.saveAll(inactiveDevices);
        }
    }
}
