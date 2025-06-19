<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Itinerary" %>

<%
    List<Itinerary> itineraries = (List<Itinerary>) request.getAttribute("itineraries");
    model.Itinerary itineraryEdit = (model.Itinerary) request.getAttribute("itineraryEdit");
    int tourId = (int) request.getAttribute("tourId");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Quản lý Hành trình Tour</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                padding: 20px;
                background: #f9f9f9;
            }
            .container {
                max-width: 700px;
                margin: auto;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            h2 {
                margin-bottom: 20px;
                color: #333;
            }
            form {
                margin-bottom: 30px;
                background: #f0f4f8;
                padding: 15px;
                border-radius: 8px;
            }

            a.action-btn {
                text-decoration: none;
                color: white;
                padding: 6px 12px;
                border-radius: 4px;
                font-size: 14px;
                display: inline-block;
                /* margin-right: 12px;  Nếu dùng flexbox và gap, có thể bỏ dòng này */
            }

            label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
                color: #444;
            }
            input[type="number"], textarea {
                width: 100%;
                padding: 8px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 14px;
                resize: vertical;
            }
            button {
                background-color: #007BFF;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                transition: background-color 0.3s ease;
            }
            button:hover {
                background-color: #0056b3;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                padding: 12px 8px;
                border-bottom: 1px solid #ddd;
                text-align: left;
            }
            th {
                background-color: #f0f0f0;
            }
            a.action-btn {
                margin-right: 10px;
                text-decoration: none;
                color: white;
                padding: 6px 12px;
                border-radius: 4px;
                font-size: 14px;
            }
            a.edit-btn {
                background-color: #28a745;
            }
            a.edit-btn:hover {
                background-color: #1e7e34;
            }
            a.delete-btn {
                background-color: #dc3545;
            }
            a.delete-btn:hover {
                background-color: #bd2130;
            }
            a.back-link {
                display: inline-block;
                margin-top: 15px;
                color: #007bff;
                text-decoration: none;
                font-weight: bold;
            }
            a.back-link:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Quản lý Hành trình Tour (Tour ID: <%= tourId%>)</h2>

            <form action="<%= request.getContextPath()%>/adminItinerary" method="post">
                <input type="hidden" name="id" value="<%= (itineraryEdit != null) ? itineraryEdit.getId() : ""%>">
                <input type="hidden" name="tourId" value="<%= tourId%>">

                <label for="dayNumber">Ngày thứ :</label>
                <input type="number" id="dayNumber" name="dayNumber" min="1" required
                       value="<%= (itineraryEdit != null) ? itineraryEdit.getDayNumber() : 1%>">

                <label for="description">Mô tả:</label>
                <textarea id="description" name="description" rows="4" required><%= (itineraryEdit != null) ? itineraryEdit.getDescription() : ""%></textarea>

                <button type="submit"><%= (itineraryEdit != null) ? "Cập nhật" : "Thêm mới"%></button>
            </form>

            <table>
                <thead>
                    <tr>
                        <th>Ngày thứ</th>
                        <th>Mô tả</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (itineraries != null && !itineraries.isEmpty()) {
                    for (Itinerary i : itineraries) {%>
                    <tr>
                        <td><%= i.getDayNumber()%></td>
                        <td><%= i.getDescription()%></td>
                        <td>
                            <a href="<%= request.getContextPath()%>/adminItinerary?action=edit&tourId=<%= tourId%>&id=<%= i.getId()%>" class="action-btn edit-btn">Sửa</a>
                            <a href="<%= request.getContextPath()%>/adminItinerary?action=delete&tourId=<%= tourId%>&id=<%= i.getId()%>" class="action-btn delete-btn" onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</a>
                        </td>
                    </tr>
                    <%  }
            } else { %>
                    <tr><td colspan="3" style="text-align:center;">Chưa có hành trình nào.</td></tr>
                    <% }%>
                </tbody>
            </table>

            <a href="<%= request.getContextPath() + "/adminTour"%>" class="back-link">← Quay lại danh sách Tour</a>
        </div>
    </body>
</html>
