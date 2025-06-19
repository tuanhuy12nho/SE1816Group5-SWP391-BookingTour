<%-- Document : forgotPassword Created on : June 7, 2025 Author : ACER --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>BookingTour - Quên mật khẩu</title>
                <meta property="og:type" content="forgot-password" />
                <meta property="og:title" content="BookingTour - Quên mật khẩu" />
                <meta property="og:description" content="BookingTour - Quên mật khẩu" />

                <!-- Bootstrap CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- Font Awesome -->
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
                <!-- SweetAlert2 -->
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <!-- Google Fonts -->
                <link
                    href="https://fonts.googleapis.com/css?family=Montserrat:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&amp;subset=vietnamese"
                    rel="stylesheet">

                <style>
                    /* Member Wrapper */
                    .member-wrapper {
                        position: relative;
                        min-height: 100vh;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        background: url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80') no-repeat center center fixed;
                        background-size: cover;
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
                        max-width: 400px;
                        width: 100%;
                        background: #ffffff;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                        text-align: center;
                    }

                    /* Header */
                    .form-header {
                        margin-bottom: 30px;
                    }

                    .form-header .logo {
                        font-size: 32px;
                        font-weight: 700;
                        color: #007bff;
                        margin-bottom: 10px;
                        font-family: 'Montserrat', sans-serif;
                    }

                    .form-header .subtitle {
                        font-size: 14px;
                        color: #6c757d;
                        margin-bottom: 20px;
                    }

                    /* Form */
                    .forgot-password .title {
                        font-size: 28px;
                        font-weight: 700;
                        color: #333;
                        margin-bottom: 15px;
                        font-family: 'Montserrat', sans-serif;
                    }

                    .forgot-password .description {
                        font-size: 14px;
                        color: #666;
                        margin-bottom: 25px;
                        line-height: 1.6;
                    }

                    .forgot-password-form {
                        width: 100%;
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
                        transition: border-color 0.3s ease, box-shadow 0.3s ease;
                    }

                    .form-control:focus {
                        border-color: #007bff;
                        box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
                        outline: none;
                    }

                    .form-control::placeholder {
                        color: #999;
                    }

                    .btn-primary {
                        background-color: #007bff;
                        border: none;
                        padding: 12px;
                        font-size: 16px;
                        font-weight: 500;
                        border-radius: 5px;
                        width: 100%;
                        transition: background-color 0.3s ease;
                        margin-bottom: 20px;
                    }

                    .btn-primary:hover {
                        background-color: #0056b3;
                    }

                    /* Footer */
                    .form-footer {
                        text-align: center;
                        margin-top: 20px;
                        padding-top: 20px;
                        border-top: 1px solid #e9ecef;
                    }

                    .back-link {
                        color: #007bff;
                        text-decoration: none;
                        font-weight: 500;
                        font-size: 14px;
                    }

                    .back-link:hover {
                        text-decoration: underline;
                        color: #0056b3;
                    }

                    .copyright {
                        font-size: 12px;
                        color: #6c757d;
                        margin-top: 15px;
                    }

                    /* Responsive Design */
                    @media (max-width: 576px) {
                        .member-content {
                            padding: 20px;
                            max-width: 90%;
                        }

                        .forgot-password .title {
                            font-size: 24px;
                        }

                        .btn-primary {
                            font-size: 14px;
                        }
                    }
                </style>
            </head>

            <body>
                <div class="member-wrapper">
                    <div class="background"></div>
                    <div class="member-content">
                        <!-- Header -->
                        <div class="form-header">
                            <div class="logo">
                                <i class="fas fa-plane"></i> BookingTour
                            </div>
                            <div class="subtitle">Hệ thống đặt tour du lịch</div>
                        </div>

                        <!-- Main Form -->
                        <div class="forgot-password">
                            <h2 class="title">
                                <i class="fas fa-key"></i> Quên mật khẩu
                            </h2>
                            <p class="description">
                                Nhập địa chỉ email của bạn và chúng tôi sẽ gửi cho bạn liên kết để đặt lại mật khẩu.
                            </p>

                            <form class="forgot-password-form" action="forgot-password" method="post">
                                <div class="form-group">
                                    <label for="email">
                                        <i class="fas fa-envelope"></i> Địa chỉ email:
                                    </label>
                                    <input type="email" id="email" name="email" class="form-control" required
                                        placeholder="Nhập địa chỉ email của bạn" value="${param.email}">
                                </div>

                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-paper-plane"></i> Gửi liên kết đặt lại
                                </button>
                            </form>
                        </div>

                        <!-- Footer -->
                        <div class="form-footer">
                            <a href="<%= request.getContextPath()%>/login" class="back-link">
                                <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
                            </a>
                            <div class="copyright">
                                © 2025 BookingTour. All rights reserved.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- SweetAlert2 Messages -->
                <script>
        <c:if test="${not empty error}">
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: '${error}',
                confirmButtonColor: '#007bff'
            });
        </c:if>

        <c:if test="${not empty success}">
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: '${success}',
                confirmButtonColor: '#007bff'
            });
        </c:if>
                </script>
            </body>