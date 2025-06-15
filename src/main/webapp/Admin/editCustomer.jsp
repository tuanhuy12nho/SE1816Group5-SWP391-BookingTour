<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="editUser" value="${requestScope.editUser}"/>
<%@ page import="java.util.List, model.AdminUser" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa khách hàng</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            body {
                background: linear-gradient(135deg, #4facfe, #ff9a9e);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                position: relative;
            }
            .container {
                max-width: 600px;
            }
            .card {
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s, box-shadow 0.3s;
            }
            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            }
            .form-control {
                margin-bottom: 10px;
                border-radius: 8px;
                transition: all 0.3s ease-in-out;
            }
            .form-control:focus {
                border-color: #ff4b2b;
                box-shadow: 0px 0px 8px rgba(255, 75, 43, 0.5);
            }
            .btn-primary {
                width: 100%;
                font-size: 18px;
                font-weight: bold;
                border-radius: 8px;
                background: linear-gradient(to right, #ff416c, #ff4b2b);
                border: none;
                transition: transform 0.2s, box-shadow 0.2s, background 0.3s;
            }
            .btn-primary:hover {
                transform: translateY(-3px);
                box-shadow: 0px 4px 12px rgba(255, 75, 43, 0.5);
                background: linear-gradient(to right, #ff4b2b, #ff416c);
            }
            .btn-secondary {
                width: 100%;
                font-size: 18px;
                font-weight: bold;
                border-radius: 8px;
                background: #6c757d;
                border: none;
                transition: transform 0.2s, box-shadow 0.2s;
            }
            .btn-secondary:hover {
                transform: translateY(-3px);
                box-shadow: 0px 4px 12px rgba(108, 117, 125, 0.5);
                background: #495057;
            }
            /* Nút Back góc trái dưới */
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
            .btn-back:hover {
                background: linear-gradient(135deg, #e84118, #c0392b);
                transform: scale(1.1);
                box-shadow: 0 6px 15px rgba(255, 63, 52, 0.5);
            }
        </style>
    </head>
    <body>
        <div class="container mt-5">
            <div class="card">
                <%-- Page heading indicating that this section is for editing customer information --%>
                <h2 class="text-center text-primary">Chỉnh Sửa Thông Tin Khách Hàng</h2>
                <%-- Form to submit updated customer information to AdminUserServlet using POST method --%>
                <form action="AdminUserServlet" method="post">
                    <%-- Hidden input specifying the action as 'update' for the servlet --%>
                    <input type="hidden" name="action" value="update">
                    <%-- Hidden input passing the customer's ID to identify the record to update --%>
                    <input type="hidden" name="id" value="${editUser.id}">

                    <%-- Username field, set as read-only to prevent modifications --%>
                    <label class="fw-bold">Username:</label>
                    <input type="text" name="username" value="${editUser.username}" class="form-control" readonly>

                    <%-- Full name field, set as read-only to prevent changes --%>
                    <label class="fw-bold">Họ Tên:</label>
                    <input type="text" name="fullName" value="${editUser.fullName}" class="form-control" readonly>

                    <%-- Email field, set as read-only to prevent editing --%>
                    <label class="fw-bold">Email:</label>
                    <input type="email" name="email" value="${editUser.email}" class="form-control" readonly>

                    <%-- Password field is commented out, indicating it is not editable in this form --%>
                    <!--
                    <label class="fw-bold">Mật khẩu:</label>
                    <input type="password" name="password" value="${editUser.password}" class="form-control" readonly>
                    -->

                    <%-- Phone number field, editable and required for submission --%>
                    <label class="fw-bold">Số Điện Thoại:</label>
                    <input type="text" name="phoneNumber" value="${editUser.phoneNumber}" class="form-control" required>

                    <%-- Address field, editable and required for submission --%>
                    <label class="fw-bold">Địa Chỉ:</label>
                    <input type="text" name="address" value="${editUser.address}" class="form-control" required>

                    <%-- Gender dropdown menu, pre-selects the customer's current gender --%>
                    <label class="fw-bold">Giới Tính:</label>
                    <select name="gender" class="form-control">
                        <option value="Male" ${editUser.gender == 'Male' ? 'selected' : ''}>Nam</option>
                        <option value="Female" ${editUser.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                    </select>

                    <%-- Country field, editable and required for submission --%>
                    <label class="fw-bold">Quốc Gia:</label>
                    <input type="text" name="country" value="${editUser.country}" class="form-control" required>

                    <%-- Date of birth field, editable and required, using date input type --%>
                    <label class="fw-bold">Ngày Sinh:</label>
                    <input type="date" name="dateOfBirth" value="${editUser.dateOfBirth}" class="form-control" required>

                    <%-- Workplace field, editable and required for submission --%>
                    <label class="fw-bold">Nơi Làm Việc:</label>
                    <input type="text" name="workplace" value="${editUser.workplace}" class="form-control" required>

                    <%-- Job title field, editable and required for submission --%>
                    <label class="fw-bold">Chức Danh:</label>
                    <input type="text" name="jobTitle" value="${editUser.jobTitle}" class="form-control" required>

                    <%-- Submit button to send the updated information to the servlet --%>
                    <button type="submit" class="btn btn-primary mt-3">Cập Nhật</button>
                    <%-- Cancel button linking back to the AdminUserServlet to abort editing --%>
                    <a href="AdminUserServlet" class="btn btn-secondary mt-2">Hủy</a>
                </form>
            </div>
        </div>

        <%-- Back button at the bottom-left corner to return to the user list --%>
        <a href="AdminUserServlet" class="btn btn-back">
            ⬅ 
        </a>
    </body>
</html>
