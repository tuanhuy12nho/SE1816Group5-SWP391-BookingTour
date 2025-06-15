<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />

    <!-- SITE TITLE -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta property="fb:app_id" content="859491851234128"/>
    <title>BookingTour - Home Page</title>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="CÔNG TY TNHH MỘT THÀNH VIÊN DỊCH VỤ LỮ HÀNH SAIGONTOURIST"/>
    <meta property="og:url" content="index.html"/>
    <meta property="og:description" content="đặt tour saigontourist"/>
    <meta property="og:image" content="<%= request.getContextPath()%>/assets/img/logo-color-big.png"/>

    <!-- Font Awesome CDN -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <style>/* Slide Styles */
        .slide {
            position: relative;
            width: 100%;
            height: 100vh; /* Full viewport height */
            overflow: hidden;
            margin: 0;
            padding: 0;
        }
        .slide img {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%); /* Center the image */
            width: 100vw; /* Full viewport width */
            height: 100vh; /* Full viewport height */
            object-fit: cover; /* Ensure the image scales properly */
            z-index: 1;
        }</style>

    <!-- JAVASCRIPTS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            locale = 'vi';
            loggedIn = false;
        });
    </script>
</head>

<body class="changeHeader">
    <div id="wrap">
        <!-- HEADER -->
        <header id="header" class="header">


            <!-- Header Main -->
            <div class="header-main">
                <div class="container">
                    <div class="logo">
                        <a href="#"><img src="<%= request.getContextPath()%>/images/Logo_G3.png" alt="SaigonTourist Logo"></a>
                    </div>
                    <div class="main-menu">
                        <ul>
                            <li><a href="<%= request.getContextPath()%>/SortTour">TRANG CHỦ</a></li>
                            <li><a href="#">LIÊN HỆ</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="slide">
                <img src="<%= request.getContextPath()%>/images/travel.jpg" alt="Slide 3">
            </div>
        </header>
    </div>
</body>