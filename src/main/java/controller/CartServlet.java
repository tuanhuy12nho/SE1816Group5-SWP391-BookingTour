package controller;

import DAO.CartDAO;
import DAO.TourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import model.Cart;
import model.Tour;
import model.User;

@WebServlet(name = "CartServlet", urlPatterns = { "/cart" })
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO;
    private TourDAO tourDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        tourDAO = new TourDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("remove".equals(action)) {
            handleRemoveFromCart(request, response, user);
        } else {
            handleViewCart(request, response, user);
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

        String action = request.getParameter("action");

        switch (action) {
            case "add":
                handleAddToCart(request, response, user);
                break;
            case "update":
                handleUpdateCart(request, response, user);
                break;
            default:
                response.sendRedirect("cart");
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int tourId = Integer.parseInt(request.getParameter("tourId"));
            int numberOfPeople = Integer.parseInt(request.getParameter("numberOfPeople"));

            Tour tour = tourDAO.getTourById(tourId);
            if (tour == null) {
                request.setAttribute("error", "Tour not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Calculate total price
            BigDecimal totalPrice = tour.getPrice().multiply(new BigDecimal(numberOfPeople));

            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cart.setTourId(tourId);
            cart.setNumberOfPeople(numberOfPeople);
            cart.setTotalPrice(totalPrice);
            cart.setAddedDate(new Timestamp(System.currentTimeMillis()));
            cart.setStatus("In Cart"); // Add to cart and check if successful
            cartDAO.addToCart(cart);

            // Set success message and redirect to the cart page
            HttpSession session = request.getSession();
            session.setAttribute("cartMessage", "Tour đã được thêm vào giỏ hàng thành công!");

            // Fetch cart items to verify the item was added
            List<Cart> updatedCart = cartDAO.getCartByUserId(user.getId());
            if (updatedCart.isEmpty()) {
                session.setAttribute("cartMessage", "Tour không được thêm vào giỏ hàng. Vui lòng thử lại!");
                response.sendRedirect("booking?action=viewTour&id=" + tourId);
            } else {
                // Success - redirect to cart
                response.sendRedirect("cart");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input parameters");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error adding to cart: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int numberOfPeople = Integer.parseInt(request.getParameter("numberOfPeople"));

            Cart cart = cartDAO.getCartById(cartId);
            if (cart == null || cart.getUserId() != user.getId()) {
                response.sendRedirect("cart");
                return;
            }

            // Get tour details to recalculate total
            Tour tour = tourDAO.getTourById(cart.getTourId());
            BigDecimal totalPrice = tour.getPrice().multiply(new BigDecimal(numberOfPeople));

            cart.setNumberOfPeople(numberOfPeople);
            cart.setTotalPrice(totalPrice);
            cartDAO.updateCart(cart);

            response.sendRedirect("cart");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input parameters");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int cartId = Integer.parseInt(request.getParameter("id"));
            cartDAO.deleteFromCart(cartId, user.getId());
            response.sendRedirect("cart");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cart ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleViewCart(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Cart> carts = cartDAO.getCartByUserId(user.getId());

        // For debugging - log the number of carts found
        System.out.println("Found " + carts.size() + " items in cart for user ID: " + user.getId());

        // Check if cart is empty, add a message
        if (carts.isEmpty()) {
            request.setAttribute("cartEmpty", "Giỏ hàng của bạn đang trống.");
        }

        request.setAttribute("carts", carts);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}