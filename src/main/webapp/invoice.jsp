<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Booking" %>
<% Booking booking = (Booking) request.getAttribute("booking");%>
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
            </div>

            <!-- Buttons for navigation and actions -->
            <div class="text-center mt-4">
                <!-- Link to return to the home page -->
                <a href="<%= request.getContextPath()%>/SortTour" class="btn btn-custom btn-secondary">Back to Home</a>
                <!-- Form to cancel the tour booking -->
                <form action="<%= request.getContextPath()%>/ViewBookings" method="post" class="d-inline">
                    <!-- Hidden input to specify the action as 'cancel' -->
                    <input type="hidden" name="action" value="cancel">
                    <!-- Hidden input to pass the booking ID -->
                    <input type="hidden" name="bookingId" value="${booking.id}">
                    <!-- Button to submit the cancellation request -->
                    <button type="submit" class="btn btn-custom btn-danger">Cancel Tour</button>
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

        </script>
        <%@include file="/WEB-INF/inclu/footer.jsp" %>
    </body>
</html>