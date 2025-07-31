<%-- 
    Document   : bookingList
    Created on : Mar 7, 2025
    Author     : Admin
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="model.Booking" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Booking List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

        <style>
            /* Booking List Section */
            .booking-section {
                background: url('https://dulichviet.com.vn/images/bandidau/lac-vao-khung-canh-tho-mong-cua-thi-tran-hallstatt-khi-du-lich-ao.jpg') no-repeat center center fixed;
                background-size: cover;
                padding: 50px 0;
                min-height: calc(100vh - 200px);
                position: relative;
            }
            .booking-section::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.4); /* Dark overlay for readability */
                z-index: 1;
            }
            .booking-container {
                position: relative;
                z-index: 2;
                max-width: 1200px;
                margin: 0 auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                overflow: hidden;
            }

            /* Main Content */
            .main-content {
                padding: 30px;
            }
            .booking-header {
                font-size: 28px;
                font-weight: bold;
                color: #333;
                margin-bottom: 30px;
                text-align: center;
                position: relative;
            }
            .booking-header::after {
                content: '';
                width: 50px;
                height: 3px;
                background: #003087; /* Blue underline */
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
            }

            /* Table Styling */
            .table {
                background: #f9f9f9;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                margin-bottom: 0;
            }
            .table thead {
                background: #003087; /* Blue header background */
                color: #fff;
            }
            .table thead th {
                font-weight: bold;
                font-size: 15px;
                padding: 15px;
                text-align: center;
                border: none;
            }
            .table tbody tr {
                transition: background-color 0.3s ease;
            }
            .table tbody tr:hover {
                background: #e0e0e0;
            }
            .table tbody td {
                font-size: 14px;
                color: #555;
                padding: 15px;
                text-align: center;
                border: 1px solid #ddd;
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
            .btn-info {
                background-color: #28a745; /* Green button to match profile page */
                color: #fff;
                border: none;
            }
            .btn-info:hover {
                background-color: #218838; /* Darker green on hover */
                transform: translateY(-2px);
            }

            /* Responsive */
            @media (max-width: 768px) {
                .booking-container {
                    margin: 0 15px;
                }
                .main-content {
                    padding: 20px;
                }
                .booking-header {
                    font-size: 24px;
                }
                .table thead th, .table tbody td {
                    font-size: 13px;
                    padding: 10px;
                }
                .table th:nth-child(8), .table td:nth-child(8) {
                    width: auto;
                }
            }
        </style>
    </head>

    <body class="changeHeader">
        <div id="wrap">
            <!-- HEADER -->
            <%@include file="/WEB-INF/inclu/head.jsp" %>

            <!-- Booking List Section -->

            <section class="booking-section">
                <div class="booking-container">
                    <div class="main-content">
                        <h2 class="booking-header">Đặt Chỗ Của Tôi</h2>
                        <table class="table table-bordered" id="bookingTable">
                            <thead>
                                <tr>
                                    <th>Mã đặt chỗ</th>
                                    <th>Ngày Ði</th>
                                    <th>Ngày hết hạn</th>
                                    <th>Tổng số chỗ</th>
                                    <th>Tổng tiền</th>
                                    <th>Trạng thái</th>
                                    <th>Thanh Toán</th>
                                    <th>Chi tiết</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty bookingList}">
                                    <c:forEach var="booking" items="${bookingList}">
                                        <tr>
                                            <td>${booking.id}</td>
                                            <td>${booking.startDate}</td>
                                            <td>${booking.endDate}</td>
                                            <td>${booking.numberOfPeople}</td>
                                            <td>${booking.price} VNĐ</td>
                                            <td>${booking.status}</td>
                                            <td>${booking.pay}</td>
                                            <td>
                                                <form method="GET" action="${pageContext.request.contextPath}/booking">
                                                    <input type="hidden" name="action" value="viewInvoice">
                                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                                    <button type="submit" class="btn btn-info btn-custom">Chi tiết</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty bookingList}">
                                    <tr>
                                        <td colspan="8" style="text-align: center;">Bạn chưa có booking nào.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/UserServlet" class="btn btn-secondary btn-custom">Quay lại</a>
                        </div>
                        <!-- Phân trang -->
                        <div class="text-center mt-3">
                            <c:if test="${currentPage > 1}">
                                <a href="ViewBookings?action=viewBookings&page=${currentPage - 1}" class="btn btn-primary btn-custom">Trang trước</a>
                            </c:if>
                            <span>Trang ${currentPage} / ${totalPages}</span>
                            <c:if test="${currentPage < totalPages}">
                                <a href="ViewBookings?action=viewBookings&page=${currentPage + 1}" class="btn btn-primary btn-custom">Trang sau</a>
                            </c:if>
                        </div>
                    </div>
                </div>

            </section>
 
            <!-- FOOTER -->
            <%@include file="/WEB-INF/inclu/footer.jsp" %>
        </div>
        <!--        <script>
                    let currentPage = 1;
                    const pageSize = 5; // Số booking trên mỗi trang
        
                    function showPage(page) {
                        let rows = document.querySelectorAll("#bookingTable tbody tr");
                        let totalRows = rows.length;
                        let totalPages = Math.ceil(totalRows / pageSize);
        
                        if (page < 1)
                            page = 1;
                        if (page > totalPages)
                            page = totalPages;
        
                        currentPage = page;
        
                        for (let i = 0; i < totalRows; i++) {
                            rows[i].style.display = (i >= (page - 1) * pageSize && i < page * pageSize) ? "" : "none";
                        }
        
                        document.getElementById("currentPage").innerText = page;
                        document.getElementById("prevPage").disabled = (page === 1);
                        document.getElementById("nextPage").disabled = (page === totalPages);
                    }
        
                    function changePage(offset) {
                        showPage(currentPage + offset);
                    }
        
                    window.onload = function () {
                        showPage(1);
                    };
                </script>-->


    </body>
</html>