package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import DAO.RequestDAO;
import DAO.RequestDAO;
import util.EmailUtil;
import util.SessionLogin;
import DAO.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {"/RequestController"})
public class RequestController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RequestController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RequestController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("RequestTour.jsp").forward(request, response);

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
//        String role = user.getUserRole();
//
//// Nếu không phải Admin và cũng không phải Manager → chuyển về login
//        if (!"Admin".equalsIgnoreCase(role) && !"Manager".equalsIgnoreCase(role)) {
//            request.getRequestDispatcher("Login.jsp").forward(request, response);
//            return;
//        }

        try {
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String nationality = request.getParameter("nationality");
            String tourType = request.getParameter("tourType");
            String departure = request.getParameter("departure");
            String destination = request.getParameter("destination");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String numPeople = request.getParameter("numPeople");
            String message = request.getParameter("message");

            // Lưu vào DB
            DAO.RequestDAO.saveRequest(fullname, email, phone, nationality, tourType,
                    departure, destination, startDate, endDate, numPeople, message);

            // Gửi email xác nhận cho user
//            String subjectUser = "Yêu cầu tour đã được gửi";
//            String bodyUser = "<h3>Chào " + fullname + "</h3>"
//                    + "<p>Chúng tôi đã nhận được yêu cầu tour của bạn.</p>"
//                    + "<p>Tour: " + tourType + " | " + departure + " → " + destination + "</p>"
//                    + "<p>Ngày: " + startDate + " đến " + endDate + "</p>"
//                    + "<p>Số lượng: " + numPeople + "</p>";

            // Gửi email cho admin
//            String subjectAdmin = "Yêu cầu tour mới từ người dùng";
//            String bodyAdmin = "<p>Người dùng: " + fullname + " (" + email + ")</p>"
//                    + "<p>Tour: " + tourType + " | " + departure + " → " + destination + "</p>"
//                    + "<p>Thời gian: " + startDate + " - " + endDate + "</p>"
//                    + "<p>Số lượng: " + numPeople + "</p>"
//                    + "<p>Ghi chú: " + message + "</p>";
//
//            util.EmailUtil.sendEmail(email, subjectUser, bodyUser);
//            util.EmailUtil.sendEmail("admin@gmail.com", subjectAdmin, bodyAdmin);

            request.getRequestDispatcher("ThankYou.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
