<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Khách Hàng</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            /* Set background gradient */
            body {
                background: linear-gradient(135deg, #4facfe, #ff9a9e);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .container {
                max-width: 600px;
            }

            /* Card styling */
            .card {
                padding: 20px;
                border-radius: 12px;
                background: white;
                box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
                animation: fadeIn 0.5s ease-in-out;
            }

            /* Fade-in animation */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Input field styling */
            .form-control {
                border-radius: 8px;
                transition: all 0.3s ease-in-out;
            }

            /* Highlight input field when focused */
            .form-control:focus {
                border-color: #ff4b2b;
                box-shadow: 0px 0px 8px rgba(255, 75, 43, 0.5);
            }

            /* Primary button styling */
            .btn-primary {
                width: 100%;
                font-size: 18px;
                font-weight: bold;
                border-radius: 8px;
                background: linear-gradient(to right, #ff416c, #ff4b2b);
                border: none;
                transition: transform 0.2s, box-shadow 0.2s, background 0.3s;
            }

            /* Button hover effect */
            .btn-primary:hover {
                transform: translateY(-3px);
                box-shadow: 0px 4px 12px rgba(255, 75, 43, 0.5);
                background: linear-gradient(to right, #ff4b2b, #ff416c);
            }

            /* Secondary button styling */
            .btn-secondary {
                width: 100%;
                font-size: 18px;
                font-weight: bold;
                border-radius: 8px;
                background: #636e72;
                border: none;
                transition: transform 0.2s, box-shadow 0.2s;
            }

            /* Secondary button hover effect */
            .btn-secondary:hover {
                background: #2d3436;
                transform: scale(1.05);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card">
                <h2 class="text-center text-primary">Thêm Khách Hàng</h2>

                <!-- User registration form -->
                <form action="<%= request.getContextPath()%>/AdminUserServlet" method="post">
                    <input type="hidden" name="action" value="add">

                    <!-- Username input -->
                    <label class="fw-bold">Username:</label>
                    <input type="text" class="form-control" name="username" required>

                    <!-- Password input -->
                    <label class="fw-bold">Mật khẩu:</label>
                    <input type="password" class="form-control" name="password" required>

                    <!-- Full name input -->
                    <label class="fw-bold">Họ tên:</label>
                    <input type="text" class="form-control" name="fullName" required>

                    <!-- Email input -->
                    <label class="fw-bold">Email:</label>
                    <input type="email" class="form-control" name="email" required>

                    <!-- Phone number input -->
                    <label class="fw-bold">Số điện thoại:</label>
                    <input type="text" class="form-control" name="phoneNumber" required>

                    <!-- Role selection -->
                    <label class="fw-bold">Vai trò:</label>
                    <select class="form-control" name="userRole">
                        <option value="User">User</option>
                        <option value="Admin">Admin</option>
                    </select>

                    <!-- Address input -->
                    <label class="fw-bold">Địa chỉ:</label>
                    <input type="text" class="form-control" name="address">

                    <!-- Gender selection -->
                    <label class="fw-bold">Giới tính:</label>
                    <select class="form-control" name="gender">
                        <option value="Male">Nam</option>
                        <option value="Female">Nữ</option>
                        <option value="Other">Khác</option>
                    </select>

                    <!-- Country input -->
                    <label class="fw-bold">Quốc gia:</label>
                    <input type="text" class="form-control" name="country" required>

                    <!-- Date of birth input -->
                    <label class="fw-bold">Ngày sinh:</label>
                    <input type="date" class="form-control" name="dateOfBirth">

                    <!-- Workplace input -->
                    <label class="fw-bold">Nơi làm việc:</label>
                    <input type="text" class="form-control" name="workplace">

                    <!-- Job title input -->
                    <label class="fw-bold">Chức danh:</label>
                    <input type="text" class="form-control" name="jobTitle" required>

                    <!-- Submit button -->
                    <button type="submit" class="btn btn-primary mt-3">Thêm khách hàng</button>

                    <!-- Back button -->
                    <a href="<%= request.getContextPath()%>/AdminUserServlet" class="btn btn-secondary mt-2">Quay lại</a>
                </form>
            </div>
        </div>
    </body>
</html>
