/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import DAO.UserDAO;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;
import model.User;
import org.json.JSONObject;

/**
 *
 * @author USA
 */
@WebServlet(name = "GoogleLoginServlet", urlPatterns = { "/google-login" })
public class GoogleLoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "984198030178-ehvd77c47vad0nb2oms94j22bikjp4tc.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/Assigment/google-login";
    private static final String SCOPE = "profile email";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        OAuth20Service service = new ServiceBuilder(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .defaultScope(SCOPE)
                .callback(REDIRECT_URI)
                .build(GoogleApi20.instance());

        if (code == null) {
            // Bước 1: Chưa có mã code → chuyển hướng sang trang xác thực của Google
            String authorizationUrl = service.getAuthorizationUrl();
            response.sendRedirect(authorizationUrl);
        } else {
            // Bước 2: Đã có mã code → lấy Access Token và thông tin người dùng
            try {
                OAuth2AccessToken accessToken = service.getAccessToken(code);

                OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v2/userinfo");
                service.signRequest(accessToken, oauthRequest);

                Response oauthResponse = service.execute(oauthRequest);

                if (oauthResponse.getCode() == 200) {
                    String body = oauthResponse.getBody();

                    // Phân tích JSON để lấy email và tên người dùng
                    JSONObject json = new JSONObject(body);
                    String email = json.getString("email");
                    String fullName = json.getString("name");

                    // Authenticate user with Google credentials
                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.authenticateWithGoogle(email, fullName);
                    System.out.println(user.getUserRole());
                    if (user != null) {
                        // Set up session for the authenticated user
                    HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                        session.setAttribute("userId", user.getId());

                    // Debug output
                    System.out.println("Google Login Success:");
                    System.out.println("Email: " + email);
                        System.out.println("Name: " + fullName);
                        System.out.println("User ID: " + user.getId());
                    System.out.println("Session ID: " + session.getId());

                        // Redirect to home page after successful login
                        response.sendRedirect(request.getContextPath() + "/SortTour");
                    } else {
                        // Authentication failed
                        request.setAttribute("error", "Failed to authenticate with Google. Please try again.");
                        request.getRequestDispatcher("Login.jsp").forward(request, response);
                    }
                } else {
                    response.getWriter().println("Lỗi xác thực Google: " + oauthResponse.getBody());
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new ServletException("Lỗi xử lý OAuth", e);
            }
        }
    }
}
