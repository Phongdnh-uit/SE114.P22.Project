# SE114 MAM (Food App)
SE114 MAM là ứng dụng đặt món ăn, phát triển bằng Kotlin với Jetpack Compose trên nền tảng Android. Ứng dụng hỗ trợ người dùng tìm kiếm, quản lý và trải nghiệm các món ăn, đồng thời hỗ trợ admin kiểm soát hệ thống.

‼️Hệ thống tích hợp Firebase Authentication để xác thực người dùng và Mapbox để định vị vị trí. Người dùng cần cấu hình các dịch vụ này trước khi sử dụng đầy đủ tính năng.

# 🚀 Các chức năng chính
  1. 👤 Người dùng (User)
    - Đăng ký, đăng nhập và xác thực người dùng 
    - Đăng ký, đăng nhập và xác thực người dùng bằng tài khoản Google (Firebase Authentication)
    - Tìm kiếm món ăn
    - Xem chi tiết món ăn (hình ảnh, mô tả, tùy chọn)
    - Gợi ý món ăn 
    - Đặt hàng và thanh toán (VNPay)
    - Lấy địa chỉ bằng bản đồ (MapBox)
    - Liệt kê lịch sử thanh toán

  2. 🧑‍🍳 Cửa hàng (Admin)
    - Quản lý danh mục (thêm, xóa, sửa)
    - Quản lý sản phẩm (thêm, xóa, sửa) và tùy chọn (thêm, xóa)
    - Quản lý người dùng (cập nhật trạng thái)
    - Quản lý người giao hàng (thêm, xóa, sửa)
    - Quản lý thông báo (thêm)
    - Quản lý mã giảm giá (thêm, xóa)
    - Quản lý đơn hàng (xử lý đơn hàng)
    - Đăng nhập và xác thực admin
    - Thống kê doanh thu theo tháng/quý và tỷ lệ danh mục bán ra theo tháng (YChart)

# ⚙️ Công nghệ sử dụng
  - Ngôn ngữ: Kotlin
  - UI: Jetpack Compose
  - Xác thực: Firebase Authentication
  - Bản đồ: Mapbox SDK
  - Thanh toán: VNPay
  - Phân trang: Paging 3
  - Biểu đồ: MPAndroidChart / YChart

# 🛠️Cài đặt và chạy ứng dụng
  1. Clone dự án
  2. Mở bằng Android Studio
    - [Hướng dẫn cách cài đặt Android Studio](https://developer.android.com/studio)
  3. Thiết lập Firebase Authentication
    - [Hướng dẫn set up Firebase vào dự án Android](https://firebase.google.com/docs/android/setup)  
    - Tải google-services.json vào app/
  4. Cấu hình Mapbox
    - [Hướng dẫn lấy APIKey](https://docs.mapbox.com/android/maps/guides/) 
    - Thêm thông tin vào res/values/mapbox_access_token.xml: 
      <?xml version="1.0" encoding="utf-8"?>
      <resources xmlns:tools="http://schemas.android.com/tools">
          <string name="mapbox_access_token" translatable="false" tools:ignore="UnusedResources"> MAP_BOX_KEY </string>
      </resources>

# 📁Cấu trúc thư mục
  - Kiến trúc hệ thống: MVVM
  ```
  MAM/
  ├── app/src/main
  │   ├── java/com/example/mam
  │   │   ├── component/              # Các thành phần UI tái sử dụng (Buttons, Card, Dialog,...)
  │   │   ├── data/                   # Lớp truy cập dữ liệu: local Constraint, local DataStore
  │   │   ├── dtos/                   # Data Transfer Objects: class mô tả dữ liệu request/response
  │   │   ├── navigation/             # Điều hướng giữa các màn hình (Compose Navigation)
  │   │   ├── repository/             # Xử lý gọi API 
  │   │   ├── screen/                 # Các màn hình (UI chính), phân theo client/management/authentication
  │   │   ├── ui/                     # Chủ đề, màu sắc, font chữ, style cho toàn ứng dụng
  │   │   ├── viewmodel/              # ViewModel theo kiến trúc MVVM
  │   │   ├── GoogleSignInUtils.kt    # Xử lý đăng nhập Google (Firebase Auth)
  │   │   ├── MainActivity.kt         # Activity chính của ứng dụng, khởi tạo Navigation Host
  │   │   └── MAMApplication.kt       # Application class, dùng để khởi tạo Hilt, Mapbox, DataStore v.v.
  │   └── res/                        # Tài nguyên giao diện: ảnh, layout XML (nếu có), strings,...
  ├── build.gradle.kts                # Tập tin cấu hình Gradle (Kotlin DSL) cho project
  ├── FE_README.md                    # File README dành riêng cho Frontend (ứng dụng Android)

  ```

# 📬Liên hệ 
  1. Tên: Đinh Thanh Tùng
  Email: dinhthanhtung0312@gmail.com
  Github: [tungdt312](https://github.com/tungdt312)

  2. Tên: Trần Trân Châu
  Email: trantranchau1605@gmail.com
  Github: [CaHoiAu](https://github.com/CaHoiAu)