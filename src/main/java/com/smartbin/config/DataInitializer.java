package com.smartbin.config;

import com.smartbin.entity.Area;
import com.smartbin.entity.Bin;
import com.smartbin.entity.Bin.BinStatus;
import com.smartbin.entity.Device;
import com.smartbin.entity.Device.DeviceType;
import com.smartbin.entity.User;
import com.smartbin.entity.NotificationSetting;
import com.smartbin.repository.AreaRepository;
import com.smartbin.repository.BinRepository;
import com.smartbin.repository.DeviceRepository;
import com.smartbin.repository.UserRepository;
import com.smartbin.repository.NotificationSettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 DataInitializer đang chạy...");

        try {
            // Kiểm tra database connection
            long userCount = userRepository.count();
            long areaCount = areaRepository.count();
            long binCount = binRepository.count();
            long deviceCount = deviceRepository.count();

            log.info("📊 Database hiện tại: Users={}, Areas={}, Bins={}, Devices={}", userCount, areaCount, binCount,
                    deviceCount);

            // Chỉ tạo users nếu chưa có
            if (userCount == 0) {
                log.info("👥 Tạo users...");
                createUsers();
            } else {
                log.info("👥 Users đã tồn tại, bỏ qua");
            }

            // Tạo area và bin nếu chưa có
            if (areaCount == 0 || binCount == 0) {
                log.info("🗑️ Tạo area và bin...");
                createSingleAreaAndBin();
            } else {
                log.info("🗑️ Area và bin đã tồn tại, bỏ qua");
            }

            // Tạo device nếu chưa có
            if (deviceCount == 0) {
                log.info("🔧 Tạo device...");
                createDeviceForExistingBin();
            } else {
                log.info("🔧 Device đã tồn tại, bỏ qua");
            }

            log.info("✅ DataInitializer hoàn thành!");

            // Ensure default notification settings for all users
            createDefaultNotificationSettings();

        } catch (Exception e) {
            log.error("❌ Lỗi trong DataInitializer: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createUsers() {
        try {
            // Tạo admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@smartbin.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(User.UserRole.ADMIN);
            admin.setEnabled(true);

            // Tạo regular user
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@smartbin.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Regular User");
            user.setRole(User.UserRole.USER);
            user.setEnabled(true);

            userRepository.saveAll(Arrays.asList(admin, user));
            log.info("✅ Đã tạo {} users", Arrays.asList(admin, user).size());

        } catch (Exception e) {
            log.error("❌ Lỗi tạo users: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createDefaultNotificationSettings() {
        try {
            List<User> users = userRepository.findAll();
            int created = 0;
            for (User u : users) {
                boolean exists = notificationSettingRepository.findByUser(u).isPresent();
                if (!exists) {
                    NotificationSetting s = new NotificationSetting();
                    s.setUser(u);
                    s.setEmails(u.getEmail());
                    s.setNotifyAllBins(true);
                    notificationSettingRepository.save(s);
                    created++;
                }
            }
            if (created > 0) {
                log.info("✅ Đã tạo {} notification settings mặc định", created);
            }
        } catch (Exception e) {
            log.error("❌ Lỗi tạo notification settings mặc định: {}", e.getMessage(), e);
        }
    }

    private void createSingleAreaAndBin() {
        try {
            // Tạo 1 area duy nhất
            Area area = new Area();
            area.setName("Khu vực chính");
            area.setDescription("Khu vực trung tâm thành phố");
            area = areaRepository.save(area);
            log.info("✅ Đã tạo area: {}", area.getName());

            // Tạo 1 bin duy nhất
            Bin bin = new Bin();
            bin.setCode("BIN001");
            bin.setName("Thùng rác trung tâm");
            bin.setLevel(15); // Bắt đầu với 15% rác
            bin.setStatus(BinStatus.ACTIVE);
            bin.setOnline(true);

            bin.setLatitude(21.0285);
            bin.setLongitude(105.8542);
            bin.setLastEmptied(LocalDateTime.now().minusDays(2));
            bin.setNextScheduledEmpty(LocalDateTime.now().plusDays(5));
            bin.setArea(area);
            bin = binRepository.save(bin);
            log.info("✅ Đã tạo bin: {} (Level: {}%)", bin.getCode(), bin.getLevel());

            log.info("🎉 Hoàn thành tạo 1 area và 1 bin duy nhất");

        } catch (Exception e) {
            log.error("❌ Lỗi tạo area và bin: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createDeviceForExistingBin() {
        try {
            // Lấy bin đầu tiên (đã tồn tại)
            List<Bin> bins = binRepository.findAll();
            if (bins.isEmpty()) {
                log.warn("⚠️ Không có bin nào để tạo device");
                return;
            }

            Bin bin = bins.get(0);
            log.info("🔧 Tạo device cho bin: {}", bin.getCode());

            // Tạo device
            Device device = new Device();
            device.setDeviceCode("DEV001");
            device.setDeviceName("IoT Sensor " + bin.getCode());
            device.setDeviceType(DeviceType.SENSOR);
            device.setManufacturer("SmartBin Tech");
            device.setModel("SB-2024");
            device.setFirmwareVersion("1.0.0");
            device.setMacAddress("AA:BB:CC:DD:EE:01");
            device.setIpAddress("192.168.1.101");
            device.setIsActive(true);
            device.setLastPing(LocalDateTime.now());

            // Lưu device
            device = deviceRepository.save(device);
            log.info("✅ Đã tạo device: {} với ID: {}", device.getDeviceCode(), device.getId());

            // Liên kết device với bin
            bin.setDevice(device);
            binRepository.save(bin);
            log.info("✅ Đã liên kết device {} với bin {}", device.getDeviceCode(), bin.getCode());

        } catch (Exception e) {
            log.error("❌ Lỗi tạo device: {}", e.getMessage(), e);
            throw e;
        }
    }
}