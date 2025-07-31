<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.AdminBooking" %>
<%@ page import="java.text.NumberFormat, java.util.Locale, java.text.SimpleDateFormat" %>

<%
    List<AdminBooking> bookingList = (List<AdminBooking>) request.getAttribute("bookings");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Booking</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            body {
                background: linear-gradient(to right, #4facfe, #00f2fe);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                flex-direction: column;
            }
            .container {
                background: white;
                padding: 20px;
                border-radius: 15px;
                box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
                max-width: 95%;
                overflow-x: auto;
            }
            h2 {
                text-align: center;
                font-weight: bold;
                color: black;
                background: rgba(0, 0, 0, 0.2);
                padding: 10px;
                border-radius: 10px;
                width: 100%;
                max-width: 600px;
                margin: 20px auto;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            thead {
                background: linear-gradient(135deg, #1e3c72, #2a5298);
                color: white;
                font-size: 16px;
            }
            th, td {
                text-align: center;
                vertical-align: middle;
                padding: 12px;
                border: 1px solid #ddd;
            }
            .status {
                font-weight: bold;
                padding: 6px 12px;
                border-radius: 8px;
                display: inline-block;
            }
            .status-pending {
                background: #ffc107;
                color: black;
            }
            .status-approved {
                background: #28a745;
                color: white;
            }
            .status-cancelled {
                background: #dc3545;
                color: white;
            }
            .btn-action {
                padding: 6px 12px;
                border-radius: 8px;
                font-weight: bold;
                transition: all 0.3s ease-in-out;
                border: none;
            }
            .btn-pending {
                background: blue;
                color: white;
            }
            .btn-pending:hover {
                background: darkblue;
            }
            .btn-confirmed {
                background: #28a745;
                color: white;
            }
            .btn-cancelled {
                background: #dc3545;
                color: white;
            }
            .btn-back {
                display: inline-block;
                margin-top: 20px;
                padding: 8px 16px;
                background: linear-gradient(135deg, #ff7e5f, #ff3f34);
                color: white;
                font-weight: bold;
                border-radius: 8px;
                text-transform: uppercase;
                font-size: 14px;
                transition: all 0.3s ease-in-out;
                box-shadow: 0 4px 8px rgba(255, 63, 52, 0.3);
                text-decoration: none;
            }
            .btn-back:hover {
                transform: scale(1.1);
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2>📋 Quản lý Booking</h2>

            <table class="table table-bordered text-center">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khách hàng</th>
                        <th>Tour</th>
                        <th>Ngày đi</th>
                        <th>Ngày về</th>
                        <th>Số người</th>
                        <th>Giá</th>
                        <th>Tổng tiền</th>
                        <th>Phương tiện</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (AdminBooking booking : bookingList) {%>
                    <tr>
                        <td><%= booking.getId()%></td>
                        <td><%= booking.getCustomerName()%></td>
                        <td><%= booking.getTourName()%></td>
                        <td><%= sdf.format(booking.getDeparture())%></td>
                        <td><%= sdf.format(booking.getEndDate())%></td>
                        <td><%= booking.getNumberOfPeople()%></td>
                        <td><%= currencyFormat.format(booking.getPrice())%></td>
                        <td><%= currencyFormat.format(booking.getTotalAmount())%></td>
                        <td><%= booking.getTransport()%></td>
                        <td>
                            <% if ("Pending".equals(booking.getStatus())) { %>
                            <span class="status status-pending">Pending</span>
                            <% } else if ("Confirmed".equals(booking.getStatus())) { %>
                            <span class="status status-approved">Đã duyệt</span>
                            <% } else { %>
                            <span class="status status-cancelled">Đã hủy</span>
                            <% } %>
                        </td>
                        <td>
                            <% if ("Pending".equals(booking.getStatus())) {%>
                            <!-- Ban đầu chỉ hiển thị chữ "Pending" -->
                            <div class="status status-pending" onclick="showDropdown(this)">
                                Pending
                            </div>


                            <!-- Select box sẽ được ẩn ban đầu -->
                            <form action="AdminBookingServlet" method="post" class="d-none">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="id" value="<%= booking.getId()%>">

                                <select name="status" class="form-select d-inline w-auto" onchange="this.form.submit()">
                                    <option value="Pending" selected hidden>Pending</option>
                                    <option value="Confirmed">✅ Confirm</option>
                                    <option value="Cancelled">❌ Cancel</option>
                                </select>
                            </form>
                            <% } else {%>
                            <button class="btn <%= "Confirmed".equals(booking.getStatus()) ? "btn-success" : "btn-danger"%>">
                                <%= booking.getStatus().equals("Confirmed") ? "Đã duyệt" : "Đã hủy"%>
                            </button>
                            <% } %>
                        </td>

                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>

        <a href="admin-dashboard" class="btn-back"><i class="fas fa-arrow-left"></i> Quay lại</a>
        <script>
            function showDropdown(element) {
                let form = element.nextElementSibling; // Tìm form ngay sau div "Pending"
                element.classList.add('d-none'); // Ẩn chữ "Pending"
                form.classList.remove('d-none'); // Hiện select box
            }
        </script>

    </body>
</html>
