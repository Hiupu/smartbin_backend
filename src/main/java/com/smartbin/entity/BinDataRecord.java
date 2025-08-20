package com.smartbin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "bin_data_records", indexes = {
        @Index(name = "idx_device_timestamp", columnList = "device_id, recorded_at"),
        @Index(name = "idx_bin_timestamp", columnList = "bin_id, recorded_at")
})
public class BinDataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Level must be at least 0")
    @Max(value = 100, message = "Level must not exceed 100")
    @Column(name = "level", nullable = false)
    private Integer level;

    @DecimalMin(value = "-50.0", message = "Temperature must be at least -50°C")
    @DecimalMax(value = "100.0", message = "Temperature must not exceed 100°C")
    @Column(name = "temperature")
    private Double temperature;

    @DecimalMin(value = "0.0", message = "Humidity must be at least 0%")
    @DecimalMax(value = "100.0", message = "Humidity must not exceed 100%")
    @Column(name = "humidity")
    private Double humidity;

    @DecimalMin(value = "0.0", message = "Battery level must be at least 0%")
    @DecimalMax(value = "100.0", message = "Battery level must not exceed 100%")
    @Column(name = "battery_level")
    private Double batteryLevel;

    @DecimalMin(value = "-100.0", message = "Signal strength must be at least -100 dBm")
    @DecimalMax(value = "100.0", message = "Signal strength must not exceed 100 dBm")
    @Column(name = "signal_strength")
    private Double signalStrength;

    @Column(name = "is_door_open")
    private Boolean isDoorOpen = false;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id", nullable = false)
    private Bin bin;

    // Constructors
    public BinDataRecord() {
        this.createdAt = LocalDateTime.now();
        this.recordedAt = LocalDateTime.now();
    }

    public BinDataRecord(Integer level, Device device, Bin bin) {
        this();
        this.level = level;
        this.device = device;
        this.bin = bin;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }
}
