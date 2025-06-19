<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BookingTour - Đăng ký</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <!-- Custom CSS -->
        <style>
            /* Reset some default styles */
            body {
                margin: 0;
                padding: 0;
                font-family: 'Montserrat', sans-serif;
            }
            .header {
                background-color: #fff;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                position: fixed;
                width: 100%;
                top: 0;
                z-index: 1000;
            }
            .navbar {
                padding: 10px 0;
            }
            .navbar-brand {
                font-size: 24px;
                font-weight: 700;
                color: #007bff;
            }
            .navbar-nav .nav-link {
                font-size: 16px;
                font-weight: 500;
                color: #333;
                padding: 10px 15px;
                transition: color 0.3s ease;
            }
            .navbar-nav .nav-link:hover {
                color: #007bff;
            }
            .member-wrapper {
                position: relative;
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                background: url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80') no-repeat center center fixed;
                background-size: cover;
                padding-top: 80px;
            }
            .member-wrapper .background {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                z-index: 1;
            }
            .member-content {
                position: relative;
                z-index: 2;
                max-width: 450px;
                width: 100%;
                background: #ffffff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                text-align: center;
            }
            .login .title {
                font-size: 28px;
                font-weight: 700;
                color: #333;
                margin-bottom: 25px;
            }
            .form-group {
                margin-bottom: 20px;
                text-align: left;
            }
            .form-group label {
                font-size: 14px;
                font-weight: 500;
                color: #444;
                margin-bottom: 8px;
                display: block;
            }
            .form-control {
                width: 100%;
                padding: 12px 15px;
                font-size: 14px;
                border: 1px solid #ced4da;
                border-radius: 5px;
            }
            .form-control:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
                outline: none;
            }
            .btn-primary {
                background-color: #007bff;
                border: none;
                padding: 12px;
                font-size: 16px;
                font-weight: 500;
                border-radius: 5px;
                width: 100%;
            }
            .btn-primary:hover {
                background-color: #0056b3;
            }
            .btn-danger {
                background-color: #dc3545;
                border: none;
                padding: 12px;
                font-size: 16px;
                font-weight: 500;
                border-radius: 5px;
                width: 100%;
            }
            .btn-danger:hover {
                background-color: #c82333;
            }
            .btn-link {
                font-size: 14px;
                color: #007bff;
                text-decoration: none;
            }
            .btn-link:hover {
                text-decoration: underline;
                color: #0056b3;
            }
            .form-footer {
                text-align: center;
            }
            .mt-2 {
                margin-top: 10px;
            }
            .mt-3 {
                margin-top: 15px;
            }
            .logo img {
                height: 40px;
            }
            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
                display: none;
            }
        </style>
    </head>
    <body class="changeHeader">
        <!-- HEADER -->
        <header id="header" class="header">
            <div class="container">
                <nav class="navbar navbar-expand-lg navbar-light">
                    <div class="container-fluid">
                        <div class="logo">
                            <a href="#"><img src="<%= request.getContextPath()%>/images/Logo_G3.png" alt="SaigonTourist Logo"></a>
                        </div>
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav ms-auto">
                                <li class="nav-item">
                                    <a class="nav-link" href="<%= request.getContextPath()%>/SortTour">TRANG CHỦ</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
        </header>

        <!-- Register Form -->
        <div class="member-wrapper">
            <div class="background"></div>
            <div class="member-content">
                <div class="login clearfix">
                    <h4 class="text-center title">Đăng Ký</h4>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center" role="alert">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success text-center" role="alert">${success}</div>
                    </c:if>
                    <form id="register-form" method="POST" class="login-form" action="<%= request.getContextPath()%>/register">
                        <div class="form-group">
                            <label for="username">Tên tài khoản</label>
                            <input type="text" class="form-control" name="username" id="username" required placeholder="Nhập tên tài khoản">
                            <div class="error-message" id="username-error">Vui lòng nhập tên tài khoản.</div>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" name="email" id="email" required placeholder="Nhập email">
                            <div class="error-message" id="email-error">Vui lòng nhập email hợp lệ.</div>
                        </div>
                        <div class="form-group">
                            <label for="full_name">Họ và tên</label>
                            <input type="text" class="form-control" name="full_name" id="full_name" required placeholder="Nhập họ và tên">
                            <div class="error-message" id="full_name-error">Vui lòng nhập họ và tên.</div>
                        </div>
                        <div class="form-group">
                            <label for="phone_number">Số điện thoại</label>
                            <input type="text" class="form-control" name="phone_number" id="phone_number" required placeholder="Nhập số điện thoại">
                            <div class="error-message" id="phone_number-error">Vui lòng nhập số điện thoại.</div>
                        </div>
                        <div class="form-group">
                            <label for="password">Mật khẩu</label>
                            <input type="password" class="form-control" name="password" id="password" required placeholder="Nhập mật khẩu">
                            <div class="error-message" id="password-error">Vui lòng nhập mật khẩu.</div>
                        </div>
                        <div class="form-group">
                            <label for="confirm_password">Xác nhận mật khẩu</label>
                            <input type="password" class="form-control" name="confirm_password" id="confirm_password" required placeholder="Nhập lại mật khẩu">
                            <div class="error-message" id="confirm_password-error">Vui lòng xác nhận mật khẩu.</div>
                        </div>
                        <div class="form-footer">
                            <button type="submit" class="btn btn-primary btn-block">Đăng Ký</button>
                            <a href="<%= request.getContextPath()%>/SortTour" class="btn btn-danger btn-block mt-2">Hủy</a>
                            <div class="text-center mt-3">
                                Đã có tài khoản? <a href="login" class="btn btn-link">Đăng Nhập</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById('register-form').addEventListener('submit', function (event) {
                let isValid = true;
                const fields = ['username', 'email', 'full_name', 'phone_number', 'password', 'confirm_password'];

                fields.forEach(field => {
                    const input = document.getElementById(field);
                    const error = document.getElementById(field + '-error');
                    if (!input.value.trim()) {
                        isValid = false;
                        error.style.display = 'block';
                    } else {
                        error.style.display = 'none';
                    }
                });

                if (!isValid) {
                    event.preventDefault();
                }
            });
        </script>
    </body>
</html>