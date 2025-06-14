<%@page import="java.math.BigDecimal" %>
<%@page import="model.Review" %>
<%@page import="java.util.List" %>
<%@page import="model.User" %>
<%@page import="model.Tour" %>
<%@page import="model.Itinerary" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<% Tour tour = (Tour) request.getAttribute("tour");
    User user = (User) request.getSession().getAttribute("user");
    String error = (String) request.getAttribute("error");
    List<Review> reviews = (List<Review>) request.getAttribute("reviews");
    List<Itinerary> itineraries = (List<Itinerary>) request.getAttribute("itineraries");
%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Chi Tiết Tour</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
            rel="stylesheet" />
        <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <style>
            /* Các style tương tự bạn đã cung cấp... */
            body {
                background-color: #f5f5f5;
                font-family: 'Arial', sans-serif;
            }

            .tour-container {
                padding-top: 20px;
                padding-bottom: 40px;
                background: linear-gradient(180deg, rgba(233, 240, 245, 0.8) 0%, #fff 100%);
                min-height: calc(100vh - 200px);
            }

            .tour-content {
                max-width: 1200px;
                margin: 0 auto;
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
                padding: 30px;
                transition: transform 0.3s ease;
            }

            .tour-content:hover {
                transform: translateY(-3px);
            }

            .tour-content h2 {
                color: #003087;
                font-size: 32px;
                font-weight: bold;
                text-align: center;
                margin-bottom: 30px;
                text-transform: uppercase;
                letter-spacing: 1px;
                position: relative;
            }

            .tour-content h2::after {
                content: '';
                width: 60px;
                height: 3px;
                background: #ff6200;
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
            }

            .tour-details {
                display: flex;
                flex-wrap: wrap;
                gap: 30px;
                margin-bottom: 20px;
            }

            .tour-details>div {
                flex: 1;
                min-width: 300px;
                background: #f9f9f9;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .tour-details>div:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            }

            .info-item {
                display: flex;
                justify-content: space-between;
                padding: 10px 0;
                border-bottom: 1px solid #eee;
                font-size: 15px;
                color: #555;
            }

            .info-item:last-child {
                border-bottom: none;
            }

            .info-item strong {
                color: #003087;
                font-weight: 600;
            }

            .info-item img {
                max-width: 100%;
                height: auto;
                border-radius: 5px;
                margin-top: 10px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .description {
                background: #f9f9f9;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .description:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            }

            .btn-container {
                text-align: center;
                margin-top: 30px;
            }

            .btn {
                display: inline-block;
                padding: 12px 25px;
                border-radius: 25px;
                text-decoration: none;
                font-weight: bold;
                font-size: 15px;
                transition: background-color 0.3s ease, transform 0.2s ease;
                margin: 0 10px;
                color: #fff;
            }

            .btn:hover {
                transform: translateY(-2px);
            }

            .btn.back-btn {
                background-color: #007bff;
            }

            .btn.back-btn:hover {
                background-color: #0056b3;
            }

            .btn.btn-book-tour {
                background-color: #ff6200;
            }

            .btn.btn-book-tour:hover {
                background-color: #e55a00;
            }

            .btn.btn-share-tour {
                background-color: #28a745;
            }

            .btn.btn-share-tour:hover {
                background-color: #218838;
            }

            .btn.btn-add-cart {
                background-color: #f39c12;
            }

            .btn.btn-add-cart:hover {
                background-color: #e67e22;
            }

            .error-message {
                color: #e74c3c;
                text-align: center;
                font-size: 18px;
                padding: 15px;
                background: #ffebee;
                border-radius: 5px;
                margin: 20px 0;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            }

            .reviews-section,
            .itinerary-section,
            .add-review-form {
                margin-top: 30px;
                padding: 20px;
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .reviews-section h3,
            .itinerary-section h3,
            .add-review-form h3 {
                text-align: center;
                color: #003087;
                font-size: 24px;
                margin-bottom: 20px;
            }

            .review {
                padding: 15px;
                border-bottom: 1px solid #dee2e6;
                margin-bottom: 15px;
            }

            .review:last-child {
                border-bottom: none;
            }

            .review .username {
                font-weight: bold;
                color: #000;
            }

            .review .rating {
                color: #ffc107;
                font-size: 18px;
                margin-left: 10px;
            }

            .review .comment {
                margin-top: 5px;
                color: #000;
                font-size: 16px;
            }

            .review .date {
                font-size: 14px;
                color: #999;
                margin-top: 5px;
            }

            .actions {
                margin-top: 10px;
            }

            .list-group-item {
                font-size: 16px;
            }
        </style>
    </head>

    <body class="changeHeader">
        <div id="wrap">
            <%@include file="/WEB-INF/inclu/head.jsp" %>

            <div class="tour-container">
                <div class="tour-content">
                    <% if (error != null) {%>
                    <p class="error-message">
                        <%= error%>
                    </p>
                    <% } else if (tour != null) {%>
                    <h2>
                        <%= tour.getName() != null ? tour.getName()
                                                                                    : "Tên tour không có"%>
                    </h2>
                    <div class="tour-details">
                        <div>
                            <div class="info-item"><strong>Địa
                                    điểm:</strong> <span>
                                    <%= tour.getLocation() != null
                                                                                                ? tour.getLocation()
                                                                                                : "Chưa cập nhật"%>
                                </span></div>
                            <div class="info-item"><strong>Phương
                                    tiện:</strong> <span>
                                    <%= tour.getTransport() != null
                                                                                                ? tour.getTransport()
                                                                                                : "Chưa cập nhật"%>
                                </span></div>
                            <div class="info-item"><strong>Bắt
                                    đầu:</strong> <span>
                                    <%= tour.getStartDate() != null
                                                                                                ? tour.getStartDate()
                                                                                                : "Chưa cập nhật"%>
                                </span></div>
                            <div class="info-item"><strong>Kết
                                    thúc:</strong> <span>
                                    <%= tour.getEndDate() != null
                                                                                                ? tour.getEndDate()
                                                                                                : "Chưa cập nhật"%>
                                </span></div>
                            <div class="info-item">
                                <strong>Giá:</strong>
                                <span>
                                    <%= (tour.getPrice() != null
                                                                                                && tour.getPrice().compareTo(BigDecimal.ZERO)
                                                                                                != 0) ? tour.getPrice()
                                                                                                        + " VND" : "Chưa cập nhật"%>
                                </span>
                            </div>
                            <% if (tour.getImageUrl() != null
                                                                                            && !tour.getImageUrl().isEmpty()) {%>
                            <div class="info-item"><img
                                    src="<%= tour.getImageUrl()%>"
                                    alt="Tour Image"></div>
                                <% }%>
                        </div>
                        <div class="description">
                            <div class="info-item"><strong>Mô
                                    tả:</strong> <span>
                                    <%= tour.getDescription() != null
                                                                                                ? tour.getDescription()
                                                                                                : "Chưa có mô tả"%>
                                </span></div>
                        </div>
                    </div>

                    <!-- Lịch trình tour -->
                    <div class="itinerary-section">
                        <h3>Lịch Trình Chi Tiết</h3>
                        <% if (itineraries != null
                                                                                        && !itineraries.isEmpty()) { %>
                        <ul class="list-group">
                            <% for (Itinerary i : itineraries) {
                            %>
                            <li class="list-group-item">
                                <strong>Ngày <%=i.getDayNumber()%>
                                    :</strong>
                                    <%= i.getDescription()
                                                                                                    != null
                                                                                                            ? i.getDescription()
                                                                                                            : "Chưa có mô tả"%>
                            </li>
                            <% } %>
                        </ul>
                        <% } else { %>
                        <p class="text-center">Chưa có lịch
                            trình cho tour này.</p>
                            <% }%>
                    </div>

                    <!-- Nút -->
                    <div class="btn-container">
                        <a href="<%= request.getContextPath()%>/SortTour"
                           class="btn back-btn">Quay lại danh
                            sách</a>
                        <a href="<%= request.getContextPath()%>/booking?action=inputTour&id=<%= tour.getId()%>"
                           class="btn btn-book-tour">Đặt Tour</a>
                        <button class="btn btn-add-cart"
                                onclick="addToCart('<%= tour.getId()%>')">
                            <i class="fas fa-shopping-cart"></i>
                            Thêm vào giỏ hàng
                        </button>
                        <button class="btn btn-share-tour"
                                onclick="shareTour('<%= request.getContextPath()%>/booking?action=viewTour&id=<%= tour.getId()%>')">Chia
                            sẻ Tour</button>
                    </div>
                    <% } else { %>
                    <p class="error-message">Không tìm thấy tour
                        nào.</p>
                        <% } %>
                </div>
            </div>

            <!-- Phần đánh giá khách hàng -->
            <div class="reviews-section">
                <h3>Đánh giá của khách hàng</h3>
                <% if (reviews != null && !reviews.isEmpty()) { %>
                <% for (Review review : reviews) {%>
                <div class="review">
                    <p>
                        <span class="username">
                            <%= review.getUsername()%>
                        </span> -
                        <span class="rating">
                            <% for (int i = 0; i < review.getRating();
                                                                                            i++) { %>★<% } %>
                            <% for (int i = review.getRating(); i < 5;
                                                                                                    i++) { %>☆<% }%>
                        </span>
                    </p>
                    <p class="comment">
                        <%= review.getComment()%>
                    </p>
                            <p class="date"><small>Ngày: <%=review.getReviewDate()%></small>
                    </p>
                    <% if (user != null
                                                                                    && user.getId() == review.getUserId()) {%>
                    <div class="actions">
                        <form
                            action="<%= request.getContextPath()%>/booking"
                            method="post">
                            <input type="hidden" name="action"
                                   value="deleteReview" />
                            <input type="hidden" name="reviewId"
                                   value="<%= review.getId()%>" />
                            <input type="hidden" name="tourId"
                                   value="<%= tour.getId()%>" />
                            <button type="submit"
                                    class="text-danger"
                                    style="background:none; border:none; padding:0; cursor:pointer;">Xóa</button>
                        </form>
                    </div>
                    <% } %>
                </div>
                <% } %>
                <% } else { %>
                <p class="text-center">Chưa có đánh giá nào
                    cho tour này.</p>
                    <% } %>
            </div>

            <!-- Form thêm đánh giá -->
            <% if (user != null && tour != null) {%>
            <div class="add-review-form">
                <h3>Thêm đánh giá của bạn</h3>
                <form action="<%= request.getContextPath()%>/booking"
                      method="post" class="needs-validation" novalidate>
                    <input type="hidden" name="action"
                           value="addReview" />
                    <input type="hidden" name="tourId"
                           value="<%= tour.getId()%>" />
                    <div class="mb-3">
                        <label for="rating" class="form-label">Đánh giá
                            (1-5):</label>
                        <input type="number" id="rating" name="rating"
                               min="1" max="5" required
                               class="form-control"
                               placeholder="Nhập số từ 1 đến 5" />
                        <div class="invalid-feedback">Vui lòng nhập số
                            từ 1 đến 5.</div>
                    </div>
                    <div class="mb-3">
                        <label for="comment" class="form-label">Bình
                            luận:</label>
                        <textarea id="comment" name="comment" required
                                  class="form-control" rows="3"
                                  placeholder="Nhập bình luận của bạn"></textarea>
                        <div class="invalid-feedback">Vui lòng nhập bình
                            luận.</div>
                    </div>
                    <button type="submit" class="btn btn-success">Gửi
                        đánh giá</button>
                </form>
            </div>
            <% } else {%>
            <p class="text-center">Bạn cần <a
                    href="<%= request.getContextPath()%>/login"
                    class="text-primary">đăng nhập</a> để đánh giá
                tour.</p>
                <% } %>

            <%@include file="/WEB-INF/inclu/footer.jsp" %>
        </div>

        <script>
            function shareTour(url) {
                navigator.clipboard.writeText(url).then(() => {
                    alert("Đã sao chép liên kết tour vào clipboard: " + url);
                }).catch(err => {
                    console.error('Lỗi khi sao chép: ', err);
                    alert("Không thể sao chép liên kết, vui lòng thử lại!");
                });
            }

            (function () {
                'use strict';
                var forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            })();

            // Hàm thêm vào giỏ hàng
            function addToCart(tourId) {
            <% if (user == null) {%>
                // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
                window.location.href = '<%= request.getContextPath()%>/Login.jsp';
            <% } else { %>
                // Mở modal để nhập số lượng người
                document.getElementById('cartTourId').value = tourId;
                var numberOfPeopleModal = new bootstrap.Modal(document.getElementById('numberOfPeopleModal'));
                numberOfPeopleModal.show();
            <% }%>
            }
        </script>

        <!-- Modal để nhập số lượng người -->
        <div class="modal fade" id="numberOfPeopleModal" tabindex="-1"
             aria-labelledby="numberOfPeopleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="numberOfPeopleModalLabel">
                            Thêm vào giỏ hàng</h5>
                        <button type="button" class="btn-close"
                                data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%= request.getContextPath()%>/cart"
                          method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="tourId" id="cartTourId">
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="numberOfPeople"
                                       class="form-label">Số lượng người:</label>
                                <input type="number" class="form-control"
                                       id="numberOfPeople" name="numberOfPeople"
                                       min="1" value="1" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary"
                                    data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Thêm
                                vào giỏ hàng</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>

</html>