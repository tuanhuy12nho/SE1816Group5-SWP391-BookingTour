<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Thêm Tour mới</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/asset/editTour.css">
        <style>

            .form-container {
                width: 500px;
                height: 600px;
                overflow-y: auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
                margin: auto;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }

            label {
                font-weight: bold;
                display: block;
                margin-top: 15px;
            }

            input, textarea {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
                box-sizing: border-box;
            }

            button {
                margin-top: 20px;
                width: 100%;
                padding: 10px;
                cursor: pointer;
            }
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

        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
        <div style="color: red; text-align: center; font-weight: bold;">
            <%= message%>
        </div>
        <%
            }
        %>

        <h2 style="text-align:center;">Thêm Tour mới</h2>

        <div class="form-container">
            <form action="<%= request.getContextPath()%>/adminaddTour" method="post" enctype="multipart/form-data">
                <%-- Input for tour name, marked as required, retains previous value if available --%>
                <label>Tên Tour:</label>
                <input type="text" name="name" required value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : ""%>">
                <%-- Input for tour location, required, retains previous value if available --%>
                <label>Địa điểm:</label>
                <input type="text" name="location" required value="<%= request.getAttribute("location") != null ? request.getAttribute("location") : ""%>">
                <%-- Input for transport method, required, retains previous value if available --%>
                <label>Phương tiện:</label>
                <input type="text" name="transport" required value="<%= request.getAttribute("transport") != null ? request.getAttribute("transport") : ""%>">
                <%-- Input for tour price, required, retains previous value if available --%>
                <label>Giá:</label>
                <input type="number" name="price" required value="<%= request.getAttribute("price") != null ? request.getAttribute("price") : ""%>">
                <%-- Textarea for tour description, required, retains previous value if available --%>
                <label>Mô tả:</label>
                <textarea name="description" required><%= request.getAttribute("description") != null ? request.getAttribute("description") : ""%></textarea>
                <%-- Input for tour start date, required, retains previous value if available --%>
                <label>Ngày bắt đầu:</label>
                <input type="date" name="startDate" required value="<%= request.getAttribute("startDate") != null ? request.getAttribute("startDate") : ""%>">
                <%-- Input for tour end date, required, retains previous value if available --%>
                <label>Ngày kết thúc:</label>
                <input type="date" name="endDate" required value="<%= request.getAttribute("endDate") != null ? request.getAttribute("endDate") : ""%>">
                <%-- File input for uploading a tour image --%>
                <label>Ảnh tour:</label>
                <input type="file" name="image">

                <%-- Radio buttons for selecting tour type (Domestic or International) --%>
                <label>Loại tour:</label>

                <input type="radio" name="type" value="Domestic" 
                       <%= "Domestic".equals(request.getAttribute("type")) || request.getAttribute("type") == null ? "checked" : ""%>> Trong nước
                <input type="radio" name="type" value="International" 
                       <%= "International".equals(request.getAttribute("type")) ? "checked" : ""%>> Ngoài nước

                <button type="submit">Thêm tour</button>
            </form>
        </div>

        <div style="text-align:center; margin-top:15px;">
            <a href="<%=request.getContextPath()%>/adminTour">🔙 Quay lại danh sách</a>
        </div>
    </body>
</html>