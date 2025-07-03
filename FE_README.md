# SE114 MAM (Food App)
Ứng dụng SE114 MAM là một app về ẩm thực phát triển với ngôn ngữ Kotlin(Jetpack Compose) trên nền tảng Android, hỗ trợ người dùng tìm kiếm, quản lý và trải nghiệm các món ăn. App sử dụng các công nghệ như Android Studio, Firebase Authentication để xác thực người dùng, và Mapbox để tích hợp bản đồ. Người dùng cần thiết lập các dịch vụ này trước khi sử dụng. Ứng dụng hướng tới việc cung cấp trải nghiệm tiện lợi, hiện đại cho người yêu ẩm thực.

# Các chức năng chính
1. Người dùng (User)
- Đăng ký, đăng nhập và xác thực người dùng 
- Đăng ký, đăng nhập và xác thực người dùng bằng tài khoản Google (Firebase Authentication)
- Tìm kiếm món ăn
- Xem chi tiết món ăn (hình ảnh, mô tả, tùy chọn)
- Gợi ý món ăn 
- Đặt hàng và thanh toán (VNPay)
- Lấy địa chỉ bằng bản đồ (MapBox)
- Liệt kê lịch sử thanh toán

2. Cửa hàng (Admin)
- Quản lý danh mục (thêm, xóa, sửa)
- Quản lý sản phẩm (thêm, xóa, sửa) và tùy chọn (thêm, xóa)
- Quản lý người dùng (cập nhật trạng thái)
- Quản lý người giao hàng (thêm, xóa, sửa)
- Quản lý thông báo (thêm)
- Quản lý mã giảm giá (thêm, xóa)
- Quản lý đơn hàng (xử lý đơn hàng)
- Đăng nhập và xác thực admin
- Thống kê doanh thu theo tháng/quý và tỷ lệ danh mục bán ra theo tháng (YChart)

# Công nghệ sử dụng
- Kotlin (Jetpack Compose) cho phát triển UI hiện đại trên Android
- AndroidX: core-ktx, lifecycle, activity, navigation, appcompat, datastore, paging3, hilt, material3, material icons, credentials
- Google Play Services: Maps, Location, Analytics, Credentials
- Firebase: Authentication, Analytics, Common (qua Firebase BoM)
- Mapbox: bản đồ và extension cho Compose
- VNPay: thanh toán trực tuyến
- Retrofit & OkHttp: gọi API, xử lý HTTP, chuyển đổi dữ liệu (Gson)
- Coil: tải và hiển thị ảnh
- Accompanist: navigation animation, permissions
- YCharts: vẽ biểu đồ thống kê
- kotlinx.coroutines: xử lý bất đồng bộ

# Cài đặt và chạy ứng dụng
1. Clone dự án
2. Mở bằng Android Studio
- [Hướng dẫn cách cài đặt Android Studio](https://developer.android.com/studio)
3. Thiết lập Firebase Authentication
- [Hướng dẫn set up Firebase vào dự án Android](https://firebase.google.com/docs/android/setup)  
- Tải google-services.json vào app/
4. Cấu hình Mapbox
- [Hướng dẫn lấy APIKey](https://docs.mapbox.com/android/maps/guides/) 

# Cấu trúc thư mục


# Liên hệ 
1. Tên: Đinh Thanh Tùng
Email: dinhthanhtung0312@gmail.com
Github: [tungdt312](https://github.com/tungdt312)

2. Tên: Trần Trân Châu
Email: trantranchau1605@gmail.com
Github: [CaHoiAu](https://github.com/CaHoiAu)