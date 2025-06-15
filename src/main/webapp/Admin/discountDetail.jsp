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

            <form action="discountDetail" method="post" class="row g-3">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${discount.id}">

                <!-- Code -->
                <div class="col-md-6">
                    <label class="form-label">Code:</label>
                    <input type="text" class="form-control" style="user-select: nont" value="******" readonly>
                </div>

                <!-- Discount % -->
                <div class="mb-3">
                    <label for="discount_percent" class="form-label">Phần trăm giảm (%):</label>
                    <input type="number" name="discount_percent" class="form-control ${not empty errorDiscountPercent ? 'is-invalid' : ''}"
                           min="0.01" max="99.99" step="0.01" value="${discount.discount_percent}" required>
                    <div class="invalid-feedback">${errorDiscountPercent}</div>
                </div>

                <!-- Start Date -->
                <div class="mb-3">
                    <label class="form-label">Ngày bắt đầu:</label>
                    <input type="date" name="start_date" class="form-control ${not empty errorDate ? 'is-invalid' : ''}" value="${startDateStr}" required>
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
                    <input type="number"
                           class="form-control ${not empty errorMax_usage ? 'is-invalid' : ''}"
                           name="max_usage"
                           min="1"
                           value="${discount.max_usage}" required>
                    <div class="invalid-feedback">${errorMax_usage}</div>
                </div>

                <!-- Description -->
                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả:</label>
                    <textarea name="description" class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" rows="4" required>${discount.description}</textarea>
                    <div class="invalid-feedback">${errorMax_usage}</div>
                </div>

                <!-- Submit -->
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Cập nhật mã giảm giá</button>
                </div>
            </form>
        </div>



        <h4 class="mt-5">Danh sách tour áp dụng</h4>
        <table>
            <thead>
                <tr>
                    <th>Thao tác</th>
                    <th>Tên Tour</th>
                    <th>Địa điểm</th>
                    <th>Giá</th>
                    <th>Phương tiện</th>
                    <th>Thời gian</th>
                    <th>Ảnh</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tour" items="${tours}">
                    <tr id="row-${tour.id}">
                        <td>
                            <c:if test="${ids.contains(tour.id)}">
                                <button class="btn btn-danger" onclick="openConfirmModal(${tour.id}, 'remove')">Hủy</button>
                            </c:if>
                            <c:if test="${!ids.contains(tour.id)}">
                                <button  class="btn btn-success"  onclick="openConfirmModal(${tour.id}, 'add')">Áp dụng</button>
                            </c:if>
                        </td>
                        <td>${tour.name}</td>
                        <td>${tour.location}</td>
                        <td>${tour.price}</td>
                        <td>${tour.transport}</td>
                        <!--<td>${tour.startDate} - ${tour.endDate}</td>-->
                        <td><img src="${tour.imageUrl}" alt="Ảnh tour" width="100"></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${totalPage > 1}">
            <div class="pagination">
                <c:forEach begin="1" end="${totalPage}" var="i">
                    <c:choose>
                        <c:when test="${i == pageIndex}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="discountDetail?id=${discount.id}&pageIndex=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>

        <!-- Modal xác nhận -->
        <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow-lg rounded-3">
                    <div class="modal-header bg-warning text-white">
                        <h5 class="modal-title fw-bold" id="modalTitle">Xác nhận</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body text-center" id="modalBody">
                        <!-- Nội dung sẽ được cập nhật bằng JS -->
                    </div>
                    <div class="modal-footer justify-content-center">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="button" class="btn btn-danger" id="confirmActionBtn">Xác nhận</button>
                    </div>
                </div>
            </div>
        </div>


        <!-- Script -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                    let selectedTourId = null;
                                    let selectedAction = null;
                                    let discountId = ${discount.id};

                                    function openConfirmModal(tourId, action) {
                                        selectedTourId = tourId;
                                        selectedAction = action;

                                        const modalTitle = document.getElementById('modalTitle');
                                        const modalBody = document.getElementById('modalBody');

                                        if (action === 'remove') {
                                            modalTitle.textContent = 'Xác nhận xóa tour khỏi mã giảm giá';
                                            modalBody.textContent = 'Bạn có chắc chắn muốn xóa tour này khỏi mã giảm giá không?';
                                        } else if (action === 'add') {
                                            modalTitle.textContent = 'Xác nhận thêm tour vào mã giảm giá';
                                            modalBody.textContent = 'Bạn có chắc chắn muốn áp dụng mã giảm giá cho tour này?';
                                        }

                                        const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
                                        modal.show();
                                    }

                                    document.getElementById('confirmActionBtn').addEventListener('click', function () {
                                        if (selectedTourId && selectedAction) {
                                            const statusValue = selectedAction === 'add' ? 'add' : 'remove';

                                            fetch("discountDetail?action=updateTour&tourId=" + selectedTourId + "&status=" + statusValue + "&discountId=" + discountId, {
                                                method: 'POST'
                                            }).then(response => {
                                                if (response.ok) {
                                                    location.reload(); // Cập nhật lại trang sau khi thao tác thành công
                                                }
                                            });

                                            bootstrap.Modal.getInstance(document.getElementById('confirmModal')).hide();
                                        }
                                    });
        </script>

    </body>
</html>