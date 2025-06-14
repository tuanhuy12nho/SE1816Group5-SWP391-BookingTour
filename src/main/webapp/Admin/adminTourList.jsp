<%@page import="DAO.AdminTourDAO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List, model.AdminTour" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AdminTourDAO tourDAO = new AdminTourDAO();
    List<AdminTour> tours = (List<AdminTour>) request.getAttribute("admintours");
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
                padding: 20px;
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

        </style>
    </head>
    <body>
        <div class="container">
            <h2>Danh sách Tour</h2>

            <!-- Bộ lọc -->
            <div class="filter-container">
                <form action="adminTour" method="get">
                    <label>Lọc:</label>
                    <select name="filter" onchange="this.form.submit()">
                        <option value="" <%= "".equals(filter) ? "selected" : ""%>>
                            Tất cả (<%= tourDAO.getTotalTours(null)%>)
                        </option>
                        <option value="Trong nước" <%= "Trong nước".equals(filter) ? "selected" : ""%>>
                            Trong nước (<%= tourDAO.getTotalTours("Domestic")%>)
                        </option>
                        <option value="Ngoài nước" <%= "Ngoài nước".equals(filter) ? "selected" : ""%>>
                            Ngoài nước (<%= tourDAO.getTotalTours("International")%>)
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
                <a href="<%= request.getContextPath()%>/Admin/admin.jsp">🔙 Quay lại Trang Admin</a>
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
    </body>
</html>