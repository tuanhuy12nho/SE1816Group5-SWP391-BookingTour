<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>


<style>
    /* Profile Section */
    .profile-section {
        background: url('https://dulichviet.com.vn/images/bandidau/lac-vao-khung-canh-tho-mong-cua-thi-tran-hallstatt-khi-du-lich-ao.jpg') no-repeat center center fixed;
        background-size: cover;
        padding: 50px 0;
        min-height: calc(100vh - 200px);
        position: relative;
    }
    .profile-section::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.4); /* Lớp phủ tối để nội dung dễ đọc */
        z-index: 1;
    }
    .profile-container {
        position: relative;
        z-index: 2;
        max-width: 1200px;
        margin: 0 auto;
        display: flex;
        gap: 30px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        overflow: hidden;
    }

    /* Sidebar */
    .sidebar {
        width: 250px;
        background: #f5f5f5;
        padding: 20px;
        border-right: 1px solid #ddd;
    }
    .sidebar ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .sidebar ul li {
        margin-bottom: 10px;
    }
    .sidebar ul li a {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 10px 15px;
        color: #333;
        text-decoration: none;
        font-size: 16px;
        border-radius: 5px;
        transition: background-color 0.3s ease, color 0.3s ease;
    }
    .sidebar ul li a:hover {
        background: #e0e0e0;
        color: #003087;
    }
    .sidebar ul li.active a {
        background: #003087;
        color: #fff;
    }
    .sidebar ul li i {
        font-size: 18px;
    }

    /* Main Content */
    .main-content {
        flex: 1;
        padding: 30px;
    }
    .profile-header {
        font-size: 28px;
        font-weight: bold;
        color: #333;
        margin-bottom: 30px;
        text-align: center;
        position: relative;
    }
    .profile-header::after {
        content: '';
        width: 50px;
        height: 3px;
        background: #003087;
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
    }

    /* Info Section */
    .info-section {
        display: flex;
        gap: 30px;
        margin-bottom: 20px;
    }
    .info-section > div {
        flex: 1;
        background: #f9f9f9;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .info-section > div:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }
    .info-section h3 {
        font-size: 20px;
        font-weight: bold;
        color: #003087;
        margin-bottom: 15px;
        border-bottom: 1px solid #ddd;
        padding-bottom: 5px;
    }
    .info-section p {
        font-size: 15px;
        color: #555;
        margin: 10px 0;
    }
    .info-section p strong {
        color: #333;
        font-weight: 600;
    }

    /* Buttons */
    .btn-custom {
        display: inline-block;
        padding: 10px 25px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: bold;
        font-size: 15px;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }
    .btn-primary {
        background-color: #28a745; /* Màu xanh lá từ hình */
        color: #fff;
    }
    .btn-primary:hover {
        background-color: #218838;
        transform: translateY(-2px);
    }
    .btn-warning {
        background-color: #ffc107;
        color: #333;
    }
    .btn-warning:hover {
        background-color: #e0a800;
        transform: translateY(-2px);
    }
    .text-center {
        text-align: center;
    }
    .mt-4 {
        margin-top: 1.5rem;
    }

    /* Login Message */
    .login-message {
        text-align: center;
        padding: 20px;
        background: #ffebee;
        border-radius: 5px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    }
    .login-message p {
        font-size: 16px;
        color: #e74c3c;
        margin-bottom: 10px;
    }

    /* Responsive */
    @media (max-width: 768px) {
        .profile-container {
            flex-direction: column;
        }
        .sidebar {
            width: 100%;
            border-right: none;
            border-bottom: 1px solid #ddd;
        }
        .info-section {
            flex-direction: column;
        }
        .main-content {
            padding: 20px;
        }
        .profile-header {
            font-size: 24px;
        }
    }
</style>

<body class="changeHeader">
    <div id="wrap">
        <!-- HEADER -->
        <%@include file="/WEB-INF/inclu/head.jsp" %>



        <!-- Profile Section -->
        <section class="profile-section">
            <div class="profile-container">
                <!-- Sidebar -->
                <div class="sidebar">
                    <ul>
                        <li class="active"><a href="#"><i class="fa fa-user"></i> Hồ sơ</a></li>
                        <li><a href="<%= request.getContextPath()%>/ViewBookings?action=viewBookings"><i class="fa fa-shopping-cart"></i> Đặt chỗ của tôi</a></li>
                        <li><a href="<%= request.getContextPath()%>/UserServlet?action=showEditProfile"><i class="fa fa-cog"></i> Thay đổi thông tin</a></li>
                        <li><a href="<%= request.getContextPath()%>/UserServlet?action=showChangePassword"><i class="fa fa-lock"></i> Đổi mật khẩu</a></li>
                    </ul>
                </div>

                <!-- Main Content -->
                <div class="main-content">
                    <h2 class="profile-header">Hồ Sơ Của Tôi</h2>
                    <%
                        User user = (User) session.getAttribute("user");
                        if (user != null) {
                    %>
                    <div class="info-section">
                        <div>
                            <h3>Thông tin cá nhân</h3>
                            <p><strong>Họ Tên:</strong> <%= user.getFullName() != null ? user.getFullName() : "Chưa cập nhật"%></p>
                            <p><strong>Email:</strong> <%= user.getEmail() != null ? user.getEmail() : "Chưa cập nhật"%></p>
                            <p><strong>Số ĐT:</strong> <%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật"%></p>
                            <p><strong>Giới Tính:</strong> <%= user.getGender() != null ? user.getGender() : "Chưa cập nhật"%></p>
                            <p><strong>Ngày Sinh:</strong> <%= user.getDate_of_birth() != null ? user.getDate_of_birth() : "Chưa cập nhật"%></p>
                        </div>
                        <div>
                            <h3>Liên hệ</h3>
                            <p><strong>Địa Chỉ:</strong> <%= user.getAddress() != null ? user.getAddress() : "Chưa cập nhật"%></p>
                            <p><strong>Quốc Gia:</strong> <%= user.getCountry() != null ? user.getCountry() : "Chưa cập nhật"%></p>
                            <p><strong>Địa Chỉ Công Quan:</strong> <%= user.getWorkplace() != null ? user.getWorkplace() : "Chưa cập nhật"%></p>
                            <p><strong>Chức Danh:</strong> <%= user.getJob_title() != null ? user.getJob_title() : "Chưa cập nhật"%></p>
                        </div>
                    </div>
                    <div class="text-center mt-4">
                        <a href="<%= request.getContextPath()%>/UserServlet?action=showEditProfile" class="btn btn-primary btn-custom">Chỉnh Sửa Hồ Sơ</a>
                    </div>
                    <% } else {%>
                    <div class="login-message">
                        <p class="mb-3">Bạn cần đăng nhập để xem thông tin.</p>
                        <a href="<%= request.getContextPath()%>/login" class="btn btn-warning btn-custom">Đăng nhập</a>
                    </div>
                    <% }%>
                </div>
            </div>
        </section>

        <!-- FOOTER -->
        <%@include file="/WEB-INF/inclu/footer.jsp" %>
    </div>
</body>
