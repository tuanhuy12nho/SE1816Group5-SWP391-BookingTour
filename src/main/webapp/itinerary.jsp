<%@page import="java.util.List"%>
<%@page import="model.Itinerary"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Itinerary> itineraries = (List<Itinerary>) request.getAttribute("itineraries");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch trình tour</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
    <div class="container">
        <h2>Lịch Trình Chi Tiết</h2>
        <% if (error != null) { %>
            <p class="text-danger"><%= error %></p>
        <% } else if (itineraries != null && !itineraries.isEmpty()) { %>
        <ul class="list-group">
            <% for (Itinerary i : itineraries) { %>
                <li class="list-group-item">
                    <strong>Ngày <%= i.getDayNumber() %>:</strong>
                    <%= (i.getDescription() != null) ? i.getDescription() : "Chưa có mô tả" %>
                </li>
            <% } %>
        </ul>
        <% } else { %>
        <p class="text-center">Chưa có lịch trình cho tour này.</p>
        <% } %>
    </div>
</body>
</html>
