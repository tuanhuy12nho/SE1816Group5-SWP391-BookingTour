/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Random;

/**
 *
 * @author Diep Quoc Thang - CE182036
 */
public class VnPayConfig {
    public static final String vnp_Version = "2.1.0";
    public static final String vnp_Command = "pay";
    public static final String vnp_TmnCode = "V28QUZTX";
    public static final String vnp_HashSecret = "ARA3O0DDLV9QY5DAX9LJ03TC389JDI0O";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    
    // Return URL must match exactly the urlPattern in VnPayReturnServlet
    public static final String vnp_ReturnUrl = "http://localhost:8080/Assigment/vnpay-return";
    
     public static String getRandomNumber(int length) {
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return sb.toString();
    }
}
