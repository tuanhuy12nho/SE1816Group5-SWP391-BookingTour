<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý mã giảm giá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f9f9f9;
                padding: 30px;
            }

            h2 {
                text-align: center;
                color: #333;
                margin-bottom: 30px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: white;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                border-radius: 8px;
                overflow: hidden;
            }

            th, td {
                padding: 15px 12px;
                border-bottom: 1px solid #e0e0e0;
                text-align: center;
            }

            th {
                background-color: #f4f6f9;
                color: #555;
                font-weight: 600;
            }

            tr:hover {
                background-color: #f1f1f1;
            }

            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 25px;
                list-style: none;
                padding: 0;
            }

            .pagination li {
                margin: 0 5px;
            }

            .pagination a,
            .pagination span {
                display: inline-block;
                padding: 8px 12px;
                text-decoration: none;
                color: #007bff;
                border: 1px solid #ddd;
                border-radius: 4px;
                background-color: #fff;
                transition: background-color 0.3s ease;
            }

            .pagination a:hover {
                background-color: #f0f0f0;
            }

            .pagination .active a {
                background-color: #007bff;
                color: white;
                border-color: #007bff;
            }

            .pagination .disabled span {
                color: #999;
                background-color: #f5f5f5;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>

        <h2>Danh sách mã giảm giá</h2>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã giảm giá</th>
                    <th>Mô tả</th>
                    <th>% giảm</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Số lượng tối đa</th>
                    <th>Đã sử dụng</th>
                    <th>Chi tiết</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="discount" items="${discounts}">
                    <tr>
                        <td>${discount.id}</td>
                        <td>******</td>
                        <td>${discount.description}</td>
                        <td>${discount.discount_percent}%</td>
                        <td>${discount.start_date}</td>
                        <td>${discount.end_date}</td>
                        <td>${discount.max_usage}</td>
                        <td>${discount.usage_count}</td>
                        <td><a href="discountDetail?id=${discount.id}">detail</a></td>
                        <td>
                            <button class="btn btn-danger btn-sm"
                                    onclick="openDeleteModal(${discount.id})">Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

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

        <!-- Pagination -->
        <c:if test="${totalPage > 1}">
            <ul class="pagination">
                <!-- Previous -->
                <c:if test="${pageIndex > 1}">
                    <li><a href="discountManage?pageIndex=${pageIndex - 1}">&laquo; Trước</a></li>
                    </c:if>

                <!-- Start & End -->
                <c:set var="start" value="${pageIndex - 2}" />
                <c:set var="end" value="${pageIndex + 2}" />
                <c:if test="${start < 1}">
                    <c:set var="end" value="${end + (1 - start)}" />
                    <c:set var="start" value="1" />
                </c:if>
                <c:if test="${end > totalPage}">
                    <c:set var="start" value="${start - (end - totalPage)}" />
                    <c:set var="end" value="${totalPage}" />
                </c:if>
                <c:if test="${start < 1}">
                    <c:set var="start" value="1" />
                </c:if>

                <!-- First page + ... -->
                <c:if test="${start > 1}">
                    <li><a href="discountManage?pageIndex=1">1</a></li>
                        <c:if test="${start > 2}">
                        <li class="disabled"><span>...</span></li>
                        </c:if>
                    </c:if>

                <!-- Page loop -->
                <c:forEach var="i" begin="${start}" end="${end}">
                    <li class="${i == pageIndex ? 'active' : ''}">
                        <a href="discountManage?pageIndex=${i}">${i}</a>
                    </li>
                </c:forEach>

                <!-- ... + Last page -->
                <c:if test="${end < totalPage}">
                    <c:if test="${end < totalPage - 1}">
                        <li class="disabled"><span>...</span></li>
                        </c:if>
                    <li><a href="discountManage?pageIndex=${totalPage}">${totalPage}</a></li>
                    </c:if>

                <!-- Next -->
                <c:if test="${pageIndex < totalPage}">
                    <li><a href="discountManage?pageIndex=${pageIndex + 1}">Tiếp &raquo;</a></li>
                    </c:if>
            </ul>
        </c:if>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            let selectedDiscountId = null;

            function openDeleteModal(discountId) {
                selectedDiscountId = discountId;

                const modalTitle = document.getElementById('modalTitle');
                const modalBody = document.getElementById('modalBody');

                modalTitle.textContent = 'Xác nhận xóa mã giảm giá';
                modalBody.textContent = 'Bạn có chắc chắn muốn xóa mã giảm giá này không?';

                const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
                modal.show();
            }

            document.getElementById('confirmActionBtn').addEventListener('click', function () {
                if (selectedDiscountId) {
                    fetch("discountManage?discountId=" + selectedDiscountId, {
                        method: 'POST' // hoặc 'POST' nếu bạn muốn an toàn hơn
                    })
                            .then(response => {
                                if (response.ok) {
                                    window.location.href = `discountManage`;
                                } else {
                                    alert("Xóa thất bại!");
                                }
                            })
                            .catch(error => {
                                console.error("Lỗi khi xóa:", error);
                                alert("Có lỗi xảy ra!");
                            });

                    bootstrap.Modal.getInstance(document.getElementById('confirmModal')).hide();
                }
            });
        </script>
    </body>
</html>