<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Booking, model.Discount, DAO.DiscountDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% 
    Booking booking = (Booking) request.getAttribute("booking");
    Float discountPercent = (Float) request.getAttribute("discountPercent");
    Double originalTotal = (Double) request.getAttribute("originalTotal");
    Double discountedTotal = (Double) request.getAttribute("discountedTotal");
    
    Discount discount = null;
    // Only fetch discount details if we don't already have the percentage
    if (booking.getDiscountId() > 0 && discountPercent == null) {
        try {
            DiscountDAO discountDAO = new DiscountDAO();
            discount = discountDAO.getDiscountById(booking.getDiscountId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tour Booking Invoice</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f6f9;
                color: #333;
            }
            .container {
                max-width: 800px;
                margin: 50px auto;
                padding: 20px;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            }
            h1 {
                text-align: center;
                color: #003087;
                font-weight: bold;
                margin-bottom: 30px;
            }
            .invoice-container {
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background: #fafafa;
            }
            .info-item {
                padding: 10px;
                border-bottom: 1px solid #eee;
                display: flex;
                justify-content: space-between;
                transition: background-color 0.3s ease;
            }
            .info-item:last-child {
                border-bottom: none;
            }
            .info-item strong {
                color: #003087;
                font-weight: 600;
            }
            .info-item span {
                color: #555;
            }
            .discount-info {
                background-color: #e7f4e4;
                border-radius: 5px;
                padding: 10px;
                margin: 15px 0;
            }
            .original-price {
                text-decoration: line-through;
                color: #999;
                font-size: 0.9em;
            }
            .discounted-price {
                color: #28a745;
                font-weight: bold;
                font-size: 1.1em;
            }
            .text-center {
                margin-top: 20px;
            }
            .btn-custom {
                padding: 10px 20px;
                border-radius: 5px;
                font-weight: bold;
                transition: transform 0.2s ease, background-color 0.3s ease;
            }
            .btn-secondary {
                background-color: #6c757d;
                color: #fff;
            }
            .btn-secondary:hover {
                background-color: #5a6268;
                color: #fff;
            }
            .btn-danger {
                background-color: #dc3545;
                color: #fff;
            }
            .btn-danger:hover {
                background-color: #c82333;
                color: #fff;
            }
            .btn-primary {
                background-color: #28a745;
                color: #fff;
            }
            .btn-primary:hover {
                background-color: #218838;
                color: #fff;
            }
            .footer-text {
                text-align: center;
                margin-top: 20px;
                color: #777;
                font-size: 14px;
            }
            .d-inline {
                display: inline-block;
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <!-- Include the header JSP file to display the common header section -->
        <%@include file="/WEB-INF/inclu/head.jsp" %>

        <!-- Main content section for the tour booking invoice -->
        <main class="container">
            <!-- Page heading for the tour booking invoice -->
            <h1 class="my-4">Tour Booking Invoice</h1>

            <!-- Container for displaying invoice details -->
            <div class="invoice-container">
                <!-- Subheading for invoice details -->
                <h2>Invoice Details</h2>
                <!-- Display the booking ID -->
                <div class="info-item"><strong>ID:</strong> <span>${booking.id}</span></div>
                <!-- Display the full name of the person who booked -->
                <div class="info-item"><strong>Full Name:</strong> <span>${booking.fullName}</span></div>
                <!-- Display the email of the person who booked -->
                <div class="info-item"><strong>Email:</strong> <span>${booking.email}</span></div>
                <!-- Display the phone number of the person who booked -->
                <div class="info-item"><strong>Phone Number:</strong> <span>${booking.phone}</span></div>
                <!-- Display the name of the booked tour -->
                <div class="info-item"><strong>Tour Name:</strong> <span>${booking.tourName}</span></div>
                <!-- Display the location of the tour -->
                <div class="info-item"><strong>Location:</strong> <span>${booking.location}</span></div>
                <!-- Display the mode of transport for the tour -->
                <div class="info-item"><strong>Transport:</strong> <span>${booking.transport}</span></div>
                <!-- Display the departure date of the tour -->
                <div class="info-item"><strong>Departure Date:</strong> <span>${booking.startDate}</span></div>
                <!-- Display the end date of the tour -->
                <div class="info-item"><strong>End Date:</strong> <span>${booking.endDate}</span></div>
                <!-- Display the status of the booking -->
                <div class="info-item"><strong>Status:</strong> <span>${booking.status}</span></div>
                <!-- Display the number of people in the booking -->
                <div class="info-item"><strong>Number of People:</strong> <span>${booking.numberOfPeople}</span></div>
                <!-- Display the price per person -->
                <div class="info-item"><strong>Price per Person:</strong> <span><fmt:formatNumber value="${booking.price}" type="number"/> VNĐ</span></div>
                
                <!-- Display discount information if available -->
                <c:choose>
                    <c:when test="${discountPercent != null}">
                        <div class="discount-info">
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Discount Applied:</strong>
                                <span>${discountPercent}% off</span>
                            </div>
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Original Total:</strong>
                                <span class="original-price"><fmt:formatNumber value="${originalTotal}" type="number"/> VNĐ</span>
                            </div>
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Discount Amount:</strong>
                                <span><fmt:formatNumber value="${originalTotal - discountedTotal}" type="number"/> VNĐ</span>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${booking.discountId > 0 && discount != null}">
                        <div class="discount-info">
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Discount Applied:</strong>
                                <span><%= discount.getCode() %> (<%= discount.getDiscount_percent() %>% off)</span>
                            </div>
                            <% 
                                double origTotal = booking.getPrice() * booking.getNumberOfPeople();
                                double discAmount = origTotal * (discount.getDiscount_percent() / 100);
                                double finalTotal = origTotal - discAmount;
                            %>
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Original Total:</strong>
                                <span class="original-price"><fmt:formatNumber value="<%= origTotal %>" type="number"/> VNĐ</span>
                            </div>
                            <div class="info-item" style="background: none; border: none;">
                                <strong>Discount Amount:</strong>
                                <span><fmt:formatNumber value="<%= discAmount %>" type="number"/> VNĐ</span>
                            </div>
                        </div>
                    </c:when>
                </c:choose>
                
                <!-- Display the total price -->
                <div class="info-item" style="font-weight: bold; font-size: 1.1em;">
                    <strong>Total Amount:</strong> 
                    <c:choose>
                        <c:when test="${discountedTotal != null}">
                            <span class="discounted-price"><fmt:formatNumber value="${discountedTotal}" type="number"/> VNĐ</span>
                        </c:when>
                        <c:when test="${booking.discountId > 0 && discount != null}">
                            <% double finalTotal = booking.getPrice() * booking.getNumberOfPeople() * (1 - (discount.getDiscount_percent() / 100)); %>
                            <span class="discounted-price"><fmt:formatNumber value="<%= finalTotal %>" type="number"/> VNĐ</span>
                        </c:when>
                        <c:otherwise>
                            <span><fmt:formatNumber value="${booking.price * booking.numberOfPeople}" type="number"/> VNĐ</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Display payment method -->
                <div class="info-item"><strong>Payment Method:</strong> <span>${booking.pay}</span></div>
            </div>

            <!-- Buttons for navigation and actions -->
            <div class="text-center mt-4">
                <!-- Link to return to the home page -->
                <a href="<%= request.getContextPath()%>/SortTour" class="btn btn-custom btn-secondary">Back to Home</a>
                <!-- Form to cancel the tour booking -->
                <form id="cancelForm" action="<%= request.getContextPath()%>/ViewBookings" method="post" class="d-inline">
                    <!-- Hidden input to specify the action as 'cancel' -->
                    <input type="hidden" name="action" value="cancel">
                    <!-- Hidden input to pass the booking ID -->
                    <input type="hidden" name="bookingId" value="${booking.id}">
                    <!-- Button to submit the cancellation request -->
                    <c:choose>
                        <c:when test="${booking.status eq 'Confirmed' || booking.status eq 'Cancelled'}">
                            <button type="button" class="btn btn-custom btn-secondary" disabled>
                                <c:choose>
                                    <c:when test="${booking.status eq 'Confirmed'}">Đã xác nhận</c:when>
                                    <c:when test="${booking.status eq 'Cancelled'}">Đã hủy</c:when>
                                </c:choose>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" onclick="confirmCancel(${booking.id})" class="btn btn-custom btn-danger">Cancel Tour</button>
                        </c:otherwise>
                    </c:choose>
                </form>
                <!-- Link to print the invoice -->
                <a href="<%= request.getContextPath()%>/printInvoice.jsp?bookingId=${booking.id}" 
                   class="btn btn-custom btn-primary d-inline">In hóa đơn</a>
            </div>

            <!-- Footer text for the invoice page -->
            <div class="footer-text">
                <p>Thank you for choosing our tour service! Contact us for any assistance.</p>
            </div>
        </main>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
            const infoItems = document.querySelectorAll('.info-item');
            infoItems.forEach(item => {
                item.addEventListener('mouseover', () => {
                    item.style.backgroundColor = '#f1f3f5';
                });
                item.addEventListener('mouseout', () => {
                    item.style.backgroundColor = '';
                });
            });

            function confirmCancel(bookingId) {
                if (confirm('Bạn có chắc chắn muốn hủy tour này?')) {
                    document.getElementById('cancelForm').submit();
                }
            }
        </script>
        <%@include file="/WEB-INF/inclu/footer.jsp" %>
    </body>
</html>