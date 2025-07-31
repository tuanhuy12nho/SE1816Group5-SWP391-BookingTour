<%@page import="DAO.AdminTourDAO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List, model.AdminTour" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%
    List<AdminTour> tours = (List<AdminTour>) request.getAttribute("list");
    int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
    int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
    String filter = request.getParameter("filter") == null ? "" : request.getParameter("filter");

    if (tours == null) {
        tours = new ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Tour</title>
        <style>
            /* General styling */
            body {
                font-family: Arial, sans-serif;
                margin: 0;
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

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
                background-color: rgba(255, 255, 255, 0.8);
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            h2 {
                text-align: center;
                color: #333;
                margin-bottom: 20px;
            }

            /* Filter container */
            .filter-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }

            .filter-container form {
                display: flex;
                gap: 20px;
            }

            .filter-container label {
                font-weight: bold;
                color: #333;
            }

            .filter-container select {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
                color: #333;
            }

            /* Add button */
            .btn.add {
                display: inline-block;
                padding: 10px 20px;
                background-color: #28A745;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-weight: bold;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                transition: background-color 0.3s;
            }

            .btn.add:hover {
                background-color: #218838;
            }

            /* Tour list */
            .tour-list {
                display: flex;
                flex-direction: column;
                gap: 20px;
            }

            .tour-item {
                display: flex;
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                transition: transform 0.3s;
            }

            .tour-item:hover {
                transform: translateY(-5px);
            }

            .tour-item img {
                width: 200px;
                height: 150px;
                object-fit: cover;
                border-top-left-radius: 10px;
                border-bottom-left-radius: 10px;
            }

            .tour-details {
                padding: 15px;
                flex: 1;
            }

            .tour-details h3 {
                margin: 0 0 10px;
                font-size: 18px;
                color: #333;
            }

            .tour-details p {
                margin: 5px 0;
                font-size: 14px;
                color: #666;
            }

            .tour-details .price {
                font-size: 16px;
                font-weight: bold;
                color: #28A745;
                margin-top: 10px;
            }

            .tour-details .btn {
                padding: 8px 15px;
                text-decoration: none;
                border-radius: 5px;
                font-size: 14px;
                margin-right: 10px;
                transition: background-color 0.3s;
            }

            .tour-details .btn.edit {
                background-color: #28A745;
                color: white;
            }

            .tour-details .btn.edit:hover {
                background-color: #218838;
            }

            .tour-details .btn.delete {
                background-color: #DC3545;
                color: white;
            }

            .tour-details .btn.delete:hover {
                background-color: #C82333;
            }

            /* Back to admin link */
            div[style*="text-align:center"] a {
                color: #007BFF;
                text-decoration: none;
                font-size: 16px;
            }

            div[style*="text-align:center"] a:hover {
                text-decoration: underline;
            }

            /* Custom Pagination */
            .custom-pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 8px;
                margin-top: 20px;
            }

            .page-btn {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 36px;
                height: 36px;
                border-radius: 8px;
                background-color: #e0e7ff;
                color: #333;
                text-decoration: none;
                font-size: 16px;
                font-weight: bold;
                transition: all 0.3s ease;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .page-btn:hover {
                background-color: #c2e9fb;
                transform: translateY(-2px);
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.15);
            }

            .page-btn.active {
                background-color: #fbc2eb;
                color: #333;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }

            .page-btn-arrow {
                background-color: #a1c4fd;
                color: #333;
            }

            .page-btn-arrow:hover {
                background-color: #90b5fc;
            }

            .page-btn-arrow span {
                font-size: 18px;
            }
            .tour-details .btn.detail {
                background-color: #007BFF; /* Màu xanh dương */
                color: white;
                padding: 8px 15px;
                border-radius: 5px;
                font-size: 14px;
                text-decoration: none;
                margin-right: 10px;
                transition: background-color 0.3s;
                display: inline-block;
            }

            .tour-details .btn.detail:hover {
                background-color: #0056b3; /* Màu xanh đậm khi hover */
                text-decoration: none;
                color: white;
            }
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Poppins', sans-serif;
                display: flex;
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
                width: 100%;
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
    </head>
    <body>
        <div class="sidebar">
            <h3>Admin Panel</h3>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/admin-dashboard"><i class="<%= request.getRequestURI().contains("AdminDashboardServlet") ? "active" : ""%>"></i> Dashboard</a>
            </div>
            <c:if test="${sessionScope.user != null && sessionScope.user.userRole == 'Admin'}">
                <div class="menu-item">
                    <a href="<%= request.getContextPath()%>/AdminUserServlet"><i class="<%= request.getRequestURI().contains("AdminUserServlet") ? "active" : ""%>"></i> Khách hàng</a>
                </div>
            </c:if>
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
                <a href="<%= request.getContextPath()%>/adminlist" class="<%= request.getRequestURI().contains("adminTour") ? "active" : ""%>">
                    <i class="fas fa-map-marked-alt"></i> Danh sách Tour
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/discountManage" class="<%= request.getRequestURI().contains("discountManage") ? "active" : ""%>">
                    <i class="fa-solid fa-ticket"></i> Danh sách Voucher
                </a>
            </div>
            <!<!-- yêu cầu tour  -->
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/admin-request-list" class="<%= request.getRequestURI().contains("/admin-request-list") ? "active" : ""%>">
                    <i class="fa-solid fa-clipboard-list"></i> Yêu cầu tour
                </a>
            </div>


            <a href="<%= request.getContextPath()%>/SortTour">
                <i class="fas fa-sign-out-alt"></i> Về Trang Chủ
            </a>
        </div>
        <div class="content">
            <div class="container">
                <h2>Danh sách Tour</h2>

                <!-- Bộ lọc -->
                <div class="filter-container">
                    <form action="adminlist" method="get">
                        <label>Lọc:</label>
                        <select name="filter" onchange="this.form.submit()">
                            <option value="" <%= "".equals(filter) ? "selected" : ""%>>
                                Tất cả (<%= request.getAttribute("totalTours")%>)
                            </option>
                            <option value="Trong nước" <%= "Trong nước".equals(filter) ? "selected" : ""%>>
                                Trong nước (<%= request.getAttribute("domesticTours")%>)
                            </option>
                            <option value="Ngoài nước" <%= "Ngoài nước".equals(filter) ? "selected" : ""%>>
                                Ngoài nước (<%= request.getAttribute("internationalTours")%>)
                            </option>
                        </select>

                        <label>Sắp xếp:</label>
                        <select name="sort" onchange="this.form.submit()">
                            <option value="priceAsc" <%= "priceAsc".equals(request.getParameter("sort")) ? "selected" : ""%>>Giá tăng dần</option>
                            <option value="priceDesc" <%= "priceDesc".equals(request.getParameter("sort")) ? "selected" : ""%>>Giá giảm dần</option>
                        </select>
                    </form>
                </div>

                <!-- Nút thêm Tour -->
                <a href="<%=request.getContextPath()%>/adminaddTour" class="btn add">➕ Thêm tour mới</a>

                <!-- Danh sách tour -->
                <div class="tour-list">
                    <% if (tours != null && !tours.isEmpty()) { %>
                    <% for (AdminTour tour : tours) {%>
                    <div class="tour-item">
                        <img src="<%= tour.getImageUrl()%>" alt="Tour Image">
                        <div class="tour-details">
                            <h3><%= tour.getName()%></h3>
                            <p>Địa điểm: <%= tour.getLocation()%></p>
                            <p>Ngày đi: <%= tour.getStartDate()%> - Ngày về: <%= tour.getEndDate()%></p>
                            <p>Phương tiện: <%= tour.getTransport()%></p>
                            <p class="price">GIÁ: <%= String.format("%,.0f", tour.getPrice())%> VNĐ</p>
                            <a href="<%= request.getContextPath()%>/adminItinerary?tourId=<%= tour.getId()%>" class="btn detail">Xem chi tiết hành trình</a>
                            <a href="<%= request.getContextPath()%>/manageReview?tourId=<%= tour.getId()%>" class="btn edit" style="background-color: orange">Xem Review</a>
                            <a href="<%= request.getContextPath()%>/admineditTour?id=<%= tour.getId()%>" class="btn edit">Chỉnh sửa</a>
                            <a href="<%= request.getContextPath()%>/admindeleteTour?id=<%= tour.getId()%>" class="btn delete" 
                               onclick="return confirm('Xác nhận xóa tour?')">Xóa</a>
                        </div>
                    </div>
                    <% } %>
                    <% } else { %>
                    <p>Không có tour nào để hiển thị.</p>
                    <% }%>
                </div>

                <!-- Liên kết quay lại -->
                <div style="text-align:center; margin-top:15px;">
                    <a href="admin-dashboard">🔙 Quay lại Trang Admin</a>
                </div>

                <!-- Phân trang -->
                <div class="custom-pagination">
                    <!-- Nút Previous -->
                    <% if (currentPage > 1) {%>
                    <a href="?page=<%= currentPage - 1%>&filter=<%= filter%>" class="page-btn page-btn-arrow">
                        <span>←</span>
                    </a>
                    <% } %>

                    <!-- Hiển thị các số trang -->
                    <% for (int i = 1; i <= totalPages; i++) {%>
                    <a href="?page=<%= i%>&filter=<%= filter%>" 
                       class="page-btn <%= (i == currentPage) ? "active" : ""%>">
                        <%= i%>
                    </a>
                    <% } %>

                    <!-- Nút Next -->
                    <% if (currentPage < totalPages) {%>
                    <a href="?page=<%= currentPage + 1%>&filter=<%= filter%>" class="page-btn page-btn-arrow">
                        <span>→</span>
                    </a>
                    <% }%>
                </div>
            </div>
        </div>
    </body>
</html>