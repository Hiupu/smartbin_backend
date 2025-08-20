package com.smartbin.repository;

import com.smartbin.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByDeviceCode(String deviceCode);

    Optional<Device> findByMacAddress(String macAddress);

    List<Device> findByDeviceType(Device.DeviceType deviceType);

    List<Device> findByIsActive(Boolean isActive);

    List<Device> findByManufacturer(String manufacturer);

    @Query("SELECT d FROM Device d WHERE d.lastPing < ?1")
    List<Device> findInactiveDevices(LocalDateTime cutoffTime);

    @Query("SELECT d FROM Device d WHERE LOWER(d.deviceCode) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(d.deviceName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Device> searchDevices(String query);

    boolean existsByDeviceCode(String deviceCode);

    boolean existsByMacAddress(String macAddress);

    long countByDeviceType(Device.DeviceType deviceType);

    long countByIsActive(Boolean isActive);
}
