<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <%@include file="/WEB-INF/inclu/header.jsp" %>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DANH SÁCH TOURIST</title>

        <!-- CSS Plugins from header.jsp -->
        <link
            href="<%= request.getContextPath()%>/assets/plugins/bootstrap/css/bootstrap.min.css"
            rel="stylesheet">
        <link
            href="<%= request.getContextPath()%>/assets/plugins/font-awesome/css/font-awesome.min.css"
            rel="stylesheet">
        <link
            href="https://fonts.googleapis.com/css?family=Montserrat:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&subset=vietnamese"
            rel="stylesheet">
        <link href="<%= request.getContextPath()%>/assets/css/style5059.css?v=20"
              rel="stylesheet">
        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/assets/css/colors/default.css"
              id="option_color">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

        <!-- Custom Styles -->
        <style>
            .controls {
                margin: 20px 0;
                text-align: center;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 10px;
            }

            .controls button,
            .controls input,
            .controls select {
                margin: 0 10px;
                padding: 8px 15px;
                border-radius: 5px;
                font-family: 'Montserrat', sans-serif;
            }

            .controls button,
            .controls select {
                background-color: #007bff;
                color: #fff;
                border: none;
                transition: background-color 0.3s ease;
            }

            .controls button:hover,
            .controls select:hover {
                background-color: #0056b3;
            }

            .trip-container {
                padding: 30px 0;
            }

            .trip-info {
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
                overflow: hidden;
                transition: transform 0.3s ease;
            }

            .trip-info:hover {
                transform: translateY(-10px);
            }

            .trip-image img {
                width: 100%;
                height: 200px;
                object-fit: cover;
            }

            .trip-image .logo {
                position: absolute;
                top: 10px;
                left: 10px;
                background: rgba(0, 0, 0, 0.7);
                color: #fff;
                padding: 5px 10px;
                border-radius: 3px;
                font-size: 12px;
            }

            .trip-details {
                padding: 15px;
            }

            .trip-details h1 {
                font-size: 18px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .trip-details h1 a {
                color: #333;
                text-decoration: none;
            }

            .trip-details h1 a:hover {
                color: #007bff;
            }

            .trip-details p {
                margin: 5px 0;
                color: #666;
                font-size: 14px;
            }

            .tour-type {
                font-size: 14px;
                color: #28a745;
                font-weight: bold;
            }

            .price-date {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-top: 10px;
            }

            .price-date span {
                font-size: 16px;
                color: #007bff;
            }

            .dates button {
                background: #f8f9fa;
                border: 1px solid #ddd;
                padding: 5px 10px;
                margin-left: 5px;
                border-radius: 3px;
                font-size: 12px;
            }

            .trip-details form button {
                background-color: #28a745;
                color: #fff;
                border: none;
                padding: 8px 15px;
                border-radius: 5px;
                margin-top: 10px;
            }

            .trip-details form button:hover {
                background-color: #218838;
            }

            .pagination {
                text-align: center;
                margin: 20px 0;
            }

            .pagination a {
                padding: 10px 15px;
                margin: 0 5px;
                text-decoration: none;
                background-color: #007bff;
                color: white;
                font-weight: bold;
                border-radius: 5px;
                transition: background-color 0.3s ease;
            }

            .pagination a:hover {
                background-color: #0056b3;
            }

            .pagination a.active {
                background-color: #28a745;
                border: 2px solid #218838;
            }

            .no-data {
                text-align: center;
                color: #e74c3c;
                font-size: 18px;
                margin-top: 20px;
            }
        </style>
        <style>
            .search-box {
                background-color: rgba(255, 255, 255, 0.85);
                /* tr?ng m?, 0.85 = 85% ?? ??c */
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
            }

            .search-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .tour-type-tabs {
                display: flex;
                gap: 20px;
                margin: 15px 0;
            }

            .tour-type-tabs .tab {
                font-weight: 500;
                cursor: pointer;
                color: #007BFF;
            }

            .tour-type-tabs .tab.active {
                border-bottom: 2px solid #007BFF;
            }

            .form-section label {
                font-weight: 500;
            }

            .bodysearch {
                position: absolute;
                top: 40%;
                /* ho?c 60% n?u mu?n n?m d??i ?nh */
                left: 50%;
                transform: translate(-50%, -50%);
                width: 90%;
                max-width: 900px;
            }

            button.btn-primary {
                text-transform: none;
                /* gi? nguyên ch? vi?t */
            }
            .btn {

                margin-top: 16px;
            }
            .voucher-container {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
                padding: 20px;
            }
            .voucher-card {
                border: 1px solid #ccc;
                border-radius: 8px;
                width: 250px;
                padding: 16px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
                position: relative;
                background-color: #f9f9f9;
            }
            .voucher-code {
                font-size: 20px;
                font-weight: bold;
                margin-bottom: 8px;
            }
            .voucher-desc {
                font-size: 14px;
                margin-bottom: 8px;
            }
            .voucher-percent {
                color: green;
                font-weight: bold;
                margin-bottom: 12px;
            }
            .copy-btn {
                position: absolute;
                top: 10px;
                right: 10px;
                cursor: pointer;
                background: none;
                border: none;
                font-size: 16px;
            }
            .copy-success {
                color: green;
                font-size: 12px;
                margin-top: 5px;
            }
       
            body {
                font-family: 'Segoe UI', sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 20px;
                color: #333;
            }

            h2 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 30px;
            }

            form {
                display: flex;
                justify-content: center;
                gap: 10px;
                margin-bottom: 30px;
            }

            input[type="text"], input[type="date"] {
                padding: 8px 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
            }

            button {
                padding: 8px 16px;
                background-color: #3498db;
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
            }

            button:hover {
                background-color: #2980b9;
            }

            .voucher-container {
                max-width: 1200px;
                margin: auto;
            }

            .voucher-card {
                background-color: #fff;
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
                transition: transform 0.2s;
            }

            .voucher-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            .voucher-code {
                font-weight: bold;
                font-size: 18px;
                color: #e74c3c;
                margin-bottom: 10px;
            }

            .voucher-desc {
                margin-bottom: 8px;
                color: #555;
            }

            .voucher-percent {
                font-weight: bold;
                color: #27ae60;
            }

            .pagination {
                text-align: center;
                margin-top: 30px;
            }

            .pagination a {
                display: inline-block;
                margin: 0 5px;
                padding: 8px 12px;
                text-decoration: none;
                color: #3498db;
                border: 1px solid #3498db;
                border-radius: 5px;
                transition: background-color 0.3s, color 0.3s;
            }

            .pagination a:hover {
                background-color: #3498db;
                color: white;
            }

            .pagination a.current {
                background-color: #3498db;
                color: white;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <h2>Danh sách Voucher ?ã l?u</h2>

        <form method="get" action="discountSaveController">
            <input type="text" name="keyword" placeholder="Tìm theo keyword" value="${keyword}">
            <button type="submit">Search</button>
        </form>

        <div class="voucher-container">
            <c:if test="${empty savedVouchers}">
                <p style="text-align:center; color: gray;">Không có voucher nào th?a ?i?u ki?n.</p>
            </c:if>

            <c:forEach var="discount" items="${savedVouchers}">
                <div class="voucher-card">
                    <div class="voucher-desc">${discount.description}</div>
                    <div class="voucher-percent">Discount: ${discount.discount_percent}%</div>
                    <div class="voucher-end"
                         data-enddate="${discount.end_date}">
                       Expired: <span class="time-left">${discount.end_date}</span>
                    </div>
                    <span class="btn btn-danger delete-btn px-2" data-discount-id="${discount.id}">
                         Xoá voucher
                    </span>
                </div>
             </c:forEach>
            </div>
       
    </div>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="discountSaveController?page=${currentPage - 1}&search=${search}&startDate=${startDate}&endDate=${endDate}">Prev</a>
        </c:if>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a class="${i == currentPage ? 'current' : ''}" 
               href="discountSaveController?page=${i}&search=${search}&startDate=${startDate}&endDate=${endDate}">
                ${i}
            </a>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <a href="discountSaveController?page=${currentPage + 1}&search=${search}&startDate=${startDate}&endDate=${endDate}">Next</a>
        </c:if>
    </div>
<script>
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", async () => {
            const discountId = button.getAttribute("data-discount-id");

            if (!confirm("B?n có ch?c mu?n xoá voucher này?")) return;

            try {
                const res = await fetch("discountSaveController", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: `discountId=`+discountId+"&action=delete"
                });

                if (res.ok) {
                    alert("Xoá voucher thành công!");
                    location.reload(); // reload l?i trang
                } else {
                    alert("Không th? xoá voucher. ?ã x?y ra l?i.");
                }
            } catch (err) {
                console.error("L?i xoá voucher:", err);
                alert("L?i k?t n?i server.");
            }
        });
    });
});
</script>

</body>
</html>
