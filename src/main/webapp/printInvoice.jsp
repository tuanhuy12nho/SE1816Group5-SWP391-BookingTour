<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Booking, DAO.BookingDAO" %>
<%
    String bookingIdStr = request.getParameter("bookingId");
    Booking booking = null;
    if (bookingIdStr != null) {
        int bookingId = Integer.parseInt(bookingIdStr);
        BookingDAO bookingDAO = new BookingDAO();
        booking = bookingDAO.getBookingById(bookingId);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Print Tour Booking Invoice</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                color: #333;
            }
            .invoice-box {
                max-width: 800px;
                margin: auto;
                padding: 20px;
                border: 1px solid #ddd;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }
            .invoice-box h1 {
                text-align: center;
                color: #003087;
                margin-bottom: 20px;
            }
            .invoice-box table {
                width: 100%;
                border-collapse: collapse;
            }
            .invoice-box table td {
                padding: 10px;
                border-bottom: 1px solid #eee;
            }
            .invoice-box table td:first-child {
                font-weight: bold;
                width: 40%;
                color: #003087;
            }
            .invoice-box .footer {
                text-align: center;
                margin-top: 20px;
                font-size: 12px;
                color: #777;
            }
            .btn-print {
                display: block;
                width: 100px;
                margin: 20px auto;
                padding: 10px;
                background-color: #28a745;
                color: #fff;
                text-align: center;
                text-decoration: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn-print:hover {
                background-color: #218838;
            }
            .logo {
                margin-left: 0%;

            }
            .logo img {
                height: 55px;
            }
            @media print {
                .btn-print {
                    display: none;
                }
            }
        </style>
    </head>
    <body>
        <!-- Container for the invoice layout -->
        <div class="invoice-box">
            <!-- Logo section with a link to the homepage -->
            <div class="logo">
                <a href="#"><img src="<%= request.getContextPath()%>/images/Logo_G3.png" alt="SaigonTourist Logo"></a>
            </div>

            <!-- Heading for the invoice -->
            <h1>Tour Booking Invoice</h1>

            <!-- Check if the booking object exists -->
            <% if (booking != null) {%>

            <!-- Table to display the booking details -->
            <table>
                <!-- Display the booking ID -->
                <tr><td>ID:</td><td><%= booking.getId()%></td></tr>
                <!-- Display the full name of the person who booked -->
                <tr><td>Full Name:</td><td><%= booking.getFullName()%></td></tr>
                <!-- Display the email of the person who booked -->
                <tr><td>Email:</td><td><%= booking.getEmail()%></td></tr>
                <!-- Display the phone number of the person who booked -->
                <tr><td>Phone Number:</td><td><%= booking.getPhone()%></td></tr>
                <!-- Display the name of the booked tour -->
                <tr><td>Tour Name:</td><td><%= booking.getTourName()%></td></tr>
                <!-- Display the location of the tour -->
                <tr><td>Location:</td><td><%= booking.getLocation()%></td></tr>
                <!-- Display the mode of transport for the tour -->
                <tr><td>Transport:</td><td><%= booking.getTransport()%></td></tr>
                <!-- Display the departure date of the tour -->
                <tr><td>Departure Date:</td><td><%= booking.getStartDate()%></td></tr>
                <!-- Display the end date of the tour -->
                <tr><td>End Date:</td><td><%= booking.getEndDate()%></td></tr>
                <!-- Display the status of the booking -->
                <tr><td>Status:</td><td><%= booking.getStatus()%></td></tr>
                <!-- Display the number of people in the booking -->
                <tr><td>Number of People:</td><td><%= booking.getNumberOfPeople()%></td></tr>
                <!-- Display the total price of the booking -->
                <tr><td>Total Price:</td><td><%= booking.getPrice()%> VNĐ</td></tr>
            </table>

            <!-- Footer section with a thank you message -->
            <div class="footer">
                <p>Thank you for choosing our tour service!</p>
            </div>

            <!-- Button to print the invoice -->
            <button class="btn-print" onclick="window.print()">Print</button>
            <!-- Link to return to the home page -->
            <a href="<%= request.getContextPath()%>/SortTour" class="btn-github">Back to Home</a>

            <!-- If no booking is found, display an error message -->
            <% } else { %>
            <p style="text-align: center; color: red;">No booking found!</p>
            <% }%>
        </div>
    </body>
</html>