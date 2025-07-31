package controller;

import common.Config;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import model.Booking;

@WebServlet(name = "VnPayPaymentServlet", urlPatterns = {"/vnpay"})
public class VnPayPaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("=== VNPay Servlet được gọi ===");
        System.out.println("Request URL: " + req.getRequestURL());
        System.out.println("Query String: " + req.getQueryString());

        HttpSession session = req.getSession();
        
        // Get parameters
        String amountStr = req.getParameter("amount");
        String tourId = req.getParameter("tourId");
        String discountIdStr = req.getParameter("discountId");
        String peopleCountStr = req.getParameter("peopleCount");
        String childCountStr = req.getParameter("childCount");
        String multiple = req.getParameter("multiple");
        boolean isMultiple = "true".equals(multiple);
        String vnp_TxnRef = Config.getRandomNumber(8); // Mã đơn hàng duy nhất
        
        // Debug parameters
        System.out.println("Amount: " + amountStr);
        System.out.println("Tour ID: " + tourId);
        System.out.println("Discount ID: " + discountIdStr);
        System.out.println("People Count: " + peopleCountStr);
        System.out.println("Child Count: " + childCountStr);
        System.out.println("Is Multiple: " + isMultiple);
        
        // Store parameters in session for use after payment returns
        if (tourId != null) {
            session.setAttribute("vnpay_tourId", tourId);
        }
        
        // Store peopleCount if provided
        if (peopleCountStr != null && !peopleCountStr.isEmpty()) {
            session.setAttribute("vnpay_peopleCount", peopleCountStr);
            System.out.println("Stored peopleCount in session: " + peopleCountStr);
        }
        
        // Store childCount if provided
        if (childCountStr != null && !childCountStr.isEmpty()) {
            session.setAttribute("vnpay_childCount", childCountStr);
            System.out.println("Stored childCount in session: " + childCountStr);
        }
        
        if (discountIdStr != null && !discountIdStr.isEmpty()) {
            try {
                int discountId = Integer.parseInt(discountIdStr);
                session.setAttribute("vnpay_discountId", discountId);
                System.out.println("Stored discountId in session: " + discountId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid discountId: " + discountIdStr);
            }
        }

        // Store multiple flag in session
        session.setAttribute("vnpay_multiple", isMultiple);
        System.out.println("Stored multiple flag in session: " + isMultiple);

        try {
            // Convert amount string to long, handling decimal values
            long amount;
            if (amountStr != null && !amountStr.isEmpty()) {
                if (amountStr.contains(".")) {
                    // Parse as double and convert to long (removes decimal places)
                    double amountDouble = Double.parseDouble(amountStr);
                    amount = Math.round(amountDouble) * 100; // VNPay requires amount * 100
                    System.out.println("Converted decimal amount: " + amountStr + " to long: " + amount);
                } else {
                    // Parse directly as long
                    amount = Long.parseLong(amountStr) * 100;
                }
            } else {
                throw new IllegalArgumentException("Amount parameter is missing or empty");
            }

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan tour ID: " + tourId);
            vnp_Params.put("vnp_OrderType", "250000");
            vnp_Params.put("vnp_Locale", "vn");

            // Add multiple flag to return URL if needed
            String returnUrl = Config.vnp_ReturnUrl;
            if (isMultiple) {
                if (returnUrl.contains("?")) {
                    returnUrl += "&multiple=true";
                } else {
                    returnUrl += "?multiple=true";
                }
                System.out.println("Updated return URL with multiple flag: " + returnUrl);
            }
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_IpAddr", Config.getIpAddress(req));
            
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Generate hash data
            String queryUrl = "";
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
            
            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

            System.out.println("🔐 Secure Hash = " + vnp_SecureHash);
            System.out.println("🔗 Hash Data = " + hashData.toString());
            System.out.println("🔗 Redirecting to: " + paymentUrl);

            resp.sendRedirect(paymentUrl);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("Lỗi thanh toán: " + e.getMessage());
        }
    }
}
