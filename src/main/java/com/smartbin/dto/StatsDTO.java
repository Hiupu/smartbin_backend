package com.smartbin.dto;

public class StatsDTO {

    private Long totalBins;
    private Long fullBins;
    private Long nearFullBins;
    private Long offlineBins;
    private Long activeBins;
    private Double averageLevel;

    // Constructors
    public StatsDTO() {
    }

    public StatsDTO(Long totalBins, Long fullBins, Long nearFullBins, Long offlineBins, Long activeBins,
            Double averageLevel) {
        this.totalBins = totalBins;
        this.fullBins = fullBins;
        this.nearFullBins = nearFullBins;
        this.offlineBins = offlineBins;
        this.activeBins = activeBins;
        this.averageLevel = averageLevel;
    }

    // Getters and Setters
    public Long getTotalBins() {
        return totalBins;
    }

    public void setTotalBins(Long totalBins) {
        this.totalBins = totalBins;
    }

    public Long getFullBins() {
        return fullBins;
    }

    public void setFullBins(Long fullBins) {
        this.fullBins = fullBins;
    }

    public Long getNearFullBins() {
        return nearFullBins;
    }

    public void setNearFullBins(Long nearFullBins) {
        this.nearFullBins = nearFullBins;
    }

    public Long getOfflineBins() {
        return offlineBins;
    }

    public void setOfflineBins(Long offlineBins) {
        this.offlineBins = offlineBins;
    }

    public Long getActiveBins() {
        return activeBins;
    }

    public void setActiveBins(Long activeBins) {
        this.activeBins = activeBins;
    }

    public Double getAverageLevel() {
        return averageLevel;
    }

    public void setAverageLevel(Double averageLevel) {
        this.averageLevel = averageLevel;
    }
}
