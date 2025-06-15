<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Chi tiết mã giảm giá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background-color: #f8f9fa;
                padding: 40px;
                color: #333;
            }

            h2 {
                color: #0066cc;
                margin-bottom: 20px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 4px 8px rgba(0,0,0,0.05);
                margin-bottom: 30px;
            }

            th, td {
                padding: 15px;
                text-align: center;
                border-bottom: 1px solid #eaeaea;
            }

            th {
                background-color: #e6f2ff;
                font-weight: 600;
            }

            tr:hover {
                background-color: #f1f9ff;
            }

            img {
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .pagination {
                text-align: center;
                margin-top: 20px;
            }

            .pagination a,
            .pagination span {
                display: inline-block;
                padding: 8px 14px;
                margin: 0 5px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
                color: #007bff;
                background-color: #e9f2ff;
                transition: background-color 0.3s, color 0.3s;
            }

            .pagination a:hover {
                background-color: #007bff;
                color: white;
            }

            .pagination .current {
                background-color: #ff6666;
                color: white;
                font-weight: bold;
            }
        </style>
    </head>
    <body>

        <!-- Link Bootstrap 5 nếu chưa có -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <div class="container mt-5">
            <h2 class="mb-4 text-primary">Chi tiết mã giảm giá</h2>

            <form action="discountCreate" method="post" class="row g-3">
                <input type="hidden" name="action" value="create">
                <!-- Discount % -->
                <div class="mb-3">
                    <label for="discount_percent" class="form-label">Phần trăm giảm (%):</label>
                    <input value="${dis}" type="number" name="discount_percent" class="form-control ${not empty errorDiscountPercent ? 'is-invalid' : ''}"
                           min="0.01" max="99.99" step="0.01" value="${discount_percent}" required>
                    <div class="invalid-feedback">${errorDiscountPercent}</div>
                </div>

                <!-- Start Date -->
                <div class="mb-3">
                    <label class="form-label">Ngày bắt đầu:</label>
                    <input type="date" name="start_date" class="form-control ${not empty errorDate ? 'is-invalid' : ''}" value="${startDateStr}" required>
                     <div class="invalid-feedback">${errorDate}</div>
                </div>

                <!-- End Date -->
                <div class="mb-3">
                    <label class="form-label">Ngày kết thúc:</label>
                    <input type="date" name="end_date" class="form-control ${not empty errorDate ? 'is-invalid' : ''}" value="${endDateStr}" required>
                    <div class="invalid-feedback">${errorDate}</div>
                </div>

                <!-- Max Usage -->
                <div class="mb-3">
                    <label class="form-label">Số lượng:</label>
                    <input type="number" value="${max_use}" 
                           class="form-control ${not empty errorMax_usage ? 'is-invalid' : ''}"
                           name="max_usage"
                           min="1"
                           value="${max_usage}" required>
                    <div class="invalid-feedback">${errorMax_usage}</div>
                </div>

                <!-- Description -->
                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả:</label>
                    <textarea  name="description" class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" rows="4" required>${des}</textarea>
                    <div class="invalid-feedback">${errorMax_usage}</div>
                </div>

                <!-- Submit -->
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Tạo mã giảm giá</button>
                </div>
            </form>
        </div>
    </body>
</html>