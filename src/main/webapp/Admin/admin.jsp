<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/style.css">  
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
        <!-- Chart.js for charts -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                background: #f5f5f5;
            }

            /* Sidebar */
            .sidebar {
                width: 250px;
                background: #1e2a44;
                color: white;
                padding: 30px 20px;
                display: flex;
                flex-direction: column;
                gap: 20px;
                position: fixed;
                height: 100vh;
                overflow-y: auto;
                box-shadow: 5px 0 15px rgba(0, 0, 0, 0.2);
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
                background: #00ddeb;
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
                background: #00ddeb;
                color: #1e2a44;
                transform: translateX(5px);
            }

            .sidebar .menu-item a.active {
                background: #00ddeb;
                color: #1e2a44;
                font-weight: 600;
            }

            .sidebar a i {
                font-size: 20px;
                transition: transform 0.3s ease;
            }

            .sidebar .menu-item a:hover i {
                transform: scale(1.2);
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
                background: #ff4d4d;
                color: #ffffff;
                transform: translateX(5px);
            }

            /* Content area */
            .content {
                margin-left: 250px;
                flex: 1;
                padding: 30px;
            }

            /* Dashboard header */
            .dashboard-header {
                margin-bottom: 30px;
            }

            .dashboard-header h2 {
                font-size: 28px;
                color: #333;
                margin-bottom: 10px;
            }

            /* Stats boxes */
            .stats-container {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 20px;
                margin-bottom: 30px;
            }

            .stat-box {
                background: white;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
                text-align: center;
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .stat-box:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
            }

            .stat-box h3 {
                font-size: 16px;
                color: #666;
                margin-bottom: 10px;
            }

            .stat-box .stat-value {
                font-size: 24px;
                font-weight: 600;
                color: #333;
            }

            /* Chart containers */
            .chart-container {
                background: white;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
                margin-bottom: 30px;
            }

            .chart-container h3 {
                font-size: 18px;
                color: #333;
                margin-bottom: 20px;
            }

            .chart-wrapper {
                position: relative;
                height: 300px;
            }
        </style>
    </head>
    <body>
        <div class="sidebar">
            <h3>Admin Panel</h3>

            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/admin-dashboard">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
            </div>
            <c:if test="${sessionScope.user != null && sessionScope.user.userRole == 'Admin'}">
                <div class="menu-item">
                    <a href="<%= request.getContextPath()%>/AdminUserServlet">
                        <i class="fas fa-users"></i> Khách hàng
                    </a>
                </div>
            </c:if>
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
                <a href="<%= request.getContextPath()%>/adminlist" class="<%= request.getRequestURI().contains("adminTour") ? "active" : ""%>">
                    <i class="fas fa-map-marked-alt"></i> Danh sách Tour
                </a>
            </div>
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/discountManage" class="<%= request.getRequestURI().contains("discountManage") ? "active" : ""%>">
                    <i class="fa-solid fa-ticket"></i> Danh sách Voucher
                </a>
            </div>
            <!-- yêu cầu tour  -->
            <div class="menu-item">
                <a href="<%= request.getContextPath()%>/admin-request-list" class="<%= request.getRequestURI().contains("/admin-request-list") ? "active" : ""%>">
                    <i class="fa-solid fa-clipboard-list"></i> Yêu cầu tour
                </a>
            </div>

            <a href="<%= request.getContextPath()%>/SortTour">
                <i class="fas fa-sign-out-alt"></i> Về Trang Chủ
            </a>
        </div>

        <!-- Content area -->
        <div class="content">
            <div class="dashboard-header">
                <h2>DashBoard</h2>
                <p>Thống kê 7 ngày gần nhất</p>
            </div>

            <!-- Stats boxes -->
            <div class="stats-container">
                <div class="stat-box">
                    <h3>Total Order</h3>
                    <div class="stat-value">${totalBookings}</div>
                </div>
                <div class="stat-box">
                    <h3>Total Amount</h3>
                    <div class="stat-value">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                    </div>
                </div>
                <div class="stat-box">
                    <h3>Total Customer</h3>
                    <div class="stat-value">${totalUsers}</div>
                </div>
                <div class="stat-box">
                    <h3>Total Tour</h3>
                    <div class="stat-value">${totalTours}</div>
                </div>
            </div>

            <!-- Status Order Chart -->
            <div class="chart-container">
                <h3>Status Order (7 ngày gần nhất)</h3>
                <div class="chart-wrapper">
                    <canvas id="statusOrderChart"></canvas>
                </div>
            </div>

            <!-- Total Amount Chart -->
            <div class="chart-container">
                <h3>Total Amount (7 ngày gần nhất)</h3>
                <div class="chart-wrapper">
                    <canvas id="totalAmountChart"></canvas>
                </div>
            </div>

        </div>

        <script>
            // Prepare labels for charts
            const labels = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
            "${day}"<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            // Prepare data for status chart
            const confirmedData = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
                ${statusByDay[day].Confirmed}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            const pendingData = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
                ${statusByDay[day].Pending}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            const cancelledData = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
                ${statusByDay[day].Cancelled}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            const processingData = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
                ${statusByDay[day].Processing}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            // Prepare data for revenue chart
            const revenueData = [
            <c:forEach items="${last7DaysLabels}" var="day" varStatus="status">
                ${revenueByDay[day]}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            // Status Order Chart
            const statusOrderCtx = document.getElementById('statusOrderChart').getContext('2d');
            const statusOrderChart = new Chart(statusOrderCtx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Confirmed Orders',
                            data: confirmedData,
                            backgroundColor: '#3490dc',
                            barPercentage: 0.6,
                            categoryPercentage: 0.5
                        },
                        {
                            label: 'Cancelled Orders',
                            data: cancelledData,
                            backgroundColor: '#e3342f',
                            barPercentage: 0.6,
                            categoryPercentage: 0.5
                        },
                        {
                            label: 'Pending Orders',
                            data: pendingData,
                            backgroundColor: '#ffed4a',
                            barPercentage: 0.6,
                            categoryPercentage: 0.5
                        },
                        {
                            label: 'Processing Orders',
                            data: processingData,
                            backgroundColor: '#38c172',
                            barPercentage: 0.6,
                            categoryPercentage: 0.5
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                drawBorder: false
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });

            // Total Amount Chart
            const totalAmountCtx = document.getElementById('totalAmountChart').getContext('2d');
            const totalAmountChart = new Chart(totalAmountCtx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Total Amount',
                            data: revenueData,
                            fill: false,
                            borderColor: '#3490dc',
                            tension: 0.4,
                            pointBackgroundColor: '#3490dc'
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                borderDash: [2, 2]
                            }
                        },
                        x: {
                            grid: {
                                display: true,
                                borderDash: [2, 2]
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });
        </script>
    </body>
</html>