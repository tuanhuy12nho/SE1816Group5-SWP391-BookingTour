<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.AdminUser" %>
<html>
    <head>
        <title>Quản lý Khách Hàng</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

        <style>

            body {
                background: linear-gradient(to right, #4facfe, #00f2fe);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                flex-direction: column;
            }
            /* Hiệu ứng hover cho hàng */
            .table tbody tr {
                transition: all 0.3s ease-in-out;
            }

            .table tbody tr:hover {
                background-color: rgba(0, 0, 0, 0.1) !important; /* Màu hover */
                transform: scale(1.02); /* Phóng to nhẹ */
                cursor: pointer; /* Hiển thị con trỏ tay */
            }

            /* Căn giữa tiêu đề */
            h2 {
                text-align: center;
                font-weight: bold;
                color: white;
                margin-bottom: 20px;
            }

            /* Container chính */
            .container {
                background: white;
                padding: 20px;
                border-radius: 15px;
                box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
                max-width: 90%;
                overflow-x: auto;
            }

            /* Căn chỉnh lại bảng */
            .table {
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            /* Header bảng */
            .table thead {
                background: linear-gradient(135deg, #1e3c72, #2a5298);
                color: white;
                font-size: 16px;
            }

            /* Canh giữa cột hành động */
            .table td, .table th {
                text-align: center;
                vertical-align: middle;
            }

            /* Nút Sửa */
            .btn-warning {
                background: pink;
                border: none;
                transition: all 0.3s ease;
            }

            .btn-warning:hover {
                background: #FF0099;
                transform: scale(1.05);
            }

            /* Nút Xóa */
            .btn-danger {
                background:#003366;
                border: none;
                transition: all 0.3s ease;
            }

            .btn-danger:hover {
                background:#000055;
                transform: scale(1.05);
            }

            /* Nút Thêm mới */
            .btn-success {
                background: #6699CC

                    ;
                border: none;
                transition: all 0.3s ease;
            }

            .btn-success:hover {
                background: #9999FF;
                transform: scale(1.05);
            }

            /* Hiệu ứng hover cho hàng */
            .table tbody tr:hover {
                background-color: #f1f1f1;
                transition: all 0.2s ease-in-out;
            }
            .page-title {
                text-align: center;
                font-weight: bold;
                font-size: 28px;
                color: black; /* Đảm bảo màu chữ nổi bật trên nền */
                background: rgba(0, 0, 0, 0.2); /* Làm mờ nền để dễ đọc */
                padding: 10px;
                border-radius: 10px;
                width: 100%;
                max-width: 600px;
                margin: 20px auto; /* Căn giữa tiêu đề */
            }
            .btn-back {
                position: fixed;
                bottom: 20px;
                left: 20px;
                background: linear-gradient(135deg, #ff7e5f, #ff3f34);
                border: none;
                padding: 12px 20px;
                border-radius: 8px;
                font-weight: bold;
                color: white;
                transition: all 0.3s ease-in-out;
                box-shadow: 0 4px 10px rgba(255, 63, 52, 0.3);
                text-transform: uppercase;
                z-index: 1000;
            }
            /* Đảm bảo nút có thể chứa pseudo-element */
            .btn-cute {
                position: relative;
                overflow: visible; /* Đảm bảo trái tim không bị ẩn */
                display: inline-block; /* Giúp pseudo-element hiển thị đúng */
                transition: all 0.3s ease;
            }

            /* Khi hover, tạo trái tim */
            .btn-cute::after {
                content: "❤️"; /* Biểu tượng trái tim */
                font-size: 18px;
                position: absolute;
                top: -10px;
                right: 50%;
                transform: translateX(50%);
                opacity: 0;
                transition: all 0.3s ease;
                z-index: 10; /* Đảm bảo trái tim hiển thị trên nút */
            }

            /* Khi hover vào nút, trái tim xuất hiện */
            .btn-cute:hover::after {
                top: -25px;
                opacity: 1;
            }

            /* Thêm hiệu ứng rung nhẹ */
            .btn-cute:hover {
                transform: scale(1.1);
            }



        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h2 class="page-title">📋 Quản lý Khách Hàng</h2>


            <!-- Add customer button -->
            <a href="<%= request.getContextPath()%>/AdminUserServlet?action=addnew" class="btn btn-success mb-3">➕ Thêm khách hàng mới</a>

            <!-- Check the data -->
            <%
                List<AdminUser> users = (List<AdminUser>) request.getAttribute("users");
                if (users == null) {
                    out.println("<p style='color:red;'>❌ Không nhận được danh sách user từ Servlet!</p>");
                } else {
                    out.println("<p style='color:green;'>✅ Nhận được danh sách users: " + users.size() + "</p>");
                }
            %>

            <!-- Customer list -->
            <table class="table table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Họ Tên</th>
                        <th>Email</th>
                        <th>Số Điện Thoại</th>
                        <th>Địa Chỉ</th>
                        <th>Giới Tính</th>
                        <th>Quốc Gia</th>
                        <th>Ngày Sinh</th>
                        <th>Nơi Làm Việc</th>
                        <th>Chức Danh</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (users != null && !users.isEmpty()) {
                            for (AdminUser user : users) {
                    %>
                    <tr>
                        <td><%= user.getId()%></td>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getFullName()%></td>
                        <td><%= user.getEmail()%></td>
                        <td><%= user.getPhoneNumber()%></td>
                        <td><%= user.getAddress()%></td>
                        <td><%= user.getGender()%></td>
                        <td><%= user.getCountry()%></td>
                        <td><%= user.getDateOfBirth()%></td>
                        <td><%= user.getWorkplace()%></td>
                        <td><%= user.getJobTitle()%></td>
                        <td>
                            <a href="<%= request.getContextPath()%>/AdminUserServlet?action=edit&id=<%= user.getId()%>" class="btn btn-warning btn-sm btn-cute">
                                <i class="bi bi-pencil-square"></i> 
                            </a>
                            <a href="<%= request.getContextPath()%>/AdminUserServlet?action=confirmDelete&id=<%= user.getId()%>" class="btn btn-danger btn-sm btn-cute">
                                <i class="bi bi-trash"></i> 
                            </a>
                        </td>

                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="12" class="text-center text-danger">Không có khách hàng nào</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
        <a href="Admin/admin.jsp" class=" btn-sm btn-cute btn btn-back ">
            <i class="fas fa-arrow-left"></i> 
    </body>
</html>
