/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.BookingDAO;
import DAO.FavoriteDAO;
import DAO.ItineraryDAO;
import DAO.ReviewDAO;
import DAO.DiscountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.Booking;
import model.Itinerary;
import model.Review;
import model.Tour;
import model.User;
import model.Discount;
import java.math.BigDecimal;
import model.Cart;
import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Admin
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the current session to manage user data
        HttpSession session = request.getSession();

        // Get the user object from the session to check if the user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        // Set userId to the user's ID if logged in, otherwise default to -1
        int userId = (user != null) ? user.getId() : -1;

        // Retrieve the "action" parameter from the request to determine the operation
        String action = request.getParameter("action");

        // Handle the "viewTour" action to display tour details
        if ("viewTour".equals(action)) {
            // Get the tour ID from the request parameters
            String tourIdParam = request.getParameter("id");

            // Validate that the tour ID is provided and is a valid integer
            if (tourIdParam != null && tourIdParam.matches("\\d+")) {
                int tourId = Integer.parseInt(tourIdParam);
                //Hien

                if (user != null) {
                    FavoriteDAO favDao = new FavoriteDAO();

                    boolean favorite = favDao.haveLikeInMyFavorites(user.getId(), tourId);

                    request.setAttribute("favorite", favorite + "");
                } else {
                    request.setAttribute("favorite", "none");
                }

                //Hien
                // Create an instance of BookingDAO to interact with the database
                BookingDAO bookingDAO = new BookingDAO();

                // Fetch the tour details from the database using the tour ID
                Tour tour = bookingDAO.getTourById(tourId);
                ItineraryDAO itineraryDAO = new ItineraryDAO();
                List<Itinerary> itineraries = itineraryDAO.getItinerariesByTourId(tourId);
                request.setAttribute("itineraries", itineraries);

                // Create an instance of ReviewDAO to fetch reviews
                ReviewDAO reviewDAO = new ReviewDAO();

                // Fetch the list of reviews associated with the tour
                List<Review> reviews = reviewDAO.getReviewsByTourId(tourId);

                // Check if the tour exists; if not, set an error message
                if (tour == null) {
                    request.setAttribute("error", "Tour does not exist!");
                } else {
                    // Set the tour and reviews as request attributes to pass to the JSP
                    request.setAttribute("tour", tour);
                    request.setAttribute("reviews", reviews);
                }
            } else {
                // Set an error message if the tour ID is invalid or missing
                request.setAttribute("error", "Invalid ID!");
            }
            // Forward the request to tourDetail.jsp to display the results
            request.getRequestDispatcher("tourDetail.jsp").forward(request, response);
        } // Handle the "inputTour" action to prepare the booking form
        else if ("inputTour".equals(action)) {
            // Check if the user is logged in; redirect to login page if not
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }
            
            // Debug information
            System.out.println("========== DEBUG INFO ==========");
            System.out.println("All request parameters:");
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                String value = request.getParameter(name);
                System.out.println(name + " = " + value);
            }
            System.out.println("===============================");
            
            // Check if this is a multiple tour booking from the cart
            if ("true".equals(request.getParameter("multiple"))) {
                System.out.println("Processing multiple tour booking");
                
                // Get cart items from cookie
                String cookieData = getCookieValue(request, "selectedCartItems");
                if (cookieData != null && !cookieData.isEmpty()) {
                    try {
                        Map<String, Object> parsedData = parseCartIdsFromCookie(cookieData);
                        List<Integer> cartIds = (List<Integer>) parsedData.get("cartIds");
                        String discountIdStr = (String) parsedData.get("discountId");

                        if (!cartIds.isEmpty()) {
                            System.out.println("Found " + cartIds.size() + " cart IDs in cookie: " + cartIds);
                            
                            // Save discount ID to session if available
                            if (discountIdStr != null && !discountIdStr.isEmpty() && !"null".equals(discountIdStr)) {
                                try {
                                    int discountId = Integer.parseInt(discountIdStr);
                                    if (discountId > 0) {
                                        session.setAttribute("selectedDiscountId", discountId);
                                        System.out.println("Saved discountId to session: " + discountId);
                                        
                                        // Get discount information
                                        DAO.DiscountDAO discountDAO = new DAO.DiscountDAO();
                                        Discount selectedDiscount = discountDAO.getDiscountById(discountId);
                                        if (selectedDiscount != null) {
                                            session.setAttribute("selectedDiscount", selectedDiscount);
                                            System.out.println("Saved discount info to session: " + selectedDiscount.getCode());
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid discount ID: " + discountIdStr);
                                }
                            }
                            
                            // Get the carts using our new method
                            DAO.CartDAO cartDAO = new DAO.CartDAO();
                            List<Cart> selectedCarts = cartDAO.getCartsByIds(cartIds, user.getId());
                            
                            if (!selectedCarts.isEmpty()) {
                                System.out.println("Successfully retrieved " + selectedCarts.size() + " carts");
                                
                                // Create BookingDAO instance
                                BookingDAO bookingDAO = new BookingDAO();
                                
                                // Save to session for use in Booking.jsp
                                session.setAttribute("selectedCarts", selectedCarts);
                                
                                // Use the first cart's tour as the main tour
                                Cart firstCart = selectedCarts.get(0);
                                Tour tour = bookingDAO.getTourById(firstCart.getTourId());
                                
                                if (tour != null) {
                                    request.setAttribute("tour", tour);
                                    request.setAttribute("numberOfPeople", firstCart.getNumberOfPeople());
                                    request.setAttribute("numberOfChild", firstCart.getNumberOfChild());
                                    
                                    // Load all tour information
                                    for (Cart cartItem : selectedCarts) {
                                        Tour cartTour = bookingDAO.getTourById(cartItem.getTourId());
                                        if (cartTour != null) {
                                            request.setAttribute("tour" + cartItem.getTourId(), cartTour);
                                            System.out.println("Added tour: " + cartTour.getName() + " with ID " + cartItem.getTourId());
                                        }
                                    }
                                    
                                    request.setAttribute("isMultipleBooking", true);
                                    
                                    // Forward to Booking.jsp
                                    request.getRequestDispatcher("Booking.jsp").forward(request, response);
                                    return;
                                } else {
                                    System.out.println("Main tour not found for ID: " + firstCart.getTourId());
                                }
                            } else {
                                System.out.println("No carts found for the IDs in the cookie");
                            }
                        } else {
                            System.out.println("No cart IDs found in cookie");
                        }
                    } catch (Exception e) {
                        System.out.println("Error processing multiple tours: " + e.getMessage());
                        e.printStackTrace();
                    }
                    
                    // If we get here, something went wrong with processing multiple tours
                    request.setAttribute("error", "Không thể tìm thấy thông tin tour đã chọn.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                } else {
                    System.out.println("No cookie data found for multiple tour booking");
                    request.setAttribute("error", "Không tìm thấy thông tin giỏ hàng đã chọn.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            }

            // For single tour booking, get the tour ID from request parameters
            String tourIdParam = request.getParameter("id");
            System.out.println("Received tour ID from form: " + tourIdParam);

            // Validate that the tour ID is provided and is a valid integer
            if (tourIdParam != null && tourIdParam.matches("\\d+")) {
                int tourId = Integer.parseInt(tourIdParam);

                try {
                    // Create an instance of BookingDAO to interact with the database
                    BookingDAO bookingDAO = new BookingDAO();

                    // Fetch the tour details from the database using the tour ID
                    Tour tour = bookingDAO.getTourById(tourId);
                    
                    // Check if the tour exists; if not, set an error message
                    if (tour == null) {
                        System.out.println("Tour not found for ID: " + tourId);
                        request.setAttribute("error", "Tour không tồn tại hoặc đã bị xóa!");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                        return;
                    } else {
                        System.out.println("Found tour: " + tour.getName());
                    }
                    
                    // Set the tour as a request attribute to pass to the JSP
                    request.setAttribute("tour", tour);
                    
                    // Check for multiple tours in session (from checkout process)
                    @SuppressWarnings("unchecked")
                    List<Cart> selectedCarts = (List<Cart>) session.getAttribute("selectedCarts");
                    if (selectedCarts != null && !selectedCarts.isEmpty()) {
                        System.out.println("Found " + selectedCarts.size() + " selected carts in session");
                        request.setAttribute("isMultipleBooking", true);
                        
                        // Store all tour information as attributes
                        for (Cart cartItem : selectedCarts) {
                            try {
                                int cartTourId = cartItem.getTourId();
                                Tour cartTour = bookingDAO.getTourById(cartTourId);
                                if (cartTour != null) {
                                    request.setAttribute("tour" + cartTourId, cartTour);
                                    System.out.println("Added tour: " + cartTour.getName() + " with ID " + cartTourId);
                                }
                            } catch (Exception e) {
                                System.out.println("Error loading tour for cart item: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("No selected carts in session, checking for individual cart");
                    }

                    // Check for cart data in session if we're coming from cart.jsp
                    Cart selectedCart = (Cart) session.getAttribute("selectedCart");
                    
                    // If no cart in session, check if we have a cart ID from the browser session storage
                    if (selectedCart == null) {
                        // Get cart data from CartDAO using tour ID and user ID
                        try {
                            DAO.CartDAO cartDAO = new DAO.CartDAO();
                            List<Cart> userCarts = cartDAO.getCartByUserId(user.getId());
                            
                            // Find cart with matching tour ID
                            for (Cart cart : userCarts) {
                                if (cart.getTourId() == tourId) {
                                    selectedCart = cart;
                                    session.setAttribute("selectedCart", selectedCart);
                                    session.setAttribute("selectedTour", tour);
                                    session.setAttribute("numberOfPeople", cart.getNumberOfPeople());
                                    session.setAttribute("numberOfChild", cart.getNumberOfChild());
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error retrieving cart: " + e.getMessage());
                        }
                    }

                    // If we found cart information, add it to request attributes
                    if (selectedCart != null) {
                        request.setAttribute("numberOfPeople", selectedCart.getNumberOfPeople());
                        request.setAttribute("numberOfChild", selectedCart.getNumberOfChild());
                    }
                } catch (Exception e) {
                    // Handle any exceptions by setting an error message
                    request.setAttribute("error", "An error occurred: " + e.getMessage());
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            } else {
                // Set an error message if the tour ID is invalid or missing
                request.setAttribute("error", "Invalid ID!");
            }
            // Forward the request to Booking.jsp to display the booking form
            request.getRequestDispatcher("Booking.jsp").forward(request, response);
        } // Handle the "viewInvoice" action to display a booking invoice
        else if ("viewInvoice".equals(action)) {
            // Check if the user is logged in; redirect to login page if not
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            // Get the booking ID from the request parameters
            String bookingIdParam = request.getParameter("bookingId");

            // Validate that the booking ID is provided and is a valid integer
            if (bookingIdParam == null || !bookingIdParam.matches("\\d+")) {
                request.setAttribute("error", "Invalid booking ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = null;

            try {
                // Create an instance of BookingDAO to interact with the database
                BookingDAO bookingDAO = new BookingDAO();

                // Fetch the booking details from the database using the booking ID
                booking = bookingDAO.getBookingById(bookingId);

                // Check if the booking exists; if not, set an error message
                if (booking == null) {
                    request.setAttribute("error", "No invoice found with ID: " + bookingId);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                
                // Calculate the discounted total if a discount is applied
                if (booking.getDiscountId() > 0) {
                    // Get the discount percentage and calculate discounted total
                    float discountPercent = bookingDAO.getDiscountPercentForBooking(bookingId);
                    double discountedTotal = bookingDAO.calculateDiscountedTotal(booking);
                    
                    // Add these values as request attributes
                    request.setAttribute("discountPercent", discountPercent);
                    request.setAttribute("originalTotal", booking.getPrice() * booking.getNumberOfPeople());
                    request.setAttribute("discountedTotal", discountedTotal);
                }

                // Set the booking as a request attribute to pass to the JSP
                request.setAttribute("booking", booking);
                // Forward the request to invoice.jsp to display the invoice
                request.getRequestDispatcher("invoice.jsp").forward(request, response);

            } catch (SQLException e) {
                // Handle database errors by setting an error message
                request.setAttribute("error", "Data query error: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } // Handle VNPay successful payment redirect
        else if ("processVnPaySuccess".equals(action)) {
            // Check if user is logged in
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }
            
            // Get the tour ID from the request
            String tourIdParam = request.getParameter("tourId");
            if (tourIdParam == null || !tourIdParam.matches("\\d+")) {
                request.setAttribute("error", "Invalid tour ID!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            int tourId = Integer.parseInt(tourIdParam);
            
            // Create BookingDAO instance
            BookingDAO bookingDAO = new BookingDAO();
            
            // Get the tour details
            Tour tour = bookingDAO.getTourById(tourId);
            if (tour == null) {
                request.setAttribute("error", "Tour not found!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Set tour in request
            request.setAttribute("tour", tour);
            
            // Set VNPay payment flag
            request.setAttribute("vnpayPayment", true);
            
            // Forward to Info.jsp
            request.getRequestDispatcher("Info.jsp").forward(request, response);
        }
        else {
            // Redirect to the SortTour servlet if no valid action is specified
            response.sendRedirect("/SortTour");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Retrieve the "action" parameter from the request to determine the operation
        String action = request.getParameter("action");

        // Create instances of BookingDAO and ReviewDAO to interact with the database
        BookingDAO bookingDAO = new BookingDAO();
        ReviewDAO reviewDAO = new ReviewDAO();

        // Retrieve the current session to manage user data
        HttpSession session = request.getSession();

        // Get the user object from the session to check if the user is logged in
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in; redirect to login page if not
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Get the user ID from the user object
        int userId = user.getId();

        // Get payment method from request parameters
        String pay = request.getParameter("pay");
        if (pay == null || pay.isEmpty()) {
            // Default to cash payment if not specified
            pay = "Tiền mặt";
        }

        // Handle different actions based on the "action" parameter
        switch (action) {
            case "save":
                // Handle the booking process; ensure the user is logged in
                if (user == null) {
                    request.setAttribute("error", "You need to log in to book a tour.");
                    request.getRequestDispatcher("Login.jsp").forward(request, response);
                    return;
                }

                // Check if this is a multiple booking
                boolean isMultipleBooking = false;
                List<Cart> selectedCarts = (List<Cart>) session.getAttribute("selectedCarts");
                Integer selectedDiscountId = (Integer) session.getAttribute("selectedDiscountId");

                if (selectedCarts != null && !selectedCarts.isEmpty()) {
                    isMultipleBooking = true;
                    System.out.println("Processing multiple bookings (" + selectedCarts.size() + " items)");
                }

                if (isMultipleBooking) {
                    // Handle multiple bookings
                    boolean allSuccess = true;
                    List<Integer> successfulBookingIds = new ArrayList<>();
                    DAO.CartDAO cartDAO = new DAO.CartDAO();
                    
                    for (Cart cart : selectedCarts) {
                        try {
                            // Get the tour details
                            Tour cartTour = bookingDAO.getTourById(cart.getTourId());
                            if (cartTour == null) {
                                System.out.println("Tour not found for ID: " + cart.getTourId());
                                continue;
                            }
                            
                            // Create booking object
                            Booking booking = new Booking();
                            booking.setUserId(userId);
                            booking.setTourId(cart.getTourId());
                            booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
                            booking.setStatus("Pending");
                            booking.setNumberOfAdults(cart.getNumberOfPeople());
                            booking.setNumberOfChildren(cart.getNumberOfChild());
                            booking.setNumberOfPeople(cart.getNumberOfPeople());  // For backward compatibility
                            booking.setNumberOfChild(cart.getNumberOfChild());    // For backward compatibility
                            booking.setPay(pay);
                            
                            // Set tour details
                            booking.setTourName(cartTour.getName());
                            booking.setLocation(cartTour.getLocation());
                            booking.setTransport(cartTour.getTransport());
                            booking.setPrice(cartTour.getPrice().doubleValue());
                            booking.setStartDate(cartTour.getStartDate());
                            booking.setEndDate(cartTour.getEndDate());
                            
                            // Set discount ID if available
                            if (selectedDiscountId != null) {
                                booking.setDiscountId(selectedDiscountId);
                            }
                            
                            // Calculate total amount
                            BigDecimal tourPrice = cartTour.getPrice();
                            BigDecimal adultPrice = tourPrice.multiply(new BigDecimal(cart.getNumberOfPeople()));
                            BigDecimal childPrice = BigDecimal.ZERO;
                            if (cart.getNumberOfChild() > 0) {
                                childPrice = tourPrice.multiply(new BigDecimal(cart.getNumberOfChild())).multiply(new BigDecimal("0.5"));
                            }
                            
                            // Apply discount if present (only to adult price)
                            BigDecimal totalPrice = adultPrice.add(childPrice);
                            BigDecimal discountAmount = BigDecimal.ZERO;
                            
                            if (selectedDiscountId != null) {
                                DAO.DiscountDAO discountDAO = new DAO.DiscountDAO();
                                model.Discount discount = discountDAO.getDiscountById(selectedDiscountId);
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
                                System.out.println("Successfully created booking ID: " + bookingId + " for tour ID: " + cart.getTourId());
                                successfulBookingIds.add(bookingId);
                                
                                // Delete cart item after successful booking
                                cartDAO.deleteFromCart(cart.getId(), userId);
                                System.out.println("Deleted cart item ID: " + cart.getId());
                            } else {
                                System.out.println("Failed to create booking for tour ID: " + cart.getTourId());
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
                    session.removeAttribute("selectedDiscount");
                    
                    // Clear cookies
                    Cookie cookie = new Cookie("selectedCartItems", "");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    
                    if (allSuccess) {
                        // All bookings created successfully
                        request.getRequestDispatcher("Info.jsp").forward(request, response);
                    } else if (!successfulBookingIds.isEmpty()) {
                        // Some bookings succeeded - show partial success message
                        request.setAttribute("successMessage", "Some bookings were processed successfully.");
                        request.getRequestDispatcher("Info.jsp").forward(request, response);
                    } else {
                        // All bookings failed
                        request.setAttribute("error", "Failed to process bookings.");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                    }
                    return;
                }

                // Get booking parameters from the request for single booking
                String tourIdParam = request.getParameter("tourId");
                String peoplecount = request.getParameter("peopleCount");
                String childcount = request.getParameter("childCount"); // New parameter for children
                
                
                // Get the discount ID if available
                String discountIdParam = request.getParameter("discountId");
                Integer discountId = null;
                if (discountIdParam != null && !discountIdParam.isEmpty()) {
                    discountId = Integer.parseInt(discountIdParam);
                }

                // Validate that the number of people is provided and is a valid integer
                if (peoplecount == null || !peoplecount.matches("\\d+")) {
                    request.getRequestDispatcher("List.jsp").forward(request, response);
                    return;
                }

                // Process child count (default to 0 if not provided)
                int childCount = 0;
                if (childcount != null && childcount.matches("\\d+")) {
                    childCount = Integer.parseInt(childcount);
                }

                // Validate that the tour ID is provided and is a valid integer
                if (tourIdParam == null || !tourIdParam.matches("\\d+")) {
                    request.setAttribute("error", "Invalid tour ID.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // Validate that the payment method is valid
                if (pay == null || !(pay.equals("Tiền mặt") || pay.equals("Thẻ ngân hàng") || pay.equals("Visa"))) {
                    request.setAttribute("error", "Invalid payment method.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                int tourId = Integer.parseInt(tourIdParam);
                int peopleCounts = Integer.parseInt(peoplecount);

                // Fetch the tour details from the database
                Tour tour = bookingDAO.getTourById(tourId);

                // Check if the tour exists; if not, set an error message
                if (tour == null) {
                    request.setAttribute("error", "Tour information not found.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                try {
                    // Use the current date as the booking date
                    java.sql.Date bookingDATE = new java.sql.Date(System.currentTimeMillis());

                    // Create a new Booking object with the provided details
                    Booking booking = new Booking();
                    booking.setUserId(userId);
                    booking.setTourId(tourId);
                    booking.setBookingDate(bookingDATE);
                    booking.setStatus("Pending");
                    booking.setNumberOfAdults(peopleCounts);
                    booking.setNumberOfChildren(childCount);
                    booking.setPay(pay);
                    
                    // Set discount ID if provided
                    if (discountId != null) {
                        booking.setDiscountId(discountId);
                    }
                    
                    // Calculate total amount - adults pay full price, children get 50% discount
                    BigDecimal tourPrice = tour.getPrice();
                    BigDecimal adultPrice = tourPrice.multiply(new BigDecimal(peopleCounts));
                    BigDecimal childPrice = BigDecimal.ZERO;
                    if (childCount > 0) {
                        childPrice = tourPrice.multiply(new BigDecimal(childCount)).multiply(new BigDecimal("0.5"));
                    }
                    
                    // Apply discount if present (only to adult price)
                    BigDecimal totalPrice = adultPrice.add(childPrice);
                    BigDecimal discountAmount = BigDecimal.ZERO;
                    
                    if (discountId != null) {
                        DAO.DiscountDAO discountDAO = new DAO.DiscountDAO();
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
                    
                    // Save the booking to the database and get the generated booking ID
                    int bookingId = bookingDAO.bookTour(booking);

                    if (bookingId > 0) {
                        // If booking is successful, delete the cart item if it came from cart
                        Cart selectedCart = (Cart) session.getAttribute("selectedCart");
                        if (selectedCart != null) {
                            try {
                                // Delete the item from cart
                                DAO.CartDAO cartDAO = new DAO.CartDAO();
                                cartDAO.deleteFromCart(selectedCart.getId(), userId);
                                System.out.println("Deleted cart item ID: " + selectedCart.getId() + " after successful booking");
                            } catch (Exception e) {
                                System.out.println("Error removing cart item: " + e.getMessage());
                            }
                        }
                        
                        // Redirect to the invoice view
                        request.getRequestDispatcher("Info.jsp").forward(request, response);
                    } else {
                        // Set an error message if booking fails
                        request.setAttribute("error", "Booking failed.");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                    }
                    
                    // Clean up session attributes that were used during checkout
                    session.removeAttribute("selectedDiscount");
                    session.removeAttribute("discountedPrice");
                    session.removeAttribute("selectedCart");
                    session.removeAttribute("selectedTour");
                    session.removeAttribute("numberOfPeople");
                    session.removeAttribute("numberOfChild");
                    
                } catch (Exception e) {
                    // Handle any exceptions by setting an error message
                    request.setAttribute("error", "An error occurred: " + e.getMessage());
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
                break;

            // ... rest of the switch cases stay the same
        }
    }

    // Helper method to handle errors by setting an error message and forwarding to a specified page
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, String targetPage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher(targetPage).forward(request, response);
    }

    /**
     * Helper method to get cookie value by name
     */
    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Helper method to parse cart IDs and discount from cookie value
     */
    private Map<String, Object> parseCartIdsFromCookie(String cookieValue) {
        List<Integer> cartIds = new ArrayList<>();
        String discountId = null;
        
        try {
            if (cookieValue != null && !cookieValue.isEmpty()) {
                String decodedValue = java.net.URLDecoder.decode(cookieValue, java.nio.charset.StandardCharsets.UTF_8.toString());
                System.out.println("Decoded cookie value: " + decodedValue);
                
                // Extract cart IDs from cartIds array
                if (decodedValue.contains("\"cartIds\":[")) {
                    int startIdx = decodedValue.indexOf("\"cartIds\":[") + 10;
                    int endIdx = decodedValue.indexOf("]", startIdx);
                    if (endIdx > startIdx) {
                        // Get the entire array content: ["5","7"]
                        String itemsJson = decodedValue.substring(startIdx, endIdx);
                        System.out.println("Raw cart IDs from cookie: " + itemsJson);
                        
                        // Remove the outer brackets if they exist
                        itemsJson = itemsJson.replace("[", "").replace("]", "");
                        
                        // Split by comma and clean up the quotes
                        if (!itemsJson.isEmpty()) {
                            String[] items = itemsJson.split(",");
                            
                            for (String item : items) {
                                // Clean up the item (remove quotes and spaces)
                                item = item.trim().replace("\"", "");
                                
                                try {
                                    int cartId = Integer.parseInt(item);
                                    cartIds.add(cartId);
                                    System.out.println("Found cartId in cookie: " + cartId);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid cartId in cookie: " + item);
                                }
                            }
                        }
                    }
                }
                
                // Extract discount ID
                if (decodedValue.contains("\"discountId\":")) {
                    int startIndex = decodedValue.indexOf("\"discountId\":") + 13;
                    int endIndex = decodedValue.indexOf(",", startIndex);
                    if (endIndex == -1) {
                        endIndex = decodedValue.indexOf("}", startIndex);
                    }
                    if (endIndex > startIndex) {
                        discountId = decodedValue.substring(startIndex, endIndex).trim();
                        if (discountId.startsWith("\"") && discountId.endsWith("\"")) {
                            discountId = discountId.substring(1, discountId.length() - 1);
                        }
                        System.out.println("Found discountId in cookie: " + discountId);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing cookie data: " + e.getMessage());
            e.printStackTrace();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("cartIds", cartIds);
        result.put("discountId", discountId);
        return result;
    }
}
