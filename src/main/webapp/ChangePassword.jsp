<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đổi Mật Khẩu</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

        <!-- Custom CSS -->
        <style>
            /* Change Password Section */
            .change-password-section {
                background: url('https://dulichviet.com.vn/images/bandidau/lac-vao-khung-canh-tho-mong-cua-thi-tran-hallstatt-khi-du-lich-ao.jpg') no-repeat center center fixed;
                background-size: cover;
                padding: 50px 0;
                min-height: calc(100vh - 200px);
                position: relative;
            }
            .change-password-section::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.4); /* Dark overlay for readability */
                z-index: 1;
            }
            .change-password-container {
                position: relative;
                z-index: 2;
                max-width: 600px; /* Smaller width since it's a shorter form */
                margin: 0 auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                padding: 30px;
            }

            /* Header Styling */
            .change-password-header {
                font-size: 28px;
                font-weight: bold;
                color: #333;
                margin-bottom: 30px;
                text-align: center;
                position: relative;
            }
            .change-password-header::after {
                content: '';
                width: 50px;
                height: 3px;
                background: #003087; /* Blue underline */
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
            }

            /* Form Styling */
            .form-label {
                font-size: 15px;
                font-weight: 600;
                color: #333;
                margin-bottom: 5px;
            }
            .form-control {
                background: #f9f9f9;
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 10px;
                font-size: 14px;
                color: #555;
                transition: border-color 0.3s ease, box-shadow 0.3s ease;
            }
            .form-control:focus {
                border-color: #003087;
                box-shadow: 0 0 5px rgba(0, 48, 135, 0.3);
                outline: none;
            }
            .mb-3 {
                margin-bottom: 20px !important;
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
            .btn-submit {
                background-color: #28a745; /* Green button to match profile page */
                color: #fff;
                border: none;
            }
            .btn-submit:hover {
                background-color: #218838; /* Darker green on hover */
                transform: translateY(-2px);
            }
            .btn-secondary {
                background-color: #6c757d;
                color: #fff;
                border: none;
            }
            .btn-secondary:hover {
                background-color: #5a6268;
                transform: translateY(-2px);
            }
            .btn-primary {
                background-color: #28a745;
                color: #fff;
                border: none;
            }
            .btn-primary:hover {
                background-color: #218838;
                transform: translateY(-2px);
            }
            .text-center {
                text-align: center;
                display: flex;
                justify-content: center;
                gap: 15px;
            }

            /* Error Message */
            .error-message {
                text-align: center;
                padding: 15px;
                background: #ffebee;
                border-radius: 5px;
                margin-top: 20px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            }
            .error-message p {
                font-size: 15px;
                color: #e74c3c;
                margin: 0;
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
                .change-password-container {
                    margin: 0 15px;
                    padding: 20px;
                }
                .change-password-header {
                    font-size: 24px;
                }
                .form-label {
                    font-size: 14px;
                }
                .form-control {
                    font-size: 13px;
                    padding: 8px;
                }
                .btn-custom {
                    padding: 8px 20px;
                    font-size: 14px;
                }
            }
        </style>
    </head>

    <body class="changeHeader">
        <div id="wrap">
            <!-- Include the header JSP file to display the common header section -->
            <%@include file="/WEB-INF/inclu/head.jsp" %>

            <!-- Section for the change password functionality -->
            <section class="change-password-section">
                <!-- Container for the change password form -->
                <div class="change-password-container">
                    <!-- Heading for the change password page -->
                    <h2 class="change-password-header">Đổi Mật Khẩu</h2>

                    <!-- Retrieve the user object from the session to check if the user is logged in -->
                    <%
                        User user = (User) session.getAttribute("user");
                        if (user != null) { // If user is logged in, display the change password form
                    %>
                    <!-- Form to submit the password change request to UserServlet with POST method -->
                    <form action="UserServlet" method="post">
                        <!-- Hidden input to specify the action as 'changePassword' for the servlet -->
                        <input type="hidden" name="action" value="changePassword">

                        <!-- Input field for the old password -->
                        <div class="mb-3">
                            <label for="oldPassword" class="form-label">Mật Khẩu Cũ:</label>
                            <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                        </div>

                        <!-- Input field for the new password -->
                        <div class="mb-3">
                            <label for="newPassword" class="form-label">Mật Khẩu Mới:</label>
                            <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                        </div>

                        <!-- Input field to confirm the new password -->
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Xác Nhận Mật Khẩu Mới:</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        </div>

                        <!-- Buttons for submitting the form and returning to the user page -->
                        <div class="text-center">
                            <!-- Submit button to update the password -->
                            <button type="submit" class="btn btn-submit btn-custom text-white">Cập nhật</button>
                            <!-- Link to return to the user page without making changes -->
                            <a href="<%= request.getContextPath()%>/UserServlet" class="btn btn-secondary btn-custom">Quay lại</a>
                        </div>

                        <!-- Display an error message if there is an error attribute set in the request -->
                        <% if (request.getAttribute("error") != null) {%>
                        <div class="error-message">
                            <p class="mb-0"><%= request.getAttribute("error")%></p>
                        </div>
                        <% } %>
                    </form>

                    <!-- If the user is not logged in, display a message prompting them to log in -->
                    <% } else {%>
                    <div class="login-message">
                        <p class="mb-3">Bạn cần đăng nhập để đổi mật khẩu.</p>
                        <!-- Link to the login page -->
                        <a href="<%= request.getContextPath()%>/login" class="btn btn-primary btn-custom">Đăng nhập</a>
                    </div>
                    <% }%>
                </div>
            </section>

            <!-- Include the footer JSP file to display the common footer section -->
            <%@include file="/WEB-INF/inclu/footer.jsp" %>

            <!-- Include Bootstrap JavaScript bundle for form styling and functionality -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        </div>
    </body>
</html>