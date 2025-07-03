![Logo](https://github.com/Phongdnh-uit/SE114.P22.Project/blob/deployment/backend/backend/src/main/resources/static/images/banner.png)

# Măm - Hệ thống bán đồ ăn nhanh

## Giới thiệu

Đây là một nền tảng ứng dụng di dộng cho phép người dùng thực hiện quản lý thực đơn, tùy chỉnh món ăn, và đặt món trực
tuyến một cách nhanh chóng và tiện lợi.
Người dùng có thể duyệt qua danh sách món ăn, xem chi tiết thông tin như giá cả, thành phần, hình ảnh minh họa, sau đó
thêm món vào giỏ hàng và tiến hành thanh toán ngay trên nền tảng.

Hệ thống được thiết kế hướng đến tối ưu trải nghiệm người dùng, đồng thời hỗ trợ người quản lý dễ dàng cập nhật thực
đơn, theo dõi đơn hàng, và xử lý đơn theo thời gian thực. Ngoài ra, các tính năng như phân quyền người dùng (
admin/khách), xác thực tài khoản, ngăn chặn tấn công brute force, kiểm tra hợp lệ của số điện thoại và tích hợp thanh
toán (giả lập) cũng được xây dựng nhằm mô phỏng hệ thống bán hàng thực tế.

## Công nghệ sử dụng

**Backend:**

- Ngôn ngữ: Java 23
- Framework: Spring Boot 3.4.3
- Thư viện: Spring Security (OAuth2), Spring Data JPA, Spring Web, Swagger (OpenAPI), Turkraft Spring Filter
- Cơ sở dữ liệu: MySQL, Neo4J, Redis
- Tích hợp: Twillio, VNPay, Mailjet, Firebase, Mapbox.

## Yêu cầu cơ bản

### Môi trường runtime

- JDK 23 (bắt buộc có nếu không chạy thông qua docker)

### API Key

<table>
    <tr>
        <th>Tên</th>
        <th>Thuộc tính</th>
        <th>Ghi chú</th>
    </tr>
    <tr>
        <td rowspan="4">Mail</td>
        <td>Host</td>
        <td>(Gmail) smtp.gmail.com</td>
    </tr>
    <tr>
        <td>Port</td>
        <td>(Gmail) 587</td>
    </tr>
    <tr>
        <td>Username</td>
        <td>(Gmail) abc@gmail.com</td>
    </tr>
    <tr>
        <td>Password</td>
        <td>(Gmail) App Password</td>
    </tr>
    <tr>
        <td rowspan="3">Twilio</td>
        <td>SID</td>
        <td rowspan="3">https://www.twilio.com</td>
    </tr>
    <tr>
        <td>Auth ID</td>
    </tr>
    <tr>
        <td>Verify Sid</td>
    </tr>
    <tr>
        <td>Firebase</td>
        <td>Json Config</td>
        <td>https://firebase.google.com</td>
    </tr>
    <tr>
        <td rowspan="4">Map box</td>
        <td>Access Token</td>
        <td rowspan="4">https://www.mapbox.com</td>
    </tr>
    <tr>
        <td>Base Url</td>
    </tr>
    <tr>
        <td>Location Lat</td>
    </tr>
    <tr>
        <td>Location Lng</td>
    </tr>
    <tr>
        <td rowspan="3">VNPay</td>
        <td>TMP Code</td>
        <td>https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html</td>
    </tr>
    <tr>
        <td>Hash Secret</td>
    </tr>
    <tr>
        <td>Pay URL</td>
    </tr>
</table>

### Cơ sở dữ liệu (nếu không chạy thông qua docker)

- MySQL 8
- Neo4J
- Redis

## Hướng dẫn chạy dự án

- Bước 1: Clone dự án về thư mục cá nhân

```
git clone https://github.com/Phongdnh-uit/SE114.P22.Project.git
cd SE114.P22.Project
cd backend
```

- Bước 2: Chạy hệ thống backend

  - Cách 1: Chạy thông qua docker
    - Bước 1: truy cập folder **_/docker/pro_**, dựa vào file .example.env, ta bổ sung thông tin API key và các
      thông tin mới vào file mới đặt tên là .env trong cùng thư mục.
    ```
    cd docker/pro
    cp .example.env .env
    ```
    - Bước 2: Chạy hệ thống backend thông qua docker
    ```
    docker compose -f docker-compose.yaml up -d
    ```
  - Cách 2: Chạy thông qua local trên máy

    - Khi chạy thông qua local, ta cần có đủ các yêu cầu về môi trường và cơ sở dữ liệu để chạy.

      - JDK 23
      - MYSQL 8
      - NEO4J
      - REDIS

    - Bước 1: Kiểm tra phiên bản java hiện tại và chạy các cơ sở dữ liệu cần thiết

    ```java
    java -version
    ```

    - Bước 2: Cài đặt các thư viện cần thiết

    ```java
    ./gradlew --refresh-dependencies
    ```

    - Bước 3: Build và chạy ứng dụng

    ```java
    ./gradlew clean build
    ./gradlew bootRun
    ```

## Cấu trúc thư mục

### Backend

```
backend/
├── annotations/                # Chứa các annotation tùy chỉnh
├── configs/                    # Chứa các cấu hình hệ thống và thư viện
├── constants/                  # Lưu trữ các hằng số
├── controllers/                # Chứa các lớp controller xử lý yêu cầu
├── dtos/                       # Dữ liệu yêu cầu và phản hồi
├── entities/                   # Chứa các thực thể
├── enums/                      # Các khai báo enum
├── exceptions/                 # Chứa các exception tùy chỉnh và xử lý lỗi
├── mappers/                    # Chứa các logic ánh xạ dữ liệu
├── neo4j/                      # Chứa logic nghiệp vụ liên quan đến Neo4J (hệ thống đề xuất sản phẩm)
├── repositories/               # Chứa các interface giao tiếp với cơ sở dữ liệu
├── seeders/                    # Chứa logic khởi tạo dữ liệu mẫu
├── services/                   # Chứa logic nghiệp vụ
├── util/                       # Chứa các tiện ích và hàm hỗ trợ
├── vo/                         # Chứa các Value Object
resources
├── data/                       # Lưu trữ các tập tin dữ liệu mẫu
├── static/                     # Lưu trữ các tài nguyên tĩnh
├── templates/                  # Các template html
├── application-pro.properties  # Cấu hình ứng dụng cho môi trường production
└── application.properties      # Cấu hình chung cho các môi trường
```

## Screenshots

![App Screenshots](https://github.com/Phongdnh-uit/SE114.P22.Project/blob/deployment/backend/backend/src/main/resources/static/images/demo_screenshot.png)


## Thành viên phát triển
- [**Đặng Nguyễn Huy Phong - 23521159**](https://github.com/Phongdnh-uit)
- [**Phạm Tuấn Khang - 23520707**](https://github.com/KhangPham205)