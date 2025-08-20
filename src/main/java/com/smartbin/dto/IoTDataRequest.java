package com.smartbin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class IoTDataRequest {

    @NotNull(message = "Device ID is required")
    private Long deviceId;

    @NotNull(message = "Bin ID is required")
    private Long binId;

    @NotNull(message = "Waste level is required")
    @Min(value = 0, message = "Waste level must be at least 0")
    @Max(value = 100, message = "Waste level must not exceed 100")
    private Integer wasteLevel;

    // Optional fields for enhanced data
    private Double temperature;
    private Double humidity;
    private Double batteryLevel;
    private Double signalStrength;
    private Boolean isDoorOpen;
    private Double weightKg;

    private LocalDateTime recordedAt;

    // Constructors
    public IoTDataRequest() {
        this.recordedAt = LocalDateTime.now();
    }

    public IoTDataRequest(Long deviceId, Long binId, Integer wasteLevel) {
        this();
        this.deviceId = deviceId;
        this.binId = binId;
        this.wasteLevel = wasteLevel;
    }

    // Getters and Setters
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public Integer getWasteLevel() {
        return wasteLevel;
    }

    public void setWasteLevel(Integer wasteLevel) {
        this.wasteLevel = wasteLevel;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Boolean getIsDoorOpen() {
        return isDoorOpen;
    }

    public void setIsDoorOpen(Boolean isDoorOpen) {
        this.isDoorOpen = isDoorOpen;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
