<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/style.css">  
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <!-- Thêm Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
        <style>
            /* Reset mặc định */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Poppins', sans-serif;
                display: flex;
                min-height: 100vh;
                background: linear-gradient(270deg, #a1c4fd, #c2e9fb, #fbc2eb, #a1c4fd);
                background-size: 800% 800%;
                animation: hologram 15s ease infinite;
            }

            @keyframes hologram {
                0% {
                    background-position: 0% 50%;
                }
                50% {
                    background-position: 100% 50%;
                }
                100% {
                    background-position: 0% 50%;
                }
            }

            /* Sidebar */
            .sidebar {
                width: 250px;
                background: linear-gradient(180deg, #1e2a44 0%, #2c3e50 100%); /* Gradient nền */
                color: white;
                padding: 30px 20px;
                display: flex;
                flex-direction: column;
                gap: 20px;
                position: fixed;
                height: 100vh;
                overflow-y: auto;
                box-shadow: 5px 0 15px rgba(0, 0, 0, 0.2); /* Thêm bóng đổ */
                transition: all 0.3s ease;
            }

            .sidebar h3 {
                text-align: center;
                font-size: 26px;
                font-weight: 600;
                color: #ffffff;
                margin-bottom: 40px;
                letter-spacing: 1px;
                text-transform: uppercase;
                position: relative;
            }

            .sidebar h3::after {
                content: '';
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                width: 50px;
                height: 3px;
                background: #00ddeb; /* Dòng gạch chân màu cyan */
                border-radius: 2px;
            }

            .sidebar .menu-item a {
                display: flex;
                align-items: center;
                gap: 15px;
                padding: 12px 15px;
                color: #d1d5db;
                text-decoration: none;
                font-size: 16px;
                font-weight: 500;
                border-radius: 8px;
                transition: all 0.3s ease;
                position: relative;
                overflow: hidden;
            }

            .sidebar .menu-item a:hover {
                background: #00ddeb; /* Màu cyan khi hover */
                color: #1e2a44; /* Chữ đổi màu khi hover */
                transform: translateX(5px); /* Dịch chuyển nhẹ khi hover */
            }

            .sidebar .menu-item a.active {
                background: #00ddeb; /* Màu cyan cho mục active */
                color: #1e2a44;
                font-weight: 600;
            }

            .sidebar a i {
                font-size: 20px;
                transition: transform 0.3s ease;
            }

            .sidebar .menu-item a:hover i {
                transform: scale(1.2); /* Phóng to biểu tượng khi hover */
            }

            .sidebar a:last-child {
                margin-top: auto;
                display: flex;
                align-items: center;
                gap: 15px;
                padding: 12px 15px;
                color: #d1d5db;
                text-decoration: none;
                font-size: 16px;
                font-weight: 500;
                border-radius: 8px;
                transition: all 0.3s ease;
            }

            .sidebar a:last-child:hover {
                background: #ff4d4d; /* Màu đỏ khi hover vào nút "Về Trang Chủ" */
                color: #ffffff;
                transform: translateX(5px);
            }

            /* Content area */
            .content {
                margin-left: 250px; /* Để lại khoảng trống cho sidebar */
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh; /* Chiếm toàn bộ chiều cao màn hình */
                overflow: hidden; /* Ẩn phần thừa nếu video vượt quá kích thước */
            }

            /* Video container */
            .video-container {
                width: 100%;
                height: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .video-container video {
                width: 100%;
                height: 100%;
                object-fit: cover; /* Đảm bảo video lấp đầy khu vực mà không bị méo */
            }
        </style>
    </head>
    <body>
       <div class="sidebar">
            <h3>Admin Panel</h3>

            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminUserServlet"><i class="<%= request.getRequestURI().contains("AdminUserServlet") ? "active" : ""%>"></i> Khách hàng</a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminBookingServlet" class="<%= request.getRequestURI().contains("AdminBookingServlet") ? "active" : ""%>">
                    <i class="fas fa-chart-line"></i> Thống kê
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/AdminStatisticsServlet" class="<%= request.getRequestURI().contains("AdminStatisticsServlet") ? "active" : ""%>">
                    <i class="fas fa-dollar-sign"></i> Doanh thu
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/adminTour?filter=&sort=priceAsc" class="<%= request.getRequestURI().contains("adminTour") ? "active" : ""%>">
                    <i class="fas fa-map-marked-alt"></i> Danh sách Tour
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/discountManage" class="<%= request.getRequestURI().contains("discountManage") ? "active" : ""%>">
                    <i class="fa-solid fa-ticket"></i> Danh sách Voucher
                </a>
            </div>
                    
            <a href="<%= request.getContextPath()%>/SortTour">
                <i class="fas fa-sign-out-alt"></i> Về Trang Chủ
            </a>
        </div>


        <!-- Content area -->
        <div class="content">
            <div class="video-container">
                <video autoplay muted loop>
                    <source src="<%= request.getContextPath()%>/video/welcome.mp4" type="video/mp4">
                    Trình duyệt của bạn không hỗ trợ thẻ video.
                </video>
            </div>
        </div>

        <script>
            // Kiểm tra và đảm bảo video tự động phát
            document.addEventListener('DOMContentLoaded', function () {
                const video = document.querySelector('video');
                if (video) {
                    video.play().catch(error => {
                        console.error('Lỗi khi tự động phát video:', error);
                    });
                }
            });
        </script>
    </body>
</html>