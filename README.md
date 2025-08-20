# SmartBin API

SmartBin là một ứng dụng Spring Boot cung cấp REST API cho ứng dụng web React.js.

## Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL (tùy chọn, cho cơ sở dữ liệu)

## Cài đặt và chạy

### 1. Clone project
```bash
git clone <repository-url>
cd SmartBin
```

### 2. Cài đặt dependencies
```bash
mvn clean install
```

### 3. Chạy ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

### 4. Cấu hình cơ sở dữ liệu (tùy chọn)
Nếu bạn muốn sử dụng MySQL, hãy:
1. Tạo database tên `smartbin_db`
2. Bỏ comment các dòng cấu hình database trong `application.properties`
3. Cập nhật thông tin kết nối database

## API Endpoints

### Health Check
- **GET** `/api/smartbin/health` - Kiểm tra trạng thái API
- **GET** `/api/smartbin/status` - Lấy thông tin trạng thái chi tiết

### SmartBin Operations
- **GET** `/api/smartbin/bins` - Lấy danh sách tất cả bins
- **POST** `/api/smartbin/test` - Test endpoint để gửi dữ liệu

## Cấu hình CORS

API đã được cấu hình CORS để hoạt động với React.js:
- Cho phép origins: `http://localhost:3000`, `http://localhost:3001`
- Cho phép methods: GET, POST, PUT, DELETE, OPTIONS
- Cho phép tất cả headers
- Hỗ trợ credentials

## Cấu trúc project

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── smartbin/
│   │           ├── SmartBinApplication.java      # Main class
│   │           ├── config/
│   │           │   └── CorsConfig.java           # CORS configuration
│   │           └── controller/
│   │               └── SmartBinController.java   # REST controllers
│   └── resources/
│       └── application.properties                # Configuration
└── test/
    └── java/
        └── com/
            └── smartbin/
```

## Test API

Bạn có thể test API bằng cách sử dụng curl hoặc Postman:

```bash
# Health check
curl http://localhost:8080/api/smartbin/health

# Status check
curl http://localhost:8080/api/smartbin/status

# Get bins
curl http://localhost:8080/api/smartbin/bins

# Test POST
curl -X POST http://localhost:8080/api/smartbin/test \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello from React!"}'
```

## Phát triển

1. Thêm các entity classes trong package `com.smartbin.entity`
2. Tạo repository interfaces trong package `com.smartbin.repository`
3. Implement business logic trong package `com.smartbin.service`
4. Thêm controllers mới trong package `com.smartbin.controller`

## Ghi chú

- API được thiết kế để hoạt động với React.js frontend
- CORS đã được cấu hình sẵn cho development
- Database configuration có thể được bật khi cần thiết
- Logging level có thể được điều chỉnh trong `application.properties`
