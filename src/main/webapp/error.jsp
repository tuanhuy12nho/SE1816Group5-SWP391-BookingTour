<%-- 
    Document   : error
    Created on : Mar 7, 2025, 2:56:25 PM
    Author     : Admin
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Lỗi</title>
<style>
    body {
        font-family: 'Arial', sans-serif;
        margin: 0;
        padding: 0;
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: linear-gradient(135deg, #ffafbd, #ffc3a0); /* Pink to peach gradient */
        overflow-x: hidden;
    }
    .container {
        background: #fff;
        padding: 40px;
        border-radius: 20px;
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        text-align: center;
        width: 90%;
        max-width: 500px;
        animation: fadeIn 0.6s ease-in-out;
    }
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    .error-icon {
        font-size: 60px;
        color: #e74c3c; /* Red color for error icon */
        margin-bottom: 20px;
    }
    h2 {
        font-size: 28px;
        color: #333;
        margin-bottom: 10px;
    }
    .error-message {
        font-size: 16px;
        color: #e74c3c;
        margin-bottom: 30px;
        animation: blink 1.5s infinite; /* Blinking effect */
    }
    @keyframes blink {
        0% {
            opacity: 1;
        }
        50% {
            opacity: 0.3;
        }
        100% {
            opacity: 1;
        }
    }
    .go-home-btn {
        display: inline-block;
        padding: 12px 25px;
        background: linear-gradient(90deg, #c084fc, #a855f7); /* Purple gradient */
        color: #fff;
        text-decoration: none;
        border-radius: 25px;
        font-size: 16px;
        transition: all 0.3s ease;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
    .go-home-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
        background: linear-gradient(90deg, #a855f7, #9333ea); /* Darker purple gradient */
    }
</style>

<body>
    <div class="container">
        <div class="error-icon">⚠️</div> <!-- Using warning emoji as error icon -->
        <h2>Có Lỗi Xảy Ra</h2>
        <p class="error-message">${error}</p>
        <a href="<%= request.getContextPath()%>/SortTour" class="go-home-btn">Quay Về Trang Chủ</a>
    </div>

    <script>
        // Smooth scroll to top on page load
        window.addEventListener('load', () => {
            window.scrollTo({top: 0, behavior: 'smooth'});
        });
    </script>
</body>
