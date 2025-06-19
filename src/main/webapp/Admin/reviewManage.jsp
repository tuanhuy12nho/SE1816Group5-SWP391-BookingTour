<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý đánh giá</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .hidden-comment {
                color: #6c757d !important;
            }
            .hidden-comment td:not(:last-child) {
                opacity: 0.5;
            }
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
                margin-top: 50px;
                margin-left: 250px;
                flex: 1;
                display: flex
                    ;
                justify-content: center;
                align-items: top;
                height: 100vh;
                overflow: hidden;
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
    <body class="bg-light">
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
                    <i class="fas fa-map-marked-alt"></i> Danh sách Voucher
                </a>
            </div>

            <a href="<%= request.getContextPath()%>/SortTour">
                <i class="fas fa-sign-out-alt"></i> Về Trang Chủ
            </a>
        </div>
        <div class="content">
            <div class="container">
                <h2 class="mb-5 text-center text-primary">Quản lý đánh giá tour</h2>

                <form method="get" action="manageReview" class="row g-3 mb-4">
                    <input type="hidden" name="tourId" value="${param.tourId != null ? param.tourId : 1}" />

                    <div class="col-md-4">
                        <label class="form-label">Từ khóa bình luận</label>
                        <div class="input-group">
                            <input type="text" name="commentKeyword" id="commentKeyword" class="form-control"
                                   value="${param.commentKeyword}" placeholder="Tìm theo bình luận..." />
                            <button type="button" class="btn btn-outline-secondary" onclick="document.getElementById('commentKeyword').value = ''">×</button>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Tên người dùng</label>
                        <div class="input-group">
                            <input type="text" name="userKeyword" id="userKeyword" class="form-control"
                                   value="${param.userKeyword}" placeholder="Tìm theo người dùng..." />
                            <button type="button" class="btn btn-outline-secondary" onclick="document.getElementById('userKeyword').value = ''">×</button>
                        </div>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Sắp xếp theo</label>
                        <select name="sortBy" class="form-select">
                            <option value="">--Chọn--</option>
                            <option value="rating_asc" ${param.sortBy == 'rating_asc' ? 'selected' : ''}>Rating ↑</option>
                            <option value="rating_desc" ${param.sortBy == 'rating_desc' ? 'selected' : ''}>Rating ↓</option>
                            <option value="date_asc" ${param.sortBy == 'date_asc' ? 'selected' : ''}>Date ↑</option>
                            <option value="date_desc" ${param.sortBy == 'date_desc' ? 'selected' : ''}>Date ↓</option>
                        </select>
                    </div>

                    <div class="col-md-1 d-flex align-items-end">
                        <button type="submit" class="btn btn-success w-100">Lọc</button>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-bordered table-hover bg-white shadow-sm">
                        <thead class="table-primary">
                            <tr>
                                <th>ID</th>
                                <th>Người đánh giá</th>
                                <th>Rating</th>
                                <th>Bình luận</th>
                                <th>Ngày</th>
                                <th>Ẩn</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${list}">
                                <tr class="${r.status == 'hidden' ? 'hidden-comment' : ''}" id="row-${r.id}">
                                    <td>${r.id}</td>
                                    <td>${r.username}</td>
                                    <td>
                                        <span class="badge bg-warning text-dark">${r.rating} ★</span>
                                    </td>
                                    <td>${r.comment}</td>
                                    <td>${r.reviewDate}</td>
                                    <td>
                                        <c:if test="${r.status == 'hidden'}">
                                            <button style="opacity: 1" class="btn btn-outline-success btn-sm" onclick="openConfirmModal(${r.id}, 'restore')">Khôi phục</button>
                                        </c:if>

                                        <c:if test="${r.status == 'show' || r.status == null}">
                                            <button class="btn btn-outline-danger btn-sm" onclick="openConfirmModal(${r.id}, 'hide')">Ẩn</button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="6" class="text-center text-danger">Không có đánh giá nào.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- Modal xác nhận ẩn/khôi phục -->
                <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header bg-warning">
                                <h5 class="modal-title" id="modalTitle">Xác nhận</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="modalBody">
                                <!-- Nội dung xác nhận sẽ được cập nhật bằng JS -->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                <button type="button" class="btn btn-danger" id="confirmActionBtn">Xác nhận</button>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Pagination -->
                <%-- Phần phân trang giữ nguyên như cũ (có thể copy lại nếu cần) --%>
                <c:if test="${totalPage > 1}">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">

                            <!-- Prev button -->
                            <c:if test="${pageIndex > 1}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=${pageIndex - 1}">
                                        &laquo; Prev
                                    </a>
                                </li>
                            </c:if>

                            <c:set var="start" value="${pageIndex - 2}" />
                            <c:set var="end" value="${pageIndex + 2}" />
                            <c:if test="${start < 1}">
                                <c:set var="start" value="1" />
                            </c:if>
                            <c:if test="${end > totalPage}">
                                <c:set var="end" value="${totalPage}" />
                            </c:if>

                            <!-- First page and leading ellipsis -->
                            <c:if test="${start > 2}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=1">
                                        1
                                    </a>
                                </li>
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                </c:if>

                            <!-- If start == 2, include page 1 (no ellipsis) -->
                            <c:if test="${start == 2}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=1">
                                        1
                                    </a>
                                </li>
                            </c:if>

                            <!-- Page numbers -->
                            <c:forEach var="i" begin="${start}" end="${end}">
                                <li class="page-item ${i == pageIndex ? 'active' : ''}">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=${i}">
                                        ${i}
                                    </a>
                                </li>
                            </c:forEach>

                            <!-- Trailing ellipsis and last page -->
                            <c:if test="${end < totalPage - 1}">
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=${totalPage}">
                                        ${totalPage}
                                    </a>
                                </li>
                            </c:if>

                            <!-- If end == totalPage -1, include last page (no ellipsis) -->
                            <c:if test="${end == totalPage - 1}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=${totalPage}">
                                        ${totalPage}
                                    </a>
                                </li>
                            </c:if>

                            <!-- Next button -->
                            <c:if test="${pageIndex < totalPage}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="manageReview?tourId=${param.tourId}&commentKeyword=${param.commentKeyword}&userKeyword=${param.userKeyword}&sortBy=${param.sortBy}&pageIndex=${pageIndex + 1}">
                                        Next &raquo;
                                    </a>
                                </li>
                            </c:if>

                        </ul>
                    </nav>
                </c:if>

            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                let selectedReviewId = null;
                                                let selectedAction = null; // "hide" hoặc "restore"

                                                function openConfirmModal(reviewId, action) {
                                                    console.log("action: " + action)
                                                    selectedReviewId = reviewId;
                                                    selectedAction = action;

                                                    const modalTitle = document.getElementById('modalTitle');
                                                    const modalBody = document.getElementById('modalBody');

                                                    if (action === 'hide') {
                                                        modalTitle.textContent = 'Xác nhận ẩn bình luận';
                                                        modalBody.textContent = 'Bạn có chắc chắn muốn ẩn bình luận này không? Hành động này sẽ không thể hoàn tác.';
                                                    } else if (action === 'restore') {
                                                        modalTitle.textContent = 'Xác nhận khôi phục bình luận';
                                                        modalBody.textContent = 'Bạn có chắc chắn muốn khôi phục bình luận này không?';
                                                    }

                                                    const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
                                                    modal.show();
                                                }

                                                document.getElementById('confirmActionBtn').addEventListener('click', function () {
                                                    if (selectedReviewId && selectedAction) {
                                                        const statusValue = selectedAction === 'hide' ? 'hidden' : 'show';

                                                        fetch("manageReview?reviewId=" + selectedReviewId + "&status=" + statusValue, {
                                                            method: 'POST'
                                                        })
                                                                .then(response => {
                                                                    if (response.ok) {
                                                                        const row = document.getElementById('row-' + selectedReviewId);
                                                                        if (selectedAction === 'hide') {
                                                                            row.classList.add('hidden-comment');
                                                                            row.querySelector('td:last-child').innerHTML = '<button class="btn btn-outline-success btn-sm" onclick="openConfirmModal(' + selectedReviewId + ', \'restore\')">Khôi phục</button>';
                                                                        } else {
                                                                            row.classList.remove('hidden-comment');
                                                                            row.querySelector('td:last-child').innerHTML = '<button class="btn btn-outline-danger btn-sm" onclick="openConfirmModal(' + selectedReviewId + ', \'hide\')">Ẩn</button>';
                                                                        }
                                                                    }
                                                                });

                                                        bootstrap.Modal.getInstance(document.getElementById('confirmModal')).hide();
                                                    }
                                                });
        </script>

    </body>
</html>