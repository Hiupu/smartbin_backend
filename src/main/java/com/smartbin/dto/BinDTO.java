package com.smartbin.dto;

import com.smartbin.entity.Bin.BinStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class BinDTO {

    private Long id;

    @NotBlank(message = "Bin code is required")
    @Size(max = 50, message = "Bin code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Bin name is required")
    @Size(max = 100, message = "Bin name must not exceed 100 characters")
    private String name;

    @Min(value = 0, message = "Level must be at least 0")
    @Max(value = 100, message = "Level must not exceed 100")
    private Integer level;

    private BinStatus status;

    private Boolean online;

    private Double latitude;

    private Double longitude;

    private LocalDateTime lastEmptied;

    private LocalDateTime nextScheduledEmpty;

    private LocalDateTime updatedAt;

    private String areaName;

    private Long areaId;

    // Constructors
    public BinDTO() {
    }

    public BinDTO(String code, String name, Integer level, BinStatus status, Boolean online, String areaName) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.status = status;
        this.online = online;
        this.areaName = areaName;
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BinStatus getStatus() {
        return status;
    }

    public void setStatus(BinStatus status) {
        this.status = status;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}
