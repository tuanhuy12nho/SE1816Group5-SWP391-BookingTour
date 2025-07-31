<%-- 
    Document   : ThankYou
    Created on : Jul 6, 2025, 4:07:27 PM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cảm ơn</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #e7f6f2;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .thank-box {
            text-align: center;
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0,0,0,0.1);
        }
        img {
            width: 100px;
            margin-bottom: 20px;
        }
        h2 {
            color: #2e7d32;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            color: white;
            background: #2e7d32;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="thank-box">
    <img src="https://cdn-icons-png.flaticon.com/512/190/190411.png" />
    <h2>Cảm ơn bạn đã gửi yêu cầu!</h2>
    <p>Chúng tôi sẽ liên hệ lại với bạn trong thời gian sớm nhất.</p>
    <a href="List.jsp">Trang Chủ</a>
</div>
</body>
</html>
