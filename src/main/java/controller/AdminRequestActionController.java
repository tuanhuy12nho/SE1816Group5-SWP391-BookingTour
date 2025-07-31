package controller;

import DAO.RequestDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import model.User;

@WebServlet(urlPatterns = {"/admin-request-action"})
public class AdminRequestActionController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminRequestActionController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminRequestActionController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        String role = user.getUserRole();

// Nếu không phải Admin và cũng không phải Manager → chuyển về login
        if (!"Admin".equalsIgnoreCase(role)) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action");

            String status;
            switch (action) {
                case "approve":
                    status = "Đã duyệt";
                    break;
                case "reject":
                    status = "Từ chối";
                    break;
                default:
                    status = "Chờ xử lý";
                    break;
            }

            RequestDAO dao = new RequestDAO();
            Map<String, String> req = dao.getRequestById(requestId);

            if (req == null) {
                request.setAttribute("error", "Không tìm thấy yêu cầu với ID: " + requestId);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Cập nhật trạng thái
            dao.updateRequestStatus(requestId, status);

            // Soạn nội dung email
            String subject;
            String body;

            if ("Đã duyệt".equals(status)) {
                subject = "✅ Yêu cầu tour đã được PHÊ DUYỆT";
                body = "Chào " + req.get("fullname") + ",\n\n"
                        + "Yêu cầu tour của bạn đã được phê duyệt.\n"
                        + "Tour: " + req.get("tourType") + " | " + req.get("departure") + " → " + req.get("destination") + "\n"
                        + "Cảm ơn bạn đã lựa chọn dịch vụ của chúng tôi!";
            } else if ("Từ chối".equals(status)) {
                subject = "❌ Yêu cầu tour đã bị TỪ CHỐI";
                body = "Chào " + req.get("fullname") + ",\n\n"
                        + "Rất tiếc, yêu cầu tour của bạn đã bị từ chối.\n"
                        + "Nếu bạn cần thêm hỗ trợ, vui lòng liên hệ với chúng tôi.";
            } else {
                subject = "📥 Yêu cầu tour đang được xử lý";
                body = "Chào bạn, yêu cầu tour của bạn đang được xử lý. Vui lòng chờ phản hồi từ chúng tôi.";
            }

            // Gửi email
            util.EmailUtil.sendEmail(req.get("email"), subject, body);

            // Quay lại danh sách
            request.getRequestDispatcher("/Admin/List_Request_User.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý phê duyệt / từ chối yêu cầu tour và gửi email thông báo cho người dùng.";
    }

}
