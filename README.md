# Cinema Booking System

##  Giới thiệu

Hệ thống đặt vé xem phim gồm:

* Backend: Spring Boot (Java)
* Frontend: React
* Database: MySQL

---

##  Cấu trúc project

```
Cinema/
├── backend/
├── frontend/
├── docker-compose.yml
├── .env
└── README.md
```

---

##  Chạy project

### 1. Backend

```
cd backend
./mvnw spring-boot:run
```

Chạy tại: http://localhost:8080

---

### 2. Frontend

```
cd frontend
npm install
npm start
```

Chạy tại: http://localhost:3000

---

##  API

Frontend gọi API:

```
http://localhost:8080/api/...
```

---

##  Công nghệ sử dụng

* Spring Boot
* Spring Data JPA
* React
* MySQL
* Docker (optional)

---

##  Tính năng (dự kiến)

* Đăng ký / đăng nhập
* Danh sách phim
* Đặt vé
* Quản lý suất chiếu
* Thanh toán

---

##  Author

* TienToan2005
