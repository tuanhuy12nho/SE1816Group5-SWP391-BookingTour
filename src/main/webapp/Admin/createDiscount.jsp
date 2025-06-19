<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Chi tiết mã giảm giá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            h2 {
                color: #0066cc;
                margin-bottom: 20px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 4px 8px rgba(0,0,0,0.05);
                margin-bottom: 30px;
            }

            th, td {
                padding: 15px;
                text-align: center;
                border-bottom: 1px solid #eaeaea;
            }

            th {
                background-color: #e6f2ff;
                font-weight: 600;
            }

            tr:hover {
                background-color: #f1f9ff;
            }

            img {
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .pagination {
                text-align: center;
                margin-top: 20px;
            }

            .pagination a,
            .pagination span {
                display: inline-block;
                padding: 8px 14px;
                margin: 0 5px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
                color: #007bff;
                background-color: #e9f2ff;
                transition: background-color 0.3s, color 0.3s;
            }

            .pagination a:hover {
                background-color: #007bff;
                color: white;
            }

            .pagination .current {
                background-color: #ff6666;
                color: white;
                font-weight: bold;
            }
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Poppins', sans-serif;
                min-height: 100vh;
                background: linear-gradient(270deg, #a1c4fd, #c2e9fb, #fbc2eb, #a1c4fd);
                background-size: 800% 800%;
                animation: hologram 15s ease infinite;
            }

            @keyframes hologram {
                0% {
                    background-position: 0% 50%;
                }
                50% {
                    background-position: 100% 50%;
                }
                100% {
                    background-position: 0% 50%;
                }
            }

            /* Sidebar */
            .sidebar {
                width: 250px;
                background: linear-gradient(180deg, #1e2a44 0%, #2c3e50 100%); /* Gradient nền */
                color: white;
                padding: 30px 20px;
                display: flex;
                flex-direction: column;
                gap: 20px;
                position: fixed;
                height: 100vh;
                overflow-y: auto;
                box-shadow: 5px 0 15px rgba(0, 0, 0, 0.2); /* Thêm bóng đổ */
                transition: all 0.3s ease;
            }

            .sidebar h3 {
                text-align: center;
                font-size: 26px;
                font-weight: 600;
                color: #ffffff;
                margin-bottom: 40px;
                letter-spacing: 1px;
                text-transform: uppercase;
                position: relative;
            }

            .sidebar h3::after {
                content: '';
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                width: 50px;
                height: 3px;
                background: #00ddeb; /* Dòng gạch chân màu cyan */
                border-radius: 2px;
            }

            .sidebar .menu-item a {
                display: flex;
                align-items: center;
                gap: 15px;
                padding: 12px 15px;
                color: #d1d5db;
                text-decoration: none;
                font-size: 16px;
                font-weight: 500;
                border-radius: 8px;
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
            }

            .sidebar .menu-item a:hover {
                background: #00ddeb; /* Màu cyan khi hover */
                color: #1e2a44; /* Chữ đổi màu khi hover */
                transform: translateX(5px); /* Dịch chuyển nhẹ khi hover */
            }

            .sidebar .menu-item a.active {
                background: #00ddeb; /* Màu cyan cho mục active */
                color: #1e2a44;
                font-weight: 600;
            }

            .sidebar a i {
                font-size: 20px;
                transition: transform 0.3s ease;
            }

            .sidebar .menu-item a:hover i {
                transform: scale(1.2); /* Phóng to biểu tượng khi hover */
            }

            .sidebar a:last-child {
                margin-top: auto;
                display: flex;
                align-items: center;
                gap: 15px;
                padding: 12px 15px;
                color: #d1d5db;
                text-decoration: none;
                font-size: 16px;
                font-weight: 500;
                border-radius: 8px;
                transition: all 0.3s ease;
            }

            .sidebar a:last-child:hover {
                background: #ff4d4d; /* Màu đỏ khi hover vào nút "Về Trang Chủ" */
                color: #ffffff;
                transform: translateX(5px);
            }

            /* Content area */
            .content {
                padding: 0 20px;
                margin-left: 250px; /* Để lại khoảng trống cho sidebar */
                align-items: center;
                height: 100vh; /* Chiếm toàn bộ chiều cao màn hình */
                overflow: scroll; /* Ẩn phần thừa nếu video vượt quá kích thước */
                overflow-x: hidden;
            }

            /* Video container */
            .video-container {
                width: 100%;
                height: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .video-container video {
                width: 100%;
                height: 100%;
                object-fit: cover; /* Đảm bảo video lấp đầy khu vực mà không bị méo */
            }
        </style>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <div class="sidebar">
            <h3>Admin Panel</h3>

            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminUserServlet"><i class="<%= request.getRequestURI().contains("AdminUserServlet") ? "active" : ""%>"></i> Khách hàng</a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminBookingServlet" class="<%= request.getRequestURI().contains("AdminBookingServlet") ? "active" : ""%>">
                    <i class="fas fa-chart-line"></i> Thống kê
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminStatisticsServlet" class="<%= request.getRequestURI().contains("AdminStatisticsServlet") ? "active" : ""%>">
                    <i class="fas fa-dollar-sign"></i> Doanh thu
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/adminTour?filter=&sort=priceAsc" class="<%= request.getRequestURI().contains("adminTour") ? "active" : ""%>">
                    <i class="fas fa-map-marked-alt"></i> Danh sách Tour
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/discountManage" class="<%= request.getRequestURI().contains("discountManage") ? "active" : ""%>">
                     <i class="fa-solid fa-ticket"></i> Danh sách Voucher
                </a>
            </div>

            <a href="<%= request.getContextPath()%>/SortTour">
                <i class="fas fa-sign-out-alt"></i> Về Trang Chủ
            </a>
        </div>
        <!-- Link Bootstrap 5 nếu chưa có -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <div class="content">
            <div class="container mt-5">
                <h2 class="mb-4 text-primary">Tạo mới mã giảm giá</h2>

                <form action="discountCreate" method="post" class="row g-3">
                    <input type="hidden" name="action" value="create">
                    <!-- Discount % -->
                    <div class="mb-3">
                        <label for="discount_percent" class="form-label">Phần trăm giảm (%):</label>
                        <input value="${dis}" type="number" name="discount_percent" class="form-control ${not empty errorDiscountPercent ? 'is-invalid' : ''}"
                               min="0.01" max="99.99" step="0.01" value="${discount_percent}" required>
                        <div class="invalid-feedback">${errorDiscountPercent}</div>
                    </div>

                    <!-- Start Date -->
                    <div class="mb-3">
                        <label class="form-label">Ngày bắt đầu:</label>
                        <input type="date" name="start_date" class="form-control ${not empty errorDate ? 'is-invalid' : ''}" value="${startDateStr}" required>
                        <div class="invalid-feedback">${errorDate}</div>
                    </div>

                    <!-- End Date -->
                    <div class="mb-3">
                        <label class="form-label">Ngày kết thúc:</label>
                        <input type="date" name="end_date" class="form-control ${not empty errorDate ? 'is-invalid' : ''}" value="${endDateStr}" required>
                        <div class="invalid-feedback">${errorDate}</div>
                    </div>

                    <!-- Max Usage -->
                    <div class="mb-3">
                        <label class="form-label">Số lượng:</label>
                        <input type="number" value="${max_use}" 
                               class="form-control ${not empty errorMax_usage ? 'is-invalid' : ''}"
                               name="max_usage"
                               min="1"
                               value="${max_usage}" required>
                        <div class="invalid-feedback">${errorMax_usage}</div>
                    </div>

                    <!-- Description -->
                    <div class="mb-3">
                        <label for="description" class="form-label">Mô tả:</label>
                        <textarea  name="description" class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" rows="4" required>${des}</textarea>
                        <div class="invalid-feedback">${errorMax_usage}</div>
                    </div>

                    <!-- Submit -->
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary">Tạo mã giảm giá</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>