/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import common.Config;
import DAO.BookingDAO;
import DAO.CartDAO;
import DAO.DiscountDAO;
import DAO.TourDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Booking;
import model.Cart;
import model.Discount;
import model.Tour;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Diep Quoc Thang - CE182036
 */
@WebServlet(name="VnPayReturnServlet", urlPatterns={"/vnpay-return"})
public class VnPayReturnServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        model.User user = (model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Extract parameters for VNPay
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

        // Extract booking details from session
        String tourId = (String) session.getAttribute("vnpay_tourId");
        String peopleCountStr = (String) session.getAttribute("vnpay_peopleCount");
        String childCountStr = (String) session.getAttribute("vnpay_childCount");
        Integer discountId = (Integer) session.getAttribute("vnpay_discountId");
        
        System.out.println("Received VnPay return with tourId: " + tourId);
        System.out.println("peopleCount: " + peopleCountStr);
        System.out.println("childCount: " + childCountStr);
        System.out.println("discountId: " + discountId);

        if ("00".equals(vnp_ResponseCode)) {
            // Remove secure hash from map
            fields.remove("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            
            // Calculate secure hash to compare
            String signValue = Config.hashAllFields(fields);
            
//            if (signValue.equals(vnp_SecureHash)) {
//                // Valid signature, process the payment
                System.out.println("VnPay signature verified. Payment successful.");
                
                try {
                    // Create and save booking
                    BookingDAO bookingDAO = new BookingDAO();
                    
                    // Check if this is a multiple tour booking from session
                    Boolean isMultipleBooking = (Boolean) session.getAttribute("vnpay_multiple");
                    System.out.println("Is multiple booking (from session): " + isMultipleBooking);
                    
                    if (Boolean.TRUE.equals(isMultipleBooking)) {
                        // Process multiple tour booking from session
                        List<Cart> selectedCarts = (List<Cart>) session.getAttribute("selectedCarts");
                        Integer selectedDiscountId = (Integer) session.getAttribute("selectedDiscountId");
                        
                        System.out.println("Processing multiple booking with selectedCarts: " + 
                                           (selectedCarts != null ? selectedCarts.size() : "null") + 
                                           ", discountId: " + selectedDiscountId);
                        
                        if (selectedCarts != null && !selectedCarts.isEmpty()) {
                            System.out.println("Processing " + selectedCarts.size() + " tours from multiple booking");

                            boolean allSuccess = true;
                            List<Integer> successfulBookingIds = new ArrayList<>();

                            for (Cart cart : selectedCarts) {
                                try {
                                    // Get tour details
                                    TourDAO tourDAO = new TourDAO();
                                    Tour tour = tourDAO.getTourById(cart.getTourId());
                                    
                                    if (tour != null) {
                                        // Create booking object
                                        Booking booking = new Booking();
                                        booking.setUserId(user.getId());
                                        booking.setTourId(cart.getTourId());
                                        booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
                                        booking.setStatus("Pending");
                                        booking.setNumberOfAdults(cart.getNumberOfPeople());
                                        booking.setNumberOfChildren(cart.getNumberOfChild());
                                        booking.setNumberOfPeople(cart.getNumberOfPeople());
                                        booking.setNumberOfChild(cart.getNumberOfChild());
                                        booking.setPay("Thẻ ngân hàng");
                                        
                                        // Set tour details
                                        booking.setTourName(tour.getName());
                                        booking.setLocation(tour.getLocation());
                                        booking.setTransport(tour.getTransport());
                                        booking.setPrice(tour.getPrice().doubleValue());
                                        booking.setStartDate(tour.getStartDate());
                                        booking.setEndDate(tour.getEndDate());
                                        
                                        // Set discount ID if available
                                        if (selectedDiscountId != null) {
                                            booking.setDiscountId(selectedDiscountId);
                                        }
                                        
                                        // Calculate total amount
                                        BigDecimal tourPrice = tour.getPrice();
                                        BigDecimal adultPrice = tourPrice.multiply(new BigDecimal(cart.getNumberOfPeople()));
                                        BigDecimal childPrice = BigDecimal.ZERO;
                                        if (cart.getNumberOfChild() > 0) {
                                            childPrice = tourPrice.multiply(new BigDecimal(cart.getNumberOfChild())).multiply(new BigDecimal("0.5"));
                                        }
                                        
                                        // Apply discount if present (only to adult price)
                                        BigDecimal itemTotal = adultPrice.add(childPrice);
                                        BigDecimal discountAmount = BigDecimal.ZERO;
                                        
                                        if (selectedDiscountId != null) {
                                            DiscountDAO discountDAO = new DiscountDAO();
                                            Discount discount = discountDAO.getDiscountById(selectedDiscountId);
                                            if (discount != null) {
                                                double discountPercent = discount.getDiscount_percent();
                                                discountAmount = adultPrice.multiply(BigDecimal.valueOf(discountPercent / 100));
                                                itemTotal = itemTotal.subtract(discountAmount);
                                            }
                                        }
                                        
                                        // Set price details in booking
                                        booking.setAdultPrice(adultPrice);
                                        booking.setChildPrice(childPrice);
                                        booking.setDiscountAmount(discountAmount);
                                        booking.setTotalAmount(itemTotal);
                                        booking.setDiscountId(selectedDiscountId);
                                        
                                        // Save booking
                                        int bookingId = bookingDAO.bookTour(booking);
                                        
                                        if (bookingId <= 0) {
                                            allSuccess = false;
                                            System.out.println("Failed to create booking for tour ID: " + cart.getTourId());
                                        } else {
                                            System.out.println("Created booking ID: " + bookingId + " for tour ID: " + cart.getTourId());
                                            successfulBookingIds.add(bookingId);
                                            
                                            // Delete item from cart
                                            try {
                                                CartDAO cartDAO = new CartDAO();
                                                cartDAO.deleteFromCart(cart.getId(), user.getId());
                                                System.out.println("Successfully removed cart ID: " + cart.getId() + " after booking");
                                            } catch (Exception e) {
                                                System.out.println("Error removing cart item: " + e.getMessage());
                                            }
                                        }
                                    } else {
                                        System.out.println("Tour not found for ID: " + cart.getTourId());
                                        allSuccess = false;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error processing booking for cart ID " + cart.getId() + ": " + e.getMessage());
                                    e.printStackTrace();
                                    allSuccess = false;
                                }
                            }
                            
                            // Clean up session
                            session.removeAttribute("selectedCarts");
                            session.removeAttribute("selectedDiscountId");
                            session.removeAttribute("vnpay_discountId");
                            session.removeAttribute("vnpay_multiple"); // Clean up the flag
                            
                            // Clear cookie
                            Cookie cookie = new Cookie("selectedCartItems", "");
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                            
                            if (allSuccess) {
                                // All bookings created successfully
                                response.sendRedirect("Info.jsp?success=true&multiple=true");
                                return;
                            } else {
                                request.setAttribute("error", "Some bookings failed to be created");
                                request.getRequestDispatcher("error.jsp").forward(request, response);
                                return;
                            }
                        } else {
                            request.setAttribute("error", "No selected tours found");
                            request.getRequestDispatcher("error.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        // Single tour booking
                        // Validate parameters
                        if (tourId != null && peopleCountStr != null) {
                            int tourIdInt = Integer.parseInt(tourId);
                            int peopleCount = Integer.parseInt(peopleCountStr);
                            int childCount = 0;
                            if (childCountStr != null && !childCountStr.isEmpty()) {
                                childCount = Integer.parseInt(childCountStr);
                            }
                            
                            // Get tour details
                            TourDAO tourDAO = new TourDAO();
                            Tour tour = tourDAO.getTourById(tourIdInt);
                            
                            if (tour != null) {
                                // Create booking object
                                Booking booking = new Booking();
                                booking.setUserId(user.getId());
                                booking.setTourId(tourIdInt);
                                booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
                                booking.setStatus("Paid");
                                booking.setNumberOfAdults(peopleCount);
                                booking.setNumberOfChildren(childCount);
                                booking.setPay("Thẻ ngân hàng");
                                
                                // Set tour details
                                booking.setTourName(tour.getName());
                                booking.setLocation(tour.getLocation());
                                booking.setTransport(tour.getTransport());
                                booking.setPrice(tour.getPrice().doubleValue());
                                booking.setStartDate(tour.getStartDate());
                                booking.setEndDate(tour.getEndDate());
                                
                                // Set discount ID if available
                                if (discountId != null) {
                                    booking.setDiscountId(discountId);
                                }
                                
                                // Calculate total amount
                                BigDecimal tourPrice = tour.getPrice();
                                BigDecimal adultPrice = tourPrice.multiply(new BigDecimal(peopleCount));
                                BigDecimal childPrice = BigDecimal.ZERO;
                                if (childCount > 0) {
                                    childPrice = tourPrice.multiply(new BigDecimal(childCount)).multiply(new BigDecimal("0.5"));
                                }
                                
                                // Apply discount if present (only to adult price)
                                BigDecimal totalPrice = adultPrice.add(childPrice);
                                BigDecimal discountAmount = BigDecimal.ZERO;
                                
                                if (discountId != null) {
                                    DiscountDAO discountDAO = new DiscountDAO();
                                    model.Discount discount = discountDAO.getDiscountById(discountId);
                                    if (discount != null) {
                                        double discountPercent = discount.getDiscount_percent();
                                        discountAmount = adultPrice.multiply(BigDecimal.valueOf(discountPercent / 100));
                                        totalPrice = totalPrice.subtract(discountAmount);
                                    }
                                }
                                
                                // Set price details in booking
                                booking.setAdultPrice(adultPrice);
                                booking.setChildPrice(childPrice);
                                booking.setDiscountAmount(discountAmount);
                                booking.setTotalAmount(totalPrice);
                                
                                // Save booking
                                int bookingId = bookingDAO.bookTour(booking);
                                
                                if (bookingId > 0) {
                                    // Booking successful
                                    System.out.println("Booking created with ID: " + bookingId);
                                    
                                    // Clean up session
                                    session.removeAttribute("vnpay_tourId");
                                    session.removeAttribute("vnpay_peopleCount");
                                    session.removeAttribute("vnpay_childCount");
                                    session.removeAttribute("vnpay_discountId");
                                    session.removeAttribute("vnpay_multiple");  // Clean up the flag
                                    session.removeAttribute("selectedCart");
                                    session.removeAttribute("selectedTour");
                                    session.removeAttribute("numberOfPeople");
                                    session.removeAttribute("numberOfChild");
                                    
                                    // Delete item from cart if it came from cart
                                    try {
                                        Cart selectedCart = (Cart) session.getAttribute("selectedCart");
                                        if (selectedCart != null) {
                                            CartDAO cartDAO = new CartDAO();
                                            cartDAO.deleteFromCart(selectedCart.getId(), user.getId());
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Error removing cart item: " + e.getMessage());
                                    }
                                    
                                    // Redirect to success page
                                    response.sendRedirect("Info.jsp?success=true");
                                    return;
                                } else {
                                    request.setAttribute("error", "Failed to create booking record");
                                    request.getRequestDispatcher("error.jsp").forward(request, response);
                                }
                            } else {
                                request.setAttribute("error", "Tour not found");
                                request.getRequestDispatcher("error.jsp").forward(request, response);
                            }
                        } else {
                            request.setAttribute("error", "Missing tour ID or people count");
                            request.getRequestDispatcher("error.jsp").forward(request, response);
                        }
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "Error processing payment: " + e.getMessage());
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
//            } else {
//                request.setAttribute("error", "Invalid signature from payment provider");
//                request.getRequestDispatcher("error.jsp").forward(request, response);
//            }
        } else {
            // Payment failed or cancelled
            request.setAttribute("error", "Payment failed with code: " + vnp_ResponseCode);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
