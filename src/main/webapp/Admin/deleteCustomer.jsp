<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.AdminUser" %>
<%
    AdminUser user = (AdminUser) request.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xác nhận xóa khách hàng</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            body {
                background: linear-gradient(135deg, #4facfe, #ff4b2b);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .card {
                max-width: 500px;
                padding: 20px;
                border-radius: 12px;
                background: white;
                box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
                text-align: center;
                animation: fadeIn 0.5s ease-in-out;
            }
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
            .card h2 {
                color: #d63031;
                font-weight: bold;
            }
            .btn-danger {
                background: #d63031;
                border: none;
                padding: 10px 20px;
                border-radius: 8px;
                font-weight: bold;
                transition: all 0.3s ease-in-out;
                box-shadow: 0 4px 10px rgba(214, 48, 49, 0.3);
            }
            .btn-danger:hover {
                background: #c0392b;
                transform: scale(1.05);
                box-shadow: 0 6px 15px rgba(214, 48, 49, 0.5);
            }
            .btn-secondary {
                background: #636e72;
                border: none;
                padding: 10px 20px;
                border-radius: 8px;
                font-weight: bold;
                transition: all 0.3s ease-in-out;
            }
            .btn-secondary:hover {
                background: #2d3436;
                transform: scale(1.05);
            }
        </style>
    </head>
    <body>
        <div class="card">
            <%-- Check if the user object is null to determine if the customer exists --%>
            <% if (user == null) { %>
            <%-- Display a message indicating that the customer was not found or has been deleted --%>
            <h2>Không tìm thấy khách hàng</h2>
            <p>Khách hàng này không tồn tại hoặc đã bị xóa.</p>
            <%-- Provide a link to return to the user list --%>
            <a href="AdminUserServlet" class="btn btn-secondary">🔙 Quay lại danh sách</a>
            <% } else {%>
            <%-- If the user exists, display a confirmation message for deletion --%>
            <h2>Xác nhận xóa</h2>
            <%-- Show the customer's name, or "Không xác định" if the name is null --%>
            <p>Bạn có chắc chắn muốn xóa khách hàng <b><%= user.getFullName() != null ? user.getFullName() : "Không xác định"%></b> không?</p>
            <%-- Form to submit the delete action --%>
            <form action="AdminUserServlet" method="post">
                <%-- Hidden input to specify the action as 'delete' --%>
                <input type="hidden" name="action" value="delete">
                <%-- Hidden input to pass the user's ID --%>
                <input type="hidden" name="id" value="<%= user.getId()%>">
                <%-- Submit button to confirm deletion --%>
                <button type="submit" class="btn btn-danger">🗑 Xóa</button>
                <%-- Link to cancel the deletion and return to the user list --%>
                <a href="AdminUserServlet" class="btn btn-secondary">❌ Hủy</a>
            </form>
            <% }%>
        </div>
    </body>
</html>
