<%-- Document : resetPassword Created on : June 7, 2025 Author : ACER --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>BookingTour - Đặt lại mật khẩu</title>
                <meta property="og:type" content="reset-password" />
                <meta property="og:title" content="BookingTour - Đặt lại mật khẩu" />
                <meta property="og:description" content="BookingTour - Đặt lại mật khẩu" />

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
                    .reset-password .title {
                        font-size: 28px;
                        font-weight: 700;
                        color: #333;
                        margin-bottom: 15px;
                        font-family: 'Montserrat', sans-serif;
                    }

                    .reset-password .description {
                        font-size: 14px;
                        color: #666;
                        margin-bottom: 25px;
                        line-height: 1.6;
                    }

                    .reset-password-form {
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

                    .password-requirements {
                        font-size: 12px;
                        color: #6c757d;
                        margin-top: 5px;
                        text-align: left;
                    }

                    .password-requirements ul {
                        margin: 5px 0 0 15px;
                        padding: 0;
                    }

                    .password-requirements li {
                        margin-bottom: 3px;
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

                    /* Password visibility toggle */
                    .password-toggle {
                        position: relative;
                    }

                    .password-toggle .toggle-btn {
                        position: absolute;
                        right: 10px;
                        top: 50%;
                        transform: translateY(-50%);
                        background: none;
                        border: none;
                        color: #6c757d;
                        cursor: pointer;
                        font-size: 16px;
                    }

                    .password-toggle .toggle-btn:hover {
                        color: #007bff;
                    }

                    /* Responsive Design */
                    @media (max-width: 576px) {
                        .member-content {
                            padding: 20px;
                            max-width: 90%;
                        }

                        .reset-password .title {
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

                        <!-- SweetAlert2 Messages -->
                        <c:if test="${not empty error}">
                            <script>
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi!',
                                    text: '${error}',
                                    confirmButtonColor: '#007bff'
                                });
                            </script>
                        </c:if>

                        <c:if test="${not empty success}">
                            <script>
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Thành công!',
                                    text: '${success}',
                                    confirmButtonColor: '#007bff',
                                    timer: 3000,
                                    timerProgressBar: true
                                }).then(function () {
                                    window.location.href = '<%= request.getContextPath()%>/login';
                                });
                            </script>
                        </c:if>

                        <c:if test="${empty success}">
                            <!-- Main Form -->
                            <div class="reset-password">
                                <h2 class="title">
                                    <i class="fas fa-lock"></i> Đặt lại mật khẩu
                                </h2>
                                <p class="description">
                                    Nhập mật khẩu mới cho tài khoản của bạn.
                                </p>

                                <form class="reset-password-form" action="reset-password" method="post">
                                    <input type="hidden" name="token" value="${param.token}">

                                    <div class="form-group">
                                        <label for="newPassword">
                                            <i class="fas fa-key"></i> Mật khẩu mới:
                                        </label>
                                        <div class="password-toggle">
                                            <input type="password" id="newPassword" name="newPassword"
                                                class="form-control" required placeholder="Nhập mật khẩu mới"
                                                minlength="6">
                                            <button type="button" class="toggle-btn"
                                                onclick="togglePassword('newPassword')">
                                                <i class="fas fa-eye" id="newPasswordIcon"></i>
                                            </button>
                                        </div>
                                        <div class="password-requirements">
                                            Mật khẩu phải có ít nhất:
                                            <ul>
                                                <li>6 ký tự</li>
                                                <li>Chứa ít nhất 1 chữ hoa</li>
                                                <li>Chứa ít nhất 1 chữ số</li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="confirmPassword">
                                            <i class="fas fa-check-circle"></i> Xác nhận mật khẩu:
                                        </label>
                                        <div class="password-toggle">
                                            <input type="password" id="confirmPassword" name="confirmPassword"
                                                class="form-control" required placeholder="Nhập lại mật khẩu mới"
                                                minlength="6">
                                            <button type="button" class="toggle-btn"
                                                onclick="togglePassword('confirmPassword')">
                                                <i class="fas fa-eye" id="confirmPasswordIcon"></i>
                                            </button>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save"></i> Cập nhật mật khẩu
                                    </button>
                                </form>
                            </div>
                        </c:if>

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

                <!-- Password visibility toggle function -->
                <script>
                    function togglePassword(fieldId) {
                        const field = document.getElementById(fieldId);
                        const icon = document.getElementById(fieldId + 'Icon');

                        if (field.type === 'password') {
                            field.type = 'text';
                            icon.classList.remove('fa-eye');
                            icon.classList.add('fa-eye-slash');
                        } else {
                            field.type = 'password';
                            icon.classList.remove('fa-eye-slash');
                            icon.classList.add('fa-eye');
                        }
                    }

                    // Password validation
                    document.getElementById('confirmPassword').addEventListener('input', function () {
                        const newPassword = document.getElementById('newPassword').value;
                        const confirmPassword = this.value;

                        if (newPassword !== confirmPassword && confirmPassword.length > 0) {
                            this.setCustomValidity('Mật khẩu xác nhận không khớp');
                        } else {
                            this.setCustomValidity('');
                        }
                    });

                    document.getElementById('newPassword').addEventListener('input', function () {
                        const confirmPassword = document.getElementById('confirmPassword');
                        if (confirmPassword.value.length > 0) {
                            confirmPassword.dispatchEvent(new Event('input'));
                        }
                    });
                </script>
            </body>

            </html>