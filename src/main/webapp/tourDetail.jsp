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

            /* Review section */
            .reviews-section {
                margin: 40px 0;
                padding: 20px;
                background: #fafafa;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            }
            
            .reviews-section h3 {
                color: #003087;
                margin-bottom: 20px;
                border-bottom: 2px solid #ff6200;
                padding-bottom: 10px;
                display: inline-block;
            }
            
            .review {
                background: white;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                border-left: 4px solid #003087;
            }
            
            .review .username {
                font-weight: bold;
                color: #003087;
            }
            
            .review .rating {
                color: #ff9800;
                font-size: 18px;
            }
            
            .review .comment {
                margin-top: 10px;
                color: #333;
            }
            
            .review .date {
                color: #999;
                font-size: 12px;
                text-align: right;
                margin-top: 10px;
                margin-bottom: 0;
            }
            
            .review .actions {
                text-align: right;
                margin-top: 5px;
            }
            
            /* Add review form */
            .add-review-form {
                margin: 40px 0;
                padding: 25px;
                background: #f0f7ff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            }
            
            .add-review-form h3 {
                color: #003087;
                margin-bottom: 20px;
                border-bottom: 2px solid #ff6200;
                padding-bottom: 10px;
                display: inline-block;
            }

            /* Star Rating System */
            .star-rating {
                display: flex;
                flex-direction: row-reverse;
                justify-content: flex-end;
                margin-bottom: 20px;
            }

            .star-rating input {
                display: none;
            }

            .star-rating label {
                cursor: pointer;
                font-size: 30px;
                color: #ddd;
                margin: 0 2px;
            }

            .star-rating input:checked ~ label,
            .star-rating label:hover,
            .star-rating label:hover ~ label {
                color: #ff9800;
            }
            
            .star-rating input:checked + label:hover,
            .star-rating input:checked ~ label:hover,
            .star-rating label:hover ~ input:checked ~ label,
            .star-rating input:checked ~ label:hover ~ label {
                color: #ffb74d;
            }
            
            .star-rating-display {
                color: #ff9800;
                font-size: 20px;
                margin-left: 10px;
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
                    <!--Hien-1-->
                    <c:if test="${not empty favorite}">
                        <c:choose>
                            <c:when test="${favorite == 'true'}">
                                <!-- Tour đã được yêu thích -->
                                <button class="btn-favorite" data-tourid="${tour.id}" data-favorited="true"
                                        style="background-color: transparent; border: none;">
                                    <i class="fa-solid fa-heart"
                                       style="color: red; font-size: 20px; border: 1px solid red; padding: 5px; border-radius: 50%;">
                                    </i>
                                </button>
                            </c:when>
                            <c:when test="${favorite == 'false'}">
                                <!-- Tour chưa được yêu thích -->
                                <button class="btn-favorite" data-tourid="${tour.id}" data-favorited="false"
                                        style="background-color: transparent; border: none;">
                                    <i class="fa-solid fa-heart"
                                       style="color: gray; font-size: 20px; border: 1px solid gray; padding: 5px; border-radius: 50%;">
                                    </i>
                                </button>
                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <!--Hien-->
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
                    <div class="container">
                        <h2>Lịch Trình Chi Tiết</h2>
                        <% if (error != null) {%>
                        <p class="text-danger"><%= error%></p>
                        <% } else if (itineraries != null && !itineraries.isEmpty()) { %>
                        <ul class="list-group">
                            <% for (Itinerary i : itineraries) {%>
                            <li class="list-group-item">
                                <strong>Ngày <%= i.getDayNumber()%>:</strong>
                                <%= (i.getDescription() != null) ? i.getDescription() : "Chưa có mô tả"%>
                            </li>
                            <% } %>
                        </ul>
                        <% } else { %>
                        <p class="text-center">Chưa có lịch trình cho tour này.</p>
                        <% }%>
                    </div>

                    <!-- Nút -->
                    <div class="btn-container">
                        <a href="<%= request.getContextPath()%>/SortTour"
                           class="btn back-btn">Quay lại danh
                            sách</a>
<!--                        <a href="<%= request.getContextPath()%>/booking?action=inputTour&id=<%= tour.getId()%>"
                           class="btn btn-book-tour">Đặt Tour</a>-->
                        <button class="btn btn-add-cart"
                                onclick="addToCart('<%= tour.getId()%>')">
                            <i class="fas fa-shopping-cart"></i>
                            Thêm vào giỏ hàng
                        </button>
                        <%
                            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                    + request.getContextPath() + "/booking?action=viewTour&id=" + tour.getId();
                        %>
                        <a href="https://www.facebook.com/sharer/sharer.php?u=<%=url%>" target="_blank">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/2023_Facebook_icon.svg/120px-2023_Facebook_icon.svg.png" style="width: 50px" alt="Share on Facebook" />
                        </a>

                        <!-- Nút Zalo (mobile only) -->
                        <a href="https://zalo.me/share?url=<%=url%>" target="_blank">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Icon_of_Zalo.svg/75px-Icon_of_Zalo.svg.png"style="width: 50px" alt="Share on Zalo" />
                        </a>
                        <!--                        <button class="btn btn-share-tour"
                                                        onclick="shareTour('')">Chia
                                                    sẻ Tour</button>-->
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
                
                <!-- Show error message if any -->
                <% if (request.getAttribute("reviewError") != null) { %>
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <%= request.getAttribute("reviewError") %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% } %>
                
                <!-- Show success message if any -->
                <% if (request.getAttribute("reviewSuccess") != null) { %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <%= request.getAttribute("reviewSuccess") %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% } %>
                
                <form action="<%= request.getContextPath()%>/add-review"
                      method="post" class="needs-validation" novalidate>
                    <input type="hidden" name="tourId"
                           value="<%= tour.getId()%>" />
                    <div class="mb-3">
                        <label class="form-label">Đánh giá:</label>
                        <div class="star-rating">
                            <input type="radio" id="star5" name="rating" value="5" required />
                            <label for="star5" title="5 sao">★</label>
                            
                            <input type="radio" id="star4" name="rating" value="4" />
                            <label for="star4" title="4 sao">★</label>
                            
                            <input type="radio" id="star3" name="rating" value="3" />
                            <label for="star3" title="3 sao">★</label>
                            
                            <input type="radio" id="star2" name="rating" value="2" />
                            <label for="star2" title="2 sao">★</label>
                            
                            <input type="radio" id="star1" name="rating" value="1" />
                            <label for="star1" title="1 sao">★</label>
                        </div>
                        <div class="invalid-feedback">Vui lòng chọn đánh giá từ 1 đến 5 sao.</div>
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
                <% if (user == null) { %>
                    // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
                    window.location.href = '<%= request.getContextPath() %>/Login.jsp';
                <% } else { %>
                    // Mở modal để nhập số lượng người
                    document.getElementById('cartTourId').value = tourId;
                    var numberOfPeopleModal = new bootstrap.Modal(document.getElementById('numberOfPeopleModal'));
                    numberOfPeopleModal.show();
                <% } %>
            }

            // Star rating initialization
            document.addEventListener('DOMContentLoaded', function() {
                // Find star rating inputs
                var starInputs = document.querySelectorAll('.star-rating input');
                
                // Add click event to each star
                starInputs.forEach(function(input) {
                    input.addEventListener('change', function() {
                        // When a star is clicked, set all stars before it to checked
                        var value = this.value;
                        console.log('Selected rating: ' + value);
                    });
                });
                
                // Default to 5 stars when form loads
                if (document.getElementById('star5')) {
                    document.getElementById('star3').checked = true;
                }
            });
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
                                       class="form-label">Số lượng người lớn:</label>
                                <input type="number" class="form-control"
                                       id="numberOfPeople" name="numberOfPeople"
                                       min="1" max="20" value="1" required>
                                <small class="form-text text-muted">Tối đa 20 người lớn</small>
                            </div>
                            <div class="mb-3">
                                <label for="numberOfChild"
                                       class="form-label">Số lượng trẻ em (giảm 50%):</label>
                                <input type="number" class="form-control"
                                       id="numberOfChild" name="numberOfChild"
                                       min="0" max="10" value="0" required>
                                <small class="form-text text-muted">Tối đa 10 trẻ em</small>
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
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            document.querySelectorAll(".btn-favorite").forEach(button => {
                button.addEventListener("click", function () {
                    const tourId = this.getAttribute("data-tourid");
                    const isFavorited = this.getAttribute("data-favorited") === "true";
                    const icon = this.querySelector("i");
                    if (!tourId)
                        return;

                    const sendRequest = (action) => {
                        fetch("<%= request.getContextPath()%>/favorite", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/x-www-form-urlencoded"
                            },
                            body: 'action=' + action + '&tourId=' + tourId
                        })
                                .then(res => res.json())
                                .then(data => {
                                    // data.status = "success" | "warning" | "error"
                                    // data.message = chuỗi thông báo

                                    Swal.fire({
                                        icon: data.status,
                                        title: data.status === 'success' ? 'Thành công' :
                                                data.status === 'warning' ? 'Thông báo' : 'Lỗi',
                                        text: data.message,
                                        confirmButtonColor: '#3085d6',
                                        confirmButtonText: 'Đóng'
                                    });

                                    if (data.status === "success") {
                                        const newColor = action === "add" ? "red" : "gray";
                                        icon.style.color = newColor;
                                        icon.style.borderColor = newColor;
                                        button.setAttribute("data-favorited", action === "add" ? "true" : "false");
                                    }
                                })
                                .catch(err => {
                                    console.error(err);
                                    Swal.fire("Lỗi!", "Không thể xử lý yêu cầu.", "error");
                                });
                    };

                    if (isFavorited) {
                        // Nếu đã thích → Xác nhận hủy
                        Swal.fire({
                            title: "Bạn có chắc?",
                            text: "Bạn muốn hủy yêu thích tour này?",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#d33",
                            cancelButtonColor: "#3085d6",
                            confirmButtonText: "Có, hủy!",
                            cancelButtonText: "Không"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                sendRequest("remove");
                            }
                        });
                    } else {
                        // Nếu chưa thích → Thêm luôn
                        sendRequest("add");
                    }
                });
            });
        </script>
    </body>

</html>