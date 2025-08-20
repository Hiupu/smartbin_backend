package com.smartbin.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationSettingDTO {
    private String emails;
    private Boolean notifyAllBins;
    private List<Long> selectedBinIds = new ArrayList<>();
    private Integer thresholdPercent;
    private Integer fullPercent;
    private Integer cooldownMinutes;
    private Integer hysteresisPercent;

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public Boolean getNotifyAllBins() {
        return notifyAllBins;
    }

    public void setNotifyAllBins(Boolean notifyAllBins) {
        this.notifyAllBins = notifyAllBins;
    }

    public List<Long> getSelectedBinIds() {
        return selectedBinIds;
    }

    public void setSelectedBinIds(List<Long> selectedBinIds) {
        this.selectedBinIds = selectedBinIds;
    }

    public Integer getThresholdPercent() {
        return thresholdPercent;
    }

    public void setThresholdPercent(Integer thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    public Integer getFullPercent() {
        return fullPercent;
    }

    public void setFullPercent(Integer fullPercent) {
        this.fullPercent = fullPercent;
    }

    public Integer getCooldownMinutes() {
        return cooldownMinutes;
    }

    public void setCooldownMinutes(Integer cooldownMinutes) {
        this.cooldownMinutes = cooldownMinutes;
    }

    public Integer getHysteresisPercent() {
        return hysteresisPercent;
    }

    public void setHysteresisPercent(Integer hysteresisPercent) {
        this.hysteresisPercent = hysteresisPercent;
    }
}
