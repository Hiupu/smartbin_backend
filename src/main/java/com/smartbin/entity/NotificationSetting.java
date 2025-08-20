package com.smartbin.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification_settings")
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "emails", nullable = false, length = 1000)
    private String emails; // comma-separated

    @Column(name = "notify_all_bins", nullable = false)
    private Boolean notifyAllBins = Boolean.TRUE;

    @ManyToMany
    @JoinTable(name = "notification_settings_bins", joinColumns = @JoinColumn(name = "setting_id"), inverseJoinColumns = @JoinColumn(name = "bin_id"))
    private Set<Bin> selectedBins = new HashSet<>();

    @Column(name = "threshold_percent", nullable = false)
    private Integer thresholdPercent = 70; // warning threshold

    @Column(name = "full_percent", nullable = false)
    private Integer fullPercent = 90; // full threshold

    @Column(name = "cooldown_minutes", nullable = false)
    private Integer cooldownMinutes = 60;

    @Column(name = "hysteresis_percent", nullable = false)
    private Integer hysteresisPercent = 5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Set<Bin> getSelectedBins() {
        return selectedBins;
    }

    public void setSelectedBins(Set<Bin> selectedBins) {
        this.selectedBins = selectedBins;
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
