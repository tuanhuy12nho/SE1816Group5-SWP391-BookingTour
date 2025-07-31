package controller;

import DAO.ItineraryDAO;
import model.Itinerary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import model.User;

@WebServlet(name = "ItineraryServlet", urlPatterns = {"/itinerary"})
public class ItineraryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        String role = user.getUserRole();

// Nếu không phải Admin và cũng không phải Manager → chuyển về login
        if (!"Admin".equalsIgnoreCase(role) && !"Manager".equalsIgnoreCase(role)) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        String tourIdParam = request.getParameter("tourId");

        if (tourIdParam != null && tourIdParam.matches("\\d+")) {
            int tourId = Integer.parseInt(tourIdParam);

            ItineraryDAO dao = new ItineraryDAO();
            List<Itinerary> itineraries = dao.getItinerariesByTourId(tourId);

            request.setAttribute("itineraries", itineraries);
            request.setAttribute("tourId", tourId);
        } else {
            request.setAttribute("error", "ID tour không hợp lệ");
        }

        request.getRequestDispatcher("itinerary.jsp").forward(request, response);
    }
}
