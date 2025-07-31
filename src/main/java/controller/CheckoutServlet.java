package controller;

import DAO.CartDAO;
import DAO.DiscountDAO;
import DAO.DiscountSaveDAO;
import DAO.TourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Date;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Cart;
import model.Discount;
import model.Tour;
import model.User;
import model.Booking;
import DAO.BookingDAO;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CartDAO cartDAO;
    private TourDAO tourDAO;
    private DiscountSaveDAO discountSaveDAO;
    private DiscountDAO discountDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        tourDAO = new TourDAO();
        discountSaveDAO = new DiscountSaveDAO();
        discountDAO = new DiscountDAO();
    }
    
    /**
     * Helper method to get cookie data by name
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
     * Parse the URL-encoded cookie value to extract cart IDs and discount ID
     * This method uses simple string manipulation instead of a JSON library
     */
    private Map<String, Object> parseCookieData(String cookieValue) {
        Map<String, Object> result = new HashMap<>();
        List<Integer> cartIds = new ArrayList<>();
        String discountId = "";
        
        try {
            if (cookieValue != null && !cookieValue.isEmpty()) {
                String decodedValue = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8.toString());
                System.out.println("Decoded cookie value: " + decodedValue);
                
                // Extract cart IDs from cartIds array
                if (decodedValue.contains("\"cartIds\":[")) {
                    int startIdx = decodedValue.indexOf("\"cartIds\":[") + 10;
                    int endIdx = decodedValue.indexOf("]", startIdx);
                    if (endIdx > startIdx) {
                        String itemsJson = decodedValue.substring(startIdx, endIdx);
                        
                        // The format is now just an array of strings: ["5","7"]
                        // Split by comma and clean up the quotes
                        String[] items = itemsJson.split(",");
                        
                        for (String item : items) {
                            // Clean up the item (remove quotes)
                            item = item.trim().replace("\"", "");
                            
                            try {
                                int cartId = Integer.parseInt(item);
                                cartIds.add(cartId);
                                System.out.println("Found cartId: " + cartId);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid cartId: " + item);
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
                        System.out.println("Found discountId: " + discountId);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing cookie data: " + e.getMessage());
            e.printStackTrace();
        }
        
        result.put("cartIds", cartIds);
        result.put("discountId", discountId);
        return result;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            // Get cart items from cookie
            String cookieData = getCookieValue(request, "selectedCartItems");
            List<Integer> selectedItemIds = new ArrayList<>();
            String discountIdParam = null;
            
            // First try form parameters (most reliable)
            String selectedItemsData = request.getParameter("selectedItemsData");
            if (selectedItemsData != null && !selectedItemsData.isEmpty()) {
                System.out.println("Found selectedItemsData from form: " + selectedItemsData);
                String[] items = selectedItemsData.split(",");
                for (String idStr : items) {
                    try {
                        selectedItemIds.add(Integer.parseInt(idStr.trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
                
                discountIdParam = request.getParameter("selectedDiscountId");
            }
            // Then try cookie (backup)
            else if (cookieData != null) {
                System.out.println("Using cookie data: " + cookieData);
                Map<String, Object> parsedData = parseCookieData(cookieData);
                @SuppressWarnings("unchecked")
                List<Integer> cartIds = (List<Integer>) parsedData.get("cartIds");
                if (cartIds != null && !cartIds.isEmpty()) {
                    selectedItemIds.addAll(cartIds);
                    System.out.println("Found " + cartIds.size() + " cart IDs in cookie");
                }
                
                discountIdParam = (String) parsedData.get("discountId");
            }
            
            // If still no cart items, check if a single cart ID was provided
            if (selectedItemIds.isEmpty()) {
                String cartIdParam = request.getParameter("cartId");
                if (cartIdParam != null && !cartIdParam.isEmpty()) {
                    System.out.println("Using single cartId: " + cartIdParam);
                    selectedItemIds.add(Integer.parseInt(cartIdParam));
                }
            }
            
            if (selectedItemIds.isEmpty()) {
                System.out.println("No cart items found, redirecting to cart");
                response.sendRedirect("cart");
                return;
            }
            
            System.out.println("Processing " + selectedItemIds.size() + " cart items: " + selectedItemIds);
            
            // Parse discount ID
            Integer discountId = null;
            Discount selectedDiscount = null;
            if (discountIdParam != null && !discountIdParam.isEmpty()) {
                try {
                    discountId = Integer.parseInt(discountIdParam);
                    selectedDiscount = discountDAO.getDiscountById(discountId);
                    System.out.println("Using discount ID: " + discountId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid discount ID: " + discountIdParam);
                }
            }
            
            // Get all cart items for the user
            List<Cart> allCarts = cartDAO.getCartByUserId(user.getId());
            System.out.println("Found " + allCarts.size() + " total carts for user ID: " + user.getId());

            // Use our new direct method to get carts by IDs
            List<Cart> carts = cartDAO.getCartsByIds(selectedItemIds, user.getId());
            System.out.println("Retrieved " + carts.size() + " carts by IDs for user ID: " + user.getId());

            if (carts.isEmpty()) {
                // Fall back to the old filtering method if direct query fails
                System.out.println("Direct query returned no carts, trying filtering method...");
                carts = new ArrayList<>();
                
                // Filter to only include selected carts
                for (Cart cart : allCarts) {
                    System.out.println("Checking cart ID: " + cart.getId() + ", tourId: " + cart.getTourId());
                    if (selectedItemIds.contains(cart.getId())) {
                        carts.add(cart);
                        System.out.println("Added cart ID " + cart.getId() + " to selected carts");
                    }
                }
            }
            
            if (carts.isEmpty()) {
                System.out.println("No matching carts found for user, redirecting to cart");
                response.sendRedirect("cart");
                return;
            }

            // Get all user's saved discounts
            List<Discount> savedDiscounts = discountSaveDAO.getSavedVouchersByUser(
                    user.getId(), "", null, null, 1, 20);

            // Get tour details for cart items
            Map<Integer, Tour> tourMap = new HashMap<>();
            BigDecimal totalAdultPrice = BigDecimal.ZERO;
            BigDecimal totalChildPrice = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Cart cart : carts) {
                Tour tour = tourDAO.getTourById(cart.getTourId());
                tourMap.put(cart.getTourId(), tour);
                
                // Calculate prices with child discount
                BigDecimal adultPrice = tour.getPrice().multiply(new BigDecimal(cart.getNumberOfPeople()));
                BigDecimal childPrice = BigDecimal.ZERO;
                if (cart.getNumberOfChild() > 0) {
                    childPrice = tour.getPrice().multiply(new BigDecimal(cart.getNumberOfChild())).multiply(new BigDecimal("0.5"));
                }
                BigDecimal cartTotal = adultPrice.add(childPrice);
                
                // Add to totals
                totalAdultPrice = totalAdultPrice.add(adultPrice);
                totalChildPrice = totalChildPrice.add(childPrice);
                totalAmount = totalAmount.add(cartTotal);
            }
            
            // Calculate discount if selected (only applies to adult prices)
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal finalAmount = totalAmount;
            
            if (selectedDiscount != null) {
                double discountPercent = selectedDiscount.getDiscount_percent();
                discountAmount = totalAdultPrice.multiply(BigDecimal.valueOf(discountPercent / 100));
                finalAmount = totalAmount.subtract(discountAmount);
            }

            // Store selected items for POST processing
            session.setAttribute("selectedItemIds", selectedItemIds);
            session.setAttribute("selectedDiscountId", discountId);
            
            // Set attributes for the JSP
            request.setAttribute("carts", carts);
            request.setAttribute("savedDiscounts", savedDiscounts);
            request.setAttribute("tourMap", tourMap);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("selectedDiscount", selectedDiscount);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("finalAmount", finalAmount);
            
            // Use first cart as the "selected" cart for display consistency with existing code
            if (!carts.isEmpty()) {
                Cart firstCart = carts.get(0);
                Tour firstTour = tourMap.get(firstCart.getTourId());
                
                request.setAttribute("selectedCart", firstCart);
                BigDecimal firstAdultPrice = firstTour.getPrice().multiply(new BigDecimal(firstCart.getNumberOfPeople()));
                BigDecimal firstChildPrice = BigDecimal.ZERO;
                if (firstCart.getNumberOfChild() > 0) {
                    firstChildPrice = firstTour.getPrice().multiply(new BigDecimal(firstCart.getNumberOfChild())).multiply(new BigDecimal("0.5"));
                }
                request.setAttribute("adultPrice", firstAdultPrice);
                request.setAttribute("childPrice", firstChildPrice);
                request.setAttribute("originalTotal", firstAdultPrice.add(firstChildPrice));
            }

            // Forward to checkout page using existing JSP
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("cart");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            // Get selected item IDs from session
            @SuppressWarnings("unchecked")
            List<Integer> selectedItemIds = (List<Integer>) session.getAttribute("selectedItemIds");
            Integer discountId = (Integer) session.getAttribute("selectedDiscountId");
            
            // If no selected items in session, try to get from cookie
            if (selectedItemIds == null || selectedItemIds.isEmpty()) {
                String cookieData = getCookieValue(request, "selectedCartItems");
                if (cookieData != null) {
                    Map<String, Object> parsedData = parseCookieData(cookieData);
                    @SuppressWarnings("unchecked")
                    List<Integer> cartIds = (List<Integer>) parsedData.get("cartIds");
                    if (cartIds != null && !cartIds.isEmpty()) {
                        selectedItemIds = cartIds;
                    }
                    
                    String discountIdStr = (String) parsedData.get("discountId");
                    if (discountId == null && discountIdStr != null && !discountIdStr.isEmpty()) {
                        try {
                            discountId = Integer.parseInt(discountIdStr);
                        } catch (NumberFormatException e) {
                            // Invalid discount ID
                        }
                    }
                }
            }
            
            // If still no selected items, try to get a single cart ID from request
            if (selectedItemIds == null || selectedItemIds.isEmpty()) {
                String cartIdParam = request.getParameter("cartId");
                if (cartIdParam != null && !cartIdParam.isEmpty()) {
                    selectedItemIds = new ArrayList<>();
                    selectedItemIds.add(Integer.parseInt(cartIdParam));
                } else {
                    // No items to process
                    response.sendRedirect("cart");
                    return;
                }
            }
            
            // Get selected discount if available
            Discount selectedDiscount = null;
            if (discountId != null) {
                selectedDiscount = discountDAO.getDiscountById(discountId);
            }
            
            // Process each selected cart item
            List<Cart> selectedCarts = new ArrayList<>();
            List<Booking> bookings = new ArrayList<>();
            BigDecimal totalAdultPrice = BigDecimal.ZERO;
            BigDecimal totalChildPrice = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (Integer cartId : selectedItemIds) {
                Cart cart = cartDAO.getCartById(cartId);
                
                if (cart != null && cart.getUserId() == user.getId()) {
                    selectedCarts.add(cart);
                    
                    // Get the tour details
                    Tour tour = tourDAO.getTourById(cart.getTourId());
                    
                    // Calculate prices with child discount
                    BigDecimal adultPrice = tour.getPrice().multiply(new BigDecimal(cart.getNumberOfPeople()));
                    BigDecimal childPrice = BigDecimal.ZERO;
                    if (cart.getNumberOfChild() > 0) {
                        childPrice = tour.getPrice().multiply(new BigDecimal(cart.getNumberOfChild())).multiply(new BigDecimal("0.5"));
                    }
                    
                    totalAdultPrice = totalAdultPrice.add(adultPrice);
                    totalChildPrice = totalChildPrice.add(childPrice);
                    totalAmount = totalAmount.add(adultPrice.add(childPrice));
                    
                    // Create booking object
                    Booking booking = new Booking();
                    booking.setUserId(user.getId());
                    booking.setTourId(cart.getTourId());
                    booking.setNumberOfAdults(cart.getNumberOfPeople());
                    booking.setNumberOfChildren(cart.getNumberOfChild());
                    if (discountId != null) {
                        booking.setDiscountId(discountId);
                    }
                    
                    // Set additional booking information
                    booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
                    booking.setStatus("Pending");
                    booking.setTourName(tour.getName());
                    booking.setLocation(tour.getLocation());
                    booking.setTransport(tour.getTransport());
                    booking.setPrice(tour.getPrice().doubleValue());
                    booking.setStartDate(tour.getStartDate());
                    booking.setEndDate(tour.getEndDate());
                    
                    // Add to bookings list
                    bookings.add(booking);
                }
            }
            
            // Apply discount if selected (only to adult price)
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal finalAmount = totalAmount;
            
            if (selectedDiscount != null) {
                double discountPercent = selectedDiscount.getDiscount_percent();
                discountAmount = totalAdultPrice.multiply(BigDecimal.valueOf(discountPercent / 100));
                finalAmount = totalAmount.subtract(discountAmount);
            }
            
            // Store information in session
            session.setAttribute("selectedCarts", selectedCarts);
            session.setAttribute("bookings", bookings);
            session.setAttribute("selectedDiscountId", discountId);
            session.setAttribute("totalAmount", finalAmount);
            
            // Get payment method
            String paymentMethod = request.getParameter("paymentMethod");
            
            if ("vnpay".equals(paymentMethod)) {
                // Redirect to VnPay payment with final amount
                response.sendRedirect("vnpay?amount=" + finalAmount.longValue());
            } else {
                // Regular checkout - direct insert bookings
                BookingDAO bookingDAO = new BookingDAO();
                boolean allSuccess = true;
                
                for (Booking booking : bookings) {
                    // Set payment method
                    booking.setPay(paymentMethod != null ? paymentMethod : "Tiền mặt");
                    
                    // Insert booking into database
                    int bookingId = bookingDAO.bookTour(booking);
                    if (bookingId <= 0) {
                        allSuccess = false;
                        break;
                    }
                }
                
                // Clean up resources
                if (allSuccess) {
                    // Clear session data
                    session.removeAttribute("selectedCarts");
                    session.removeAttribute("bookings");
                    session.removeAttribute("selectedDiscountId");
                    session.removeAttribute("totalAmount");
                    session.removeAttribute("selectedItemIds");
                    
                    // Clear cookies
                    Cookie cookie = new Cookie("selectedCartItems", "");
                    cookie.setMaxAge(0); // Delete cookie
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    
                    // Redirect to success page
                    response.sendRedirect("Info.jsp?success=true");
                } else {
                    // If any booking failed
                    response.sendRedirect("error.jsp?message=booking_failed");
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid parameters");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error processing checkout: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
} 