<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>


<style>
    /* Profile Section */
    .profile-section {
        background: url('https://dulichviet.com.vn/images/bandidau/lac-vao-khung-canh-tho-mong-cua-thi-tran-hallstatt-khi-du-lich-ao.jpg') no-repeat center center fixed;
        background-size: cover;
        padding: 50px 0;
        min-height: calc(100vh - 200px);
        position: relative;
    }
    .profile-section::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.4); /* Lớp phủ tối để nội dung dễ đọc */
        z-index: 1;
    }
    .profile-container {
        position: relative;
        z-index: 2;
        max-width: 1200px;
        margin: 0 auto;
        display: flex;
        gap: 30px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        overflow: hidden;
    }

    /* Sidebar */
    .sidebar {
        width: 250px;
        background: #f5f5f5;
        padding: 20px;
        border-right: 1px solid #ddd;
    }
    .sidebar ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .sidebar ul li {
        margin-bottom: 10px;
    }
    .sidebar ul li a {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 10px 15px;
        color: #333;
        text-decoration: none;
        font-size: 16px;
        border-radius: 5px;
        transition: background-color 0.3s ease, color 0.3s ease;
    }
    .sidebar ul li a:hover {
        background: #e0e0e0;
        color: #003087;
    }
    .sidebar ul li.active a {
        background: #003087;
        color: #fff;
    }
    .sidebar ul li i {
        font-size: 18px;
    }

    /* Main Content */
    .main-content {
        flex: 1;
        padding: 30px;
    }
    .profile-header {
        font-size: 28px;
        font-weight: bold;
        color: #333;
        margin-bottom: 30px;
        text-align: center;
        position: relative;
    }
    .profile-header::after {
        content: '';
        width: 50px;
        height: 3px;
        background: #003087;
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
    }

    /* Info Section */
    .info-section {
        display: flex;
        gap: 30px;
        margin-bottom: 20px;
    }
    .info-section > div {
        flex: 1;
        background: #f9f9f9;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .info-section > div:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }
    .info-section h3 {
        font-size: 20px;
        font-weight: bold;
        color: #003087;
        margin-bottom: 15px;
        border-bottom: 1px solid #ddd;
        padding-bottom: 5px;
    }
    .info-section p {
        font-size: 15px;
        color: #555;
        margin: 10px 0;
    }
    .info-section p strong {
        color: #333;
        font-weight: 600;
    }

    /* Buttons */
    .btn-custom {
        display: inline-block;
        padding: 10px 25px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: bold;
        font-size: 15px;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }
    .btn-primary {
        background-color: #28a745; /* Màu xanh lá từ hình */
        color: #fff;
    }
    .btn-primary:hover {
        background-color: #218838;
        transform: translateY(-2px);
    }
    .btn-warning {
        background-color: #ffc107;
        color: #333;
    }
    .btn-warning:hover {
        background-color: #e0a800;
        transform: translateY(-2px);
    }
    .text-center {
        text-align: center;
    }
    .mt-4 {
        margin-top: 1.5rem;
    }

    /* Login Message */
    .login-message {
        text-align: center;
        padding: 20px;
        background: #ffebee;
        border-radius: 5px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    }
    .login-message p {
        font-size: 16px;
        color: #e74c3c;
        margin-bottom: 10px;
    }

    /* Responsive */
    @media (max-width: 768px) {
        .profile-container {
            flex-direction: column;
        }
        .sidebar {
            width: 100%;
            border-right: none;
            border-bottom: 1px solid #ddd;
        }
        .info-section {
            flex-direction: column;
        }
        .main-content {
            padding: 20px;
        }
        .profile-header {
            font-size: 24px;
        }
    }
    .tour-card-horizontal {
        display: flex;
        gap: 20px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        overflow: hidden;
        margin-bottom: 20px;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .tour-card-horizontal:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 18px rgba(0,0,0,0.1);
    }
    .tour-image {
        width: 300px;
        flex-shrink: 0;
    }
    .tour-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
    }
    .tour-info {
        padding: 20px;
        flex: 1;
    }
    .tour-info h3 {
        margin-top: 0;
        font-size: 22px;
        color: #003087;
        margin-bottom: 10px;
    }
    .tour-info p {
        margin: 6px 0;
        font-size: 15px;
        color: #444;
    }

</style>
<!-- Only include if head.jsp doesn't include Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<body class="changeHeader">
    <div id="wrap">
        <!-- HEADER -->
        <%@include file="/WEB-INF/inclu/head.jsp" %>

        <!-- Profile Section -->
        <section class="profile-section">
            <div class="profile-container">
                <!-- Sidebar -->
                <div class="sidebar">
                    <ul>
                        <li class="active"><a href="#"><i class="fa fa-user"></i> Hồ sơ</a></li>
                        <li><a href="<%= request.getContextPath()%>/ViewBookings?action=viewBookings"><i class="fa fa-shopping-cart"></i> Đặt chỗ của tôi</a></li>
                        <li><a href="<%= request.getContextPath()%>/favorite"><i class="fa-solid fa-heart"></i>Tour yêu thích</a></li>
                        <li><a href="<%= request.getContextPath()%>/UserServlet?action=showEditProfile"><i class="fa fa-cog"></i> Thay đổi thông tin</a></li>
                        <li><a href="<%= request.getContextPath()%>/UserServlet?action=showChangePassword"><i class="fa fa-lock"></i> Đổi mật khẩu</a></li>
                    </ul>
                </div>

                <!-- Main Content -->
                <div class="main-content">
                    <h2 class="profile-header">Tour yêu thích</h2>
                    <p style=""></p>
                    <c:if test="${messageSuss != null}">
                        <div class="alert alert-success" role="alert">
                            ${messageSuss}
                        </div>
                    </c:if>
                    <c:if test="${messageErr != null}">
                        <div class="alert alert-danger" role="alert">
                            ${messageErr}
                        </div>
                    </c:if>
                    <div>
                        <c:choose>
                            <c:when test="${empty favorites}">
                                <div style="text-align:center; flex: 1;">
                                    <p>Bạn chưa có tour yêu thích nào.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="favorites" items="${favorites}">
                                    <c:set var="tour" value="${favorites.tour}" />
                                    <div class="tour-card-horizontal">
                                        <div class="tour-image">
                                            <img src="${tour.imageUrl}" alt="${tour.name}" />
                                        </div>
                                        <div class="tour-info">
                                            <h3>${tour.name}</h3>
                                            <p><strong>Location:</strong> ${tour.location}</p>
                                            <p><strong>Type:</strong> ${tour.type}</p>
                                            <p><strong>Transport:</strong> ${tour.transport}</p>
                                            <p><strong>Start:</strong> ${tour.startDate}</p>
                                            <p><strong>End:</strong> ${tour.endDate}</p>
                                            <p><strong>Price:</strong> ${tour.price}</p>
                                            <p><strong>Description:</strong> ${tour.description}</p>
                                            <div class="text-center mt-4">
                                                <a class="btn-custom btn-warning" href="${pageContext.request.contextPath}/booking?action=viewTour&id=${tour.id}">View Tour</a>
                                                <!-- Nút Remove mở modal xác nhận -->
                                                <button 
                                                    type="button" 
                                                    class="btn-custom btn-primary" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#confirmDeleteModal" 
                                                    data-favid="${favorites.id}">
                                                    Remove
                                                </button>

                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <c:if test="${totalPages > 1}">
                                    <div class="pagination" style="text-align:center; margin-top: 20px;">
                                        <style>
                                            .pagination a, .pagination span {
                                                padding: 8px 12px;
                                                margin: 0 4px;
                                                border: 1px solid #ccc;
                                                text-decoration: none;
                                                color: #333;
                                                border-radius: 5px;
                                            }
                                            .pagination a:hover {
                                                background-color: #f0f0f0;
                                            }
                                            .pagination .active {
                                                background-color: #007bff;
                                                color: white;
                                                border-color: #007bff;
                                            }
                                        </style>

                                        <!-- Nút Prev -->
                                        <c:if test="${currentPage > 1}">
                                            <a href="favorite?action=getAll&page=${currentPage - 1}">&laquo; Prev</a>
                                        </c:if>

                                        <!-- Các nút số trang -->
                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <c:choose>
                                                <c:when test="${i == currentPage}">
                                                    <span class="active">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="favorite?action=getAll&page=${i}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <!-- Nút Next -->
                                        <c:if test="${currentPage < totalPages}">
                                            <a href="favorite?action=getAll&page=${currentPage + 1}">Next &raquo;</a>
                                        </c:if>
                                    </div>
                                </c:if>


                            </c:otherwise>
                        </c:choose>


                    </div>

                </div>
            </div>
        </section>
        <!-- Bootstrap Confirm Delete Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmDeleteModalLabel">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa tour này khỏi danh sách yêu thích?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <a id="confirmDeleteBtn" class="btn btn-danger" href="#">Xóa</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
        <%@include file="/WEB-INF/inclu/footer.jsp" %>
    </div>

    <script>
        const deleteModal = document.getElementById('confirmDeleteModal');
        const deleteBtn = document.getElementById('confirmDeleteBtn');

        deleteModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const favId = button.getAttribute('data-favid');

            // Cập nhật href của nút xác nhận xóa
            deleteBtn.href = '${pageContext.request.contextPath}/favorite?action=delete&favId=' + favId;
        });
    </script>


</body>
