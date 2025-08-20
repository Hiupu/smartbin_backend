package com.smartbin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bins")
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bin code is required")
    @Size(max = 50, message = "Bin code must not exceed 50 characters")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotBlank(message = "Bin name is required")
    @Size(max = 100, message = "Bin name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Min(value = 0, message = "Level must be at least 0")
    @Max(value = 100, message = "Level must not exceed 100")
    @Column(name = "level", nullable = false)
    private Integer level = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BinStatus status = BinStatus.ACTIVE;

    @Column(name = "online", nullable = false)
    private Boolean online = true;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(name = "latitude")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "last_emptied")
    private LocalDateTime lastEmptied;

    @Column(name = "next_scheduled_empty")
    private LocalDateTime nextScheduledEmpty;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", unique = true)
    private Device device;

    @OneToMany(mappedBy = "bin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BinDataRecord> dataRecords;

    // Constructors
    public Bin() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Bin(String code, String name, Area area) {
        this();
        this.code = code;
        this.name = name;
        this.area = area;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
        this.updatedAt = LocalDateTime.now();
        updateStatus();
    }

    public BinStatus getStatus() {
        return status;
    }

    public void setStatus(BinStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
        this.updatedAt = LocalDateTime.now();
        updateStatus();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getLastEmptied() {
        return lastEmptied;
    }

    public void setLastEmptied(LocalDateTime lastEmptied) {
        this.lastEmptied = lastEmptied;
    }

    public LocalDateTime getNextScheduledEmpty() {
        return nextScheduledEmpty;
    }

    public void setNextScheduledEmpty(LocalDateTime nextScheduledEmpty) {
        this.nextScheduledEmpty = nextScheduledEmpty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
        this.updatedAt = LocalDateTime.now();
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
        this.updatedAt = LocalDateTime.now();
    }

    public List<BinDataRecord> getDataRecords() {
        return dataRecords;
    }

    public void setDataRecords(List<BinDataRecord> dataRecords) {
        this.dataRecords = dataRecords;
    }

    // Helper methods
    private void updateStatus() {
        if (!this.online) {
            this.status = BinStatus.OFFLINE;
        } else if (this.level >= 90) {
            this.status = BinStatus.FULL;
        } else if (this.level >= 70) {
            this.status = BinStatus.WARNING;
        } else {
            this.status = BinStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        updateStatus();
    }

    // Enum for Bin Status
    public enum BinStatus {
        ACTIVE,
        WARNING,
        FULL,
        OFFLINE
    }
}
