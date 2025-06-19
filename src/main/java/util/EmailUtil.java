/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Utility class for sending emails
 * 
 * @author ACER
 */
public class EmailUtil {
    // Email configuration - Replace with your actual email settings
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String EMAIL = "vietshopmoc@gmail.com"; // Replace with your email
    private static final String PASSWORD = "xtkq isbn wxeo zwdh"; // Replace with your app password

    /*
     * CONFIGURATION INSTRUCTIONS:
     * 1. Replace EMAIL with your Gmail address
     * 2. Replace PASSWORD with your Gmail App Password (not your regular password)
     * 3. To get an App Password:
     * - Go to your Google Account settings
     * - Enable 2-Step Verification
     * - Go to Security > App passwords
     * - Generate a new app password for "Mail"
     * - Use that 16-character password here
     * 
     * For other email providers, update HOST and PORT accordingly:
     * - Outlook: smtp-mail.outlook.com, port 587
     * - Yahoo: smtp.mail.yahoo.com, port 587
     */

    /**
     * Send password reset email
     * 
     * @param toEmail    Recipient email address
     * @param resetToken Password reset token
     * @param baseUrl    Base URL of your application
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendPasswordResetEmail(String toEmail, String resetToken, String baseUrl) {
        String subject = "Dat Lai Mat Khau - BookingTour";
        String resetUrl = baseUrl + "/reset-password?token=" + resetToken;

        String body = "<html><body>" +
                "<h2>Yêu cầu đặt lại mật khẩu</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình.</p>" +
                "<p>Vui lòng nhấp vào liên kết bên dưới để đặt lại mật khẩu:</p>" +
                "<p><a href='" + resetUrl
                + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Đặt lại mật khẩu</a></p>"
                +
                "<p>Liên kết này sẽ hết hạn sau 30 phút.</p>" +
                "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>" +
                "<p>Trân trọng,<br>Đội ngũ BookingTour</p>" +
                "</body></html>";

        return sendEmail(toEmail, subject, body);
    }

    /**
     * Send email
     * 
     * @param toEmail Recipient email
     * @param subject Email subject
     * @param body    Email body (HTML)
     * @return true if sent successfully, false otherwise
     */
    private static boolean sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send test email (for testing purposes)
     * 
     * @param toEmail Recipient email
     * @return true if sent successfully, false otherwise
     */
    public static boolean sendTestEmail(String toEmail) {
        String subject = "Test Email - BookingTour";
        String body = "<html><body>" +
                "<h2>Test Email</h2>" +
                "<p>This is a test email from BookingTour application.</p>" +
                "<p>If you receive this email, the email configuration is working correctly.</p>" +
                "</body></html>";

        return sendEmail(toEmail, subject, body);
    }
}
