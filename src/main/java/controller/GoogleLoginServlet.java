/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;
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
                    String name = json.getString("name");

                    // TODO: Kiểm tra email trong DB và tạo user nếu chưa có

                    HttpSession session = request.getSession();
                    session.setAttribute("userEmail", email);
                    session.setAttribute("userName", name);

                    // Force session to be saved
                    session.setAttribute("googleLogin", true);

                    // Debug output
                    System.out.println("Google Login Success:");
                    System.out.println("Email: " + email);
                    System.out.println("Name: " + name);
                    System.out.println("Session ID: " + session.getId());

                    response.sendRedirect(request.getContextPath() + "/home");
                } else {
                    response.getWriter().println("Lỗi xác thực Google: " + oauthResponse.getBody());
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new ServletException("Lỗi xử lý OAuth", e);
            }
        }
    }
}
