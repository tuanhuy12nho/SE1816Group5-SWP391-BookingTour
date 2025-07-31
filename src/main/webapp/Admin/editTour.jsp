<%@page import="model.AdminTour"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AdminTour tour = (AdminTour) request.getAttribute("tour");
    if (tour == null) {
%>
<h3 style="color: red;">❌ Lỗi: Không tìm thấy tour!</h3>
<a href="<%= request.getContextPath()%>/adminTour">Quay lại danh sách</a>
<%
        return;
    }

    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<div style="color: green; font-weight: bold; margin-bottom: 10px;">
    <%= message%>
</div>
<%
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa Tour</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/asset/style.css">
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(to right, #74ebd5, #ACB6E5);
                margin: 0;
                padding: 20px;
            }

            h2 {
                text-align: center;
                color: #333;
                font-weight: 600;
                text-shadow: 1px 1px 3px rgba(0,0,0,0.2);
            }

            .form-container {
                width: 550px;
                max-height: 80vh;
                overflow-y: auto;
                padding: 30px;
                border-radius: 12px;
                background-color: rgba(255, 255, 255, 0.85);
                backdrop-filter: blur(10px);
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
                margin: 20px auto;
            }

            label {
                font-weight: bold;
                display: block;
                margin-top: 15px;
                color: #444;
            }

            input[type="text"],
            input[type="number"],
            input[type="date"],
            textarea {
                width: 100%;
                padding: 10px;
                margin-top: 5px;
                box-sizing: border-box;
                border-radius: 6px;
                border: 1px solid #ddd;
                transition: border-color 0.3s, box-shadow 0.3s;
            }

            input[type="text"]:focus,
            input[type="number"]:focus,
            input[type="date"]:focus,
            textarea:focus {
                border-color: #74ebd5;
                box-shadow: 0 0 8px rgba(116,235,213,0.6);
                outline: none;
            }

            input[type="file"] {
                padding: 6px;
                margin-top: 8px;
            }

            input[type="radio"] {
                margin-right: 5px;
            }

            button {
                background: linear-gradient(to right, #74ebd5, #ACB6E5);
                color: #fff;
                font-weight: bold;
                text-transform: uppercase;
                border: none;
                border-radius: 8px;
                padding: 12px;
                cursor: pointer;
                margin-top: 20px;
                transition: background 0.3s ease, transform 0.2s ease;
            }

            button:hover {
                background: linear-gradient(to right, #ACB6E5, #74ebd5);
                transform: translateY(-2px);
            }

            a {
                color: #444;
                text-decoration: none;
                transition: color 0.3s;
            }

            a:hover {
                color: #0056b3;
            }

            ::-webkit-scrollbar {
                width: 6px;
            }

            ::-webkit-scrollbar-track {
                background: transparent;
            }

            ::-webkit-scrollbar-thumb {
                background: linear-gradient(to bottom, #74ebd5, #ACB6E5);
                border-radius: 10px;
            }

        </style>
    </head>
    <body>
        <%-- Page heading indicating that this section is for editing a tour --%>
        <h2>✏ Chỉnh sửa Tour</h2>
        <div class="form-container">
            <%-- Form to submit updated tour information to /admineditTour using POST method, supporting file uploads --%>
            <form action="<%= request.getContextPath()%>/admineditTour" method="post" enctype="multipart/form-data">
                <%-- Hidden input to pass the tour's ID for identifying which tour to update --%>
                <input type="hidden" name="id" value="<%= tour.getId()%>">

                <%-- Input field for the tour's name, pre-filled with the current value --%>
                <label>Tên Tour:</label>
                <input type="text" name="name" value="<%= tour.getName()%>" required>

                <%-- Input field for the tour's location, pre-filled with the current value --%>
                <label>Địa điểm:</label>
                <input type="text" name="location" value="<%= tour.getLocation()%>" required>

                <%-- Input field for the tour's transportation method, pre-filled with the current value --%>
                <label>Phương tiện:</label>
                <input type="text" name="transport" value="<%= tour.getTransport()%>" required>

                <%-- Input field for the tour's price, pre-filled with the current value --%>
                <label>Giá:</label>
                <input type="number" name="price" value="<%= tour.getPrice()%>" required>

                <%-- Textarea for the tour's description, pre-filled with the current value --%>
                <label>Mô tả:</label>
                <textarea name="description" required><%= tour.getDescription()%></textarea>

                <%-- Input field for the tour's start date, pre-filled with the current value --%>
                <label>Ngày bắt đầu:</label>
                <input type="date" name="startDate" value="<%= tour.getStartDate()%>" required>

                <%-- Input field for the tour's end date, pre-filled with the current value --%>
                <label>Ngày kết thúc:</label>
                <input type="date" name="endDate" value="<%= tour.getEndDate()%>" required>

                <%-- Display the current tour image --%>
                <label>Ảnh hiện tại:</label><br>
                <img src="<%= request.getContextPath() + "/" + tour.getImageUrl()%>" width="200"><br>

                <%-- File input to upload a new image if the user wants to change the current one --%>
                <label>Chọn ảnh mới (nếu muốn đổi):</label>
                <input type="file" name="image">

                <%-- Radio buttons to select the tour type (Domestic or International), with the current type pre-selected --%>
                <label>Loại Tour:</label>
                <div>
                    <input type="radio" id="domestic" name="type" value="Domestic" 
                           <%= "Domestic".equals(tour.getType()) ? "checked" : ""%> required>
                    <label for="domestic">Trong nước</label>

                    <input type="radio" id="international" name="type" value="International" 
                           <%= "International".equals(tour.getType()) ? "checked" : ""%> required>
                    <label for="international">Ngoài nước</label>
                </div>

                <%-- Submit button to send the updated tour information --%>
                <button type="submit">Cập nhật</button>
            </form>
        </div>
        <%-- Link to return to the tour list page --%>
        <a href="<%=request.getContextPath()%>/adminTour">🔙 Quay lại danh sách</a>
    </body>
</html>
