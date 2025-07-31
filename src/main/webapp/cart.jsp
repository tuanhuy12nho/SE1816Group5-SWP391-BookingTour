<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Cart, model.User, java.util.List, model.Tour, DAO.TourDAO, java.math.BigDecimal, model.Discount, DAO.DiscountSaveDAO" %>
<%
    List<Cart> carts = (List<Cart>) request.getAttribute("carts");
    User user = (User) session.getAttribute("user");
    TourDAO tourDAO = new TourDAO();
    DiscountSaveDAO discountSaveDAO = new DiscountSaveDAO();
    List<Discount> savedDiscounts = null;
    if (user != null) {
        savedDiscounts = discountSaveDAO.getSavedVouchersByUser(user.getId(), "", null, null, 1, 20);
    }
    String cartMessage = (String) session.getAttribute("cartMessage");
    if (cartMessage != null) {
        session.removeAttribute("cartMessage");
    }
%>
<!DOCTYPE html>
<html>

    <head>
        <%@include file="/WEB-INF/inclu/header.jsp" %>
        <meta charset="UTF-8">

        <title>Giỏ Hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <style>
            .cart-container {
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
            }

            .cart-item {
                margin-bottom: 20px;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #fff;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
            }

            .cart-item.selected {
                border: 2px solid #28a745;
                background-color: #f8fff8;
            }

            .cart-item img {
                max-width: 100%;
                height: auto;
                border-radius: 5px;
                object-fit: cover;
            }

            .quantity-control {
                display: flex;
                flex-direction: column;
                gap: 15px;
                margin: 15px 0;
            }

            .quantity-row {
                display: flex;
                align-items: center;
                gap: 12px;
                margin-bottom: 5px;
            }

            .quantity-row label {
                min-width: 90px;
                margin-bottom: 0;
            }

            .quantity-input {
                width: 80px;
                padding: 5px;
                text-align: center;
            }

            .total-section {
                margin-top: 30px;
                padding: 25px;
                background-color: #f8f9fa;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .action-buttons {
                display: flex;
                flex-direction: column;
                gap: 10px;
                margin-top: 15px;
            }

            .action-buttons .btn {
                width: 100%;
            }

            .price-breakdown {
                font-size: 14px;
                color: #666;
                margin: 15px 0;
                padding: 10px;
                background-color: #f9f9f9;
                border-radius: 5px;
                border-left: 3px solid #28a745;
            }

            .price-breakdown p {
                margin-bottom: 5px;
            }

            .item-select {
                width: 22px;
                height: 22px;
                cursor: pointer;
                margin-right: 10px;
                accent-color: #28a745;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            }

            .empty-cart-message {
                text-align: center;
                padding: 40px 20px;
                background-color: #f9f9f9;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            }

            .empty-cart-message i {
                font-size: 50px;
                color: #6c757d;
                margin-bottom: 20px;
                display: block;
            }

            .empty-cart-message h4 {
                margin-bottom: 15px;
                color: #495057;
            }

            .empty-cart-message .btn {
                margin-top: 20px;
                padding: 10px 25px;
            }

            @media (max-width: 768px) {
                .cart-item .row {
                    flex-direction: column;
                }
                .cart-item .col-md-2,
                .cart-item .col-md-6,
                .cart-item .col-md-3,
                .cart-item .col-md-1 {
                    width: 100%;
                    text-align: center;
                    margin-bottom: 15px;
                }
                .quantity-row {
                    justify-content: center;
                }
                .action-buttons {
                    align-items: center;
                }
                .total-section .row {
                    flex-direction: column;
                }
                .total-section .col-md-4 {
                    margin-top: 25px;
                }
                .discount-section {
                    padding: 15px;
                }
                .price-breakdown {
                    text-align: left;
                }
            }

            .discount-radio {
                transform: scale(1.3);
                margin-right: 10px;
                accent-color: #28a745;
            }

            .discount-section {
                background-color: #f8f9fa;
                border-radius: 10px;
                border: 1px solid #e0e0e0;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            }

            .discount-card {
                padding: 15px;
                margin-bottom: 12px;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                cursor: pointer;
                transition: all 0.3s;
                background-color: white;
                height: 100%;
                display: flex;
                flex-direction: column;
            }

            .discount-card:hover {
                background-color: #f1f1f1;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }

            .discount-card.selected {
                background-color: #e7f4e4;
                border: 1px solid #28a745;
                box-shadow: 0 0 0 2px rgba(40, 167, 69, 0.2);
            }

            .discount-card .d-flex {
                flex-grow: 1;
            }

            .discount-card h6 {
                font-weight: 600;
                color: #333;
            }

            .discount-card p {
                color: #555;
                margin-bottom: 5px;
                font-size: 0.9rem;
            }

            .discount-card small {
                color: #28a745;
                display: block;
                margin-top: 5px;
            }

            .discount-card input[type="radio"] {
                transform: scale(1.3);
                accent-color: #28a745;
            }
            
            .no-discount-option {
                background-color: #f8f9fa;
                border: 1px solid #dee2e6;
                margin-bottom: 15px;
                position: relative;
            }
            
            .no-discount-option:after {
                content: '';
                position: absolute;
                bottom: -8px;
                left: 0;
                width: 100%;
                height: 1px;
                background-color: #dee2e6;
            }

            .child-discount-badge {
                background-color: #ffcc80;
                color: #7d5700;
                padding: 4px 8px;
                border-radius: 4px;
                display: inline-block;
                font-size: 12px;
                margin-left: 10px;
            }

            @media (max-width: 768px) {
                .cart-item .row {
                    flex-direction: column;
                }
                .cart-item .col-md-2,
                .cart-item .col-md-6,
                .cart-item .col-md-3,
                .cart-item .col-md-1 {
                    width: 100%;
                    text-align: center;
                    margin-bottom: 15px;
                }
                .quantity-row {
                    justify-content: center;
                }
                .action-buttons {
                    align-items: center;
                }
            }

            .discount-divider {
                height: 1px;
                background-color: #dee2e6;
                margin: 15px 0;
                position: relative;
            }
            
            .discount-divider:after {
                content: 'Mã giảm giá có sẵn';
                position: absolute;
                top: -10px;
                left: 20px;
                background-color: #f8f9fa;
                padding: 0 10px;
                font-size: 12px;
                color: #6c757d;
            }
        </style>

    </head>

    <body>
        <div class="container cart-container">

            <!-- Nút Quay lại -->
            <a href="SortTour" class="btn btn-secondary mb-3">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
            <h2 class="mb-4">Giỏ hàng của bạn</h2>

            <% if (cartMessage != null) {%>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= cartMessage%>
                <button type="button" class="btn-close" data-bs-dismiss="alert"
                        aria-label="Close"></button>
            </div>
            <% } %>

            <% if (carts != null && !carts.isEmpty()) { %>
            <div id="cartItems">
                <% for (Cart cart : carts) {
                        Tour tour = null;
                        try {
                            tour = tourDAO.getTourById(cart.getTourId());
                        } catch (Exception e) {
                            System.out.println("Error getting tour with ID: " + cart.getTourId() + " - " + e.getMessage());
                        }
                        String imgSrc = (tour != null && tour.getImageUrl() != null) ? tour.getImageUrl() : " images/"
                                + cart.getTourImage();%>
                <div class="cart-item" data-cart-id="<%= cart.getId()%>" data-tour-id="<%= cart.getTourId()%>">
                    <div class="row align-items-center">
                        <div class="col-md-1 d-flex justify-content-center mb-2 mb-md-0">
                            <input type="checkbox" class="item-select form-check-input" 
                                   data-cart-id="<%= cart.getId()%>"
                                   data-tour-id="<%= cart.getTourId()%>"
                                   data-adults="<%= cart.getNumberOfPeople()%>"
                                   data-children="<%= cart.getNumberOfChild()%>"
                                   data-adult-price="<%= (tour != null) ? tour.getPrice() : BigDecimal.ZERO%>"
                                   data-total="<%= cart.getTotalPrice()%>">
                        </div>
                        <div class="col-md-2 text-center mb-3 mb-md-0">
                            <img src="<%= imgSrc%>"
                                 alt="<%= (tour != null) ? tour.getName() : "Tour Image"%>"
                                 class="img-fluid" style="max-height: 150px; object-fit: cover;" />
                        </div>
                        <div class="col-md-6">
                            <h4 class="mb-2">
                                <%= (tour != null) ? tour.getName() : "Tour #" + cart.getTourId()%>
                            </h4>
                            <p class="text-muted">
                                <%= (tour != null) ? (tour.getDescription().length() > 100 ? tour.getDescription().substring(0, 100) + "..." : tour.getDescription()) : "Mô tả tour không có sẵn"%>
                            </p>
                            <p class="fw-bold">Đơn giá: <%= (tour != null) ? tour.getPrice() : cart.getTotalPrice().divide(new BigDecimal(cart.getNumberOfPeople()))%> VNĐ</p>
                            
                            <form action="cart" method="post" class="quantity-control">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="cartId" value="<%= cart.getId()%>">
                                <div class="quantity-row">
                                    <label for="numberOfPeople<%= cart.getId()%>" class="fw-medium">Số người lớn:</label>
                                    <input type="number" name="numberOfPeople" id="numberOfPeople<%= cart.getId()%>"
                                           value="<%= cart.getNumberOfPeople()%>" min="1" max="20"
                                           class="form-control quantity-input">
                                </div>
                                <div class="quantity-row">
                                    <label for="numberOfChild<%= cart.getId()%>" class="fw-medium">Số trẻ em:</label>
                                    <input type="number" name="numberOfChild" id="numberOfChild<%= cart.getId()%>"
                                           value="<%= cart.getNumberOfChild()%>" min="0" max="10"
                                           class="form-control quantity-input">
                                    <span class="child-discount-badge">Giảm 50%</span>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm mt-2">Cập nhật</button>
                            </form>

                            <!-- Chi tiết giá -->
                            <div class="price-breakdown mt-3">
                                <p class="mb-1"><i class="fas fa-user"></i> Người lớn: <%= cart.getNumberOfPeople()%> x <%= tour.getPrice()%> = <%= tour.getPrice().multiply(new BigDecimal(cart.getNumberOfPeople()))%> VNĐ</p>
                                <% if (cart.getNumberOfChild() > 0) {%>
                                <p class="mb-1"><i class="fas fa-child"></i> Trẻ em: <%= cart.getNumberOfChild()%> x <%= tour.getPrice()%> x 50% = <%= tour.getPrice().multiply(new BigDecimal(cart.getNumberOfChild())).multiply(new BigDecimal("0.5"))%> VNĐ</p>
                                <% }%>
                            </div>
                        </div>
                        <div class="col-md-3 text-md-end text-center">
                            <p class="h5 fw-bold text-success">Tổng: <%= cart.getTotalPrice()%> VNĐ</p>
                            <div class="action-buttons">
                                <a href="cart?action=remove&id=<%= cart.getId()%>"
                                   class="btn btn-danger"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa tour này khỏi giỏ hàng?')">
                                    <i class="fas fa-trash"></i> Xóa
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>

            <div class="total-section">
                <div class="row">
                    <div class="col-md-8">
                        <h4 class="mb-3"><i class="fas fa-shopping-cart me-2"></i>Tổng giá trị giỏ hàng: <span id="cartTotal" class="text-success">0</span> VNĐ</h4>
                        <p class="text-muted"><i class="fas fa-info-circle me-1"></i> Chỉ tính các mục đã chọn</p>

                        <!-- Phần chọn mã giảm giá -->
                        <div class="discount-section mt-4">
                            <h5 class="mb-3"><i class="fas fa-tags me-2"></i>Chọn mã giảm giá</h5>
                            <p class="text-muted mb-3">Chọn một mã giảm giá để áp dụng cho các mục đã chọn (chỉ áp dụng cho vé người lớn)</p>

                            <!-- No discount option - always at the top, styled like a header -->
                            <div class="discount-card no-discount-option" data-id="" data-percent="0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0"><i class="fas fa-ban me-2"></i>Không sử dụng mã giảm giá</h6>
                                    </div>
                                    <div>
                                        <input type="radio" name="discount" class="discount-radio" value="" checked>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="discount-divider mb-3"></div>
                            
                            <!-- Available discounts in a card grid -->
                            <% if (savedDiscounts != null && !savedDiscounts.isEmpty()) { %>
                                <div class="row row-cols-1 row-cols-md-2 g-3">
                                    <% for (Discount discount : savedDiscounts) {%>
                                    <div class="col">
                                        <div class="discount-card discount-item h-100" 
                                             data-id="<%= discount.getId()%>"
                                             data-percent="<%= discount.getDiscount_percent()%>">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <h6 class="mb-1"><%= discount.getCode()%></h6>
                                                    <p class="mb-1"><%= discount.getDescription()%></p>
                                                    <small class="text-success"><i class="fas fa-percentage me-1"></i>Giảm <%= discount.getDiscount_percent()%>% | <i class="far fa-calendar-alt me-1"></i>Hết hạn: <%= discount.getEnd_date()%></small>
                                                </div>
                                                <div>
                                                    <input type="radio" name="discount" class="discount-radio" value="<%= discount.getId()%>" data-percent="<%= discount.getDiscount_percent()%>">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <div class="alert alert-info mt-3">
                                    <p class="mb-0"><i class="fas fa-info-circle me-2"></i>Bạn chưa lưu mã giảm giá nào</p>
                                </div>
                            <% } %>
                        </div>

                        <div id="discount-summary" class="mt-4" style="display: none;">
                            <div class="alert alert-success">
                                <p class="mb-1"><i class="fas fa-check-circle me-2"></i>Áp dụng mã giảm giá: <span id="discount-code" class="fw-bold">-</span></p>
                                <p class="mb-1"><i class="fas fa-minus-circle me-2"></i>Giảm: <span id="discount-amount" class="fw-bold">0</span> VNĐ</p>
                                <h5 class="mt-2"><i class="fas fa-money-bill-wave me-2"></i>Tổng sau khi giảm: <span id="final-total" class="text-success fw-bold">0</span> VNĐ</h5>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 d-flex align-items-center justify-content-center">
                        <form id="checkoutForm" action="booking" method="get" class="w-100">
                            <input type="hidden" name="action" value="inputTour">
                            <input type="hidden" name="id" id="selectedTourId">
                            <button type="submit" id="checkoutSelectedBtn" class="btn btn-lg btn-success disabled w-100 p-3 shadow">
                                <i class="fas fa-shopping-cart me-2"></i> Thanh toán đã chọn
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <% } else { %>
            <div class="empty-cart-message">
                <i class="fas fa-shopping-cart"></i>
                <% if (request.getAttribute("cartEmpty") != null) {%>
                <h4><%= request.getAttribute("cartEmpty")%></h4>
                <% } else { %>
                <h4>Giỏ hàng của bạn đang trống</h4>
                <p class="text-muted">Hãy thêm các tour bạn yêu thích vào giỏ hàng để đặt!</p>
                <% } %>
                <a href="SortTour" class="btn btn-primary">
                    <i class="fas fa-search me-2"></i>Tiếp tục chọn tour
                </a>
            </div>
            <% }%>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <footer>
            <div class="footer clearfix">
                <div class="container">
                    <div class="row">
                        <div class="col-md-5 col-sm-4 col-xs-12">
                            <div class="info-sgt-box">
                                <div class="info-sgt-logo">
                                    <img class="image-info-footer" src="<%= request.getContextPath()%>/images/Logo_G3.png" alt="image commitment"/>
                                </div>
                                <div class="info-sgt-title">

                                </div>

                                <div class="info-sgt-contact">
                                    <p class="contact-phone">Tổng đài: <span class="number-phone">038 284 8074</span></p>
                                    <p class="contact-email">Email: info@g mail.net</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7 col-sm-8 col-xs-12">
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <div class="footerContent info-sgt-box">
                                        <h5>DỊCH VỤ</h5>
                                        <ul>
                                            <li><a href="#">Tour trong nước</a></li>
                                            <li><a href="#">Tour nước ngoài</a></li>
                                            <li><a href="#">Dịch vụ du lịch</a></li>
                                            <li><a href="#">Vé máy bay</a></li>
                                            <li><a href="#">Thuê xe</a></li>                                   
                                            <li><a href="#">Việc làm ngoài nước</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-xs-12">
                                    <div class="footerContent info-sgt-box">
                                        <h5>THÔNG TIN</h5>
                                        <ul>
                                            <li><a href="#">Tuyến điểm</a></li>
                                            <li><a href="#">Trách nhiệm xã hội</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="copyRight clearfix">
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-12 col-xs-12">
                                <div class="copyRightText">
                                    <p>Hello Everyone.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>

        <style>
            footer {
                background: #004a99; /* Blue background as in the screenshot */
                color: #fff;
                padding: 20px 0 0; /* Reduced padding to fit content */
                font-family: 'Arial', sans-serif;
                width: 100%;
            }

            .container {
                width: 90%;
                max-width: 1200px;
                margin: 0 auto;
            }

            .row {
                display: flex;
                flex-direction: row;
                flex-wrap: nowrap; /* Ensure content stays in one row */
                align-items: flex-start; /* Align items at the top */
                margin: 0 -15px;
                width: 100%;
            }

            .col-md-5, .col-md-7, .col-sm-6, .col-sm-12, .col-xs-12 {
                padding: 0 15px;
                box-sizing: border-box;
            }

            /* Adjust column widths for horizontal layout */
            .col-md-5 {
                flex: 0 0 35%; /* Adjusted width to fit content */
                max-width: 35%;
            }
            .col-md-7 {
                flex: 0 0 65%; /* Adjusted width to fit content */
                max-width: 65%;
                display: flex;
                flex-direction: row; /* Ensure inner row is also horizontal */
            }
            .col-sm-6 {
                flex: 0 0 50%; /* Each inner column takes half of col-md-7 */
                max-width: 50%;
            }
            .col-sm-12 {
                flex: 0 0 100%;
                max-width: 100%;
            }

            /* Responsive adjustments */
            @media (max-width: 768px) {
                .row {
                    flex-wrap: wrap; /* Stack vertically on smaller screens */
                }
                .col-md-5, .col-md-7, .col-sm-6 {
                    flex: 0 0 100%;
                    max-width: 100%;
                    margin-bottom: 20px;
                }
                .col-md-7 {
                    flex-direction: column; /* Stack inner columns vertically on small screens */
                }
            }

            .info-sgt-box {
                margin-bottom: 0; /* Remove bottom margin for horizontal alignment */
            }

            .info-sgt-logo, .info-sgt-image {
                margin-bottom: 10px;
            }

            .image-info-footer {
                max-width: 120px; /* Reduced size to fit horizontally */
                height: auto;
            }

            .info-sgt-title {
                font-size: 14px;
                line-height: 1.3;
                margin-bottom: 10px;
                color: #fff;
            }

            .info-sgt-contact p {
                margin: 5px 0;
                font-size: 12px;
            }

            .contact-phone .number-phone {
                font-weight: bold;
                color: #ffd700; /* Yellow for phone number */
            }

            .contact-email {
                color: #ddd;
            }

            .footerContent h5 {
                font-size: 14px;
                font-weight: bold;
                margin-bottom: 10px;
                text-transform: uppercase;
                border-bottom: 1px solid #fff;
                padding-bottom: 5px;
            }

            .footerContent ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .footerContent ul li {
                margin-bottom: 5px;
            }

            .footerContent ul li a {
                color: #ddd;
                text-decoration: none;
                font-size: 12px;
                transition: color 0.3s ease;
            }

            .footerContent ul li a:hover {
                color: #ffd700; /* Yellow on hover */
            }

            .copyRight {
                background: #003366; /* Darker blue for copyright section */
                padding: 10px 0;
                text-align: center;
                width: 100%;
                margin-top: 20px;
            }

            .copyRightText p {
                margin: 0;
                font-size: 12px;
                color: #ddd;
            }

            .clearfix::after {
                content: "";
                display: table;
                clear: both;
            }
        </style>
        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                const checkboxes = document.querySelectorAll('.item-select');
                                const cartTotal = document.getElementById('cartTotal');
                                const checkoutBtn = document.getElementById('checkoutSelectedBtn');
                                const discountCards = document.querySelectorAll('.discount-item');
                                let selectedDiscount = 0; // Default to no discount

                                // Debug output to show all cart items and their data attributes
                                console.log('Cart items found:', checkboxes.length);
                                checkboxes.forEach((checkbox, index) => {
                                    const cartId = checkbox.getAttribute('data-cart-id');
                                    const tourId = checkbox.getAttribute('data-tour-id');
                                    console.log(`Cart item #${index + 1}:`, {
                                        cartId: cartId,
                                        tourId: tourId,
                                        adults: checkbox.getAttribute('data-adults'),
                                        children: checkbox.getAttribute('data-children')
                                    });

                                    // Verify if data-tour-id is null or empty
                                    if (!tourId) {
                                        console.error('Missing tour ID for cart item:', cartId);

                                        // Try to find the tour ID from the parent element
                                        const parentItem = checkbox.closest('.cart-item');
                                        if (parentItem) {
                                            console.log('Parent item data:', {
                                                cartId: parentItem.getAttribute('data-cart-id'),
                                                tourId: parentItem.getAttribute('data-tour-id')
                                            });
                                        }
                                    }
                                });

                                // Add event listener to each checkbox
                                checkboxes.forEach(checkbox => {
                                    checkbox.addEventListener('change', function () {
                                        // Toggle selected class on parent cart-item
                                        const cartItem = this.closest('.cart-item');
                                        if (this.checked) {
                                            cartItem.classList.add('selected');
                                            // Show debug info
                                            console.log('Selected:', {
                                                cartId: this.getAttribute('data-cart-id'),
                                                tourId: this.getAttribute('data-tour-id')
                                            });
                                        } else {
                                            cartItem.classList.remove('selected');
                                            console.log('Deselected:', {
                                                cartId: this.getAttribute('data-cart-id'),
                                                tourId: this.getAttribute('data-tour-id')
                                            });
                                        }

                                        // Recalculate total
                                        calculateTotal();
                                    });
                                });

                                // Add click event listeners to discount cards
                                discountCards.forEach(card => {
                                    card.addEventListener('click', function () {
                                        // Get discount ID and percent
                                        const discountId = this.getAttribute('data-id');
                                        const discountPercent = parseFloat(this.getAttribute('data-percent'));

                                        // Remove selected class from all discount cards
                                        document.querySelectorAll('.discount-card').forEach(c => {
                                            c.classList.remove('selected');
                                        });

                                        // Add selected class to clicked card
                                        this.classList.add('selected');

                                        // Select radio button
                                        this.querySelector('input[type="radio"]').checked = true;

                                        // Update selected discount
                                        selectedDiscount = discountPercent;

                                        // Recalculate total
                                        calculateTotal();
                                    });
                                });

                                // Function to calculate total based on selected items
                                function calculateTotal() {
                                    let adultTotal = 0;
                                    let childTotal = 0;
                                    let selectedCount = 0;
                                    let discountAmount = 0;

                                    // Get selected discount percent
                                    const selectedRadio = document.querySelector('input[name="discount"]:checked');
                                    const discountPercent = selectedRadio ? parseFloat(selectedRadio.dataset.percent || 0) : 0;

                                    // Get all selected items
                                    const selectedItems = [];

                                    checkboxes.forEach(checkbox => {
                                        if (checkbox.checked) {
                                            selectedCount++;
                                            const cartId = checkbox.getAttribute('data-cart-id');
                                            const adults = parseInt(checkbox.getAttribute('data-adults'));
                                            const children = parseInt(checkbox.getAttribute('data-children'));
                                            const adultPrice = parseFloat(checkbox.getAttribute('data-adult-price'));

                                            // Store selected item data
                                            selectedItems.push({
                                                cartId: cartId,
                                                adults: adults,
                                                children: children,
                                                adultPrice: adultPrice
                                            });

                                            // Calculate adult price
                                            const currentAdultTotal = adults * adultPrice;
                                            adultTotal += currentAdultTotal;

                                            // Calculate child price (50% discount)
                                            const currentChildTotal = children * adultPrice * 0.5;
                                            childTotal += currentChildTotal;
                                        }
                                    });

                                    // Calculate discount (only for adults)
                                    if (discountPercent > 0) {
                                        discountAmount = adultTotal * (discountPercent / 100);
                                    }

                                    const subtotal = adultTotal + childTotal;
                                    const finalTotal = subtotal - discountAmount;

                                    // Update total display
                                    cartTotal.textContent = subtotal.toFixed(0);

                                    // Update discount display
                                    const discountSummary = document.getElementById('discount-summary');
                                    const discountCode = document.getElementById('discount-code');
                                    const discountAmountEl = document.getElementById('discount-amount');
                                    const finalTotalEl = document.getElementById('final-total');

                                    if (discountPercent > 0) {
                                        const selectedOption = selectedRadio.closest('.discount-card').querySelector('h6');
                                        discountCode.textContent = selectedOption.textContent;
                                        discountAmountEl.textContent = discountAmount.toFixed(0);
                                        finalTotalEl.textContent = finalTotal.toFixed(0);
                                        discountSummary.style.display = 'block';
                                    } else {
                                        discountSummary.style.display = 'none';
                                    }

                                    // Enable/disable checkout button
                                    if (selectedCount > 0) {
                                        checkoutBtn.classList.remove('disabled');
                                    } else {
                                        checkoutBtn.classList.add('disabled');
                                    }

                                    // Save selected items to cookie for later use
                                    saveSelectedItemsToCookie(selectedItems, selectedRadio ? selectedRadio.value : '');
                                }

                                // Function to save selected items to cookie
                                function saveSelectedItemsToCookie(selectedItems, discountId) {
                                    // Check if we have selected items
                                    if (!selectedItems || selectedItems.length === 0) {
                                        console.log('No selected items to save to cookie');
                                        return;
                                    }

                                    console.log('Saving to cookie:', {
                                        selectedItems: selectedItems,
                                        discountId: discountId
                                    });

                                    // Extract just the cart IDs as simple array
                                    const cartIds = selectedItems.map(item => item.cartId);

                                    // For simpler processing, save in a format that's easy to parse
                                    const cookieData = JSON.stringify({
                                        cartIds: cartIds,
                                        discountId: discountId
                                    });

                                    console.log('Cookie data:', cookieData);

                                    // Set the cookie with URL encoding
                                    document.cookie = "selectedCartItems=" + encodeURIComponent(cookieData) + "; path=/; max-age=3600";
                                    console.log('Cookie set successfully');
                                }

                                // Set up the checkout button
                                checkoutBtn.addEventListener('click', function (e) {
                                    e.preventDefault(); // Always prevent default form submission to handle it ourselves

                                    if (this.classList.contains('disabled')) {
                                        alert('Vui lòng chọn ít nhất một tour để thanh toán');
                                        return false;
                                    }

                                    // Get selected cart IDs
                                    const selectedItems = [];
                                    let selectedTourId = null;
                                    let selectedCartId = null;

                                    checkboxes.forEach(checkbox => {
                                        if (checkbox.checked) {
                                            const cartId = checkbox.getAttribute('data-cart-id');
                                            const tourId = checkbox.getAttribute('data-tour-id');
                                            const adults = checkbox.getAttribute('data-adults');
                                            const children = checkbox.getAttribute('data-children');

                                            selectedItems.push({
                                                cartId: cartId,
                                                tourId: tourId,
                                                adults: adults,
                                                children: children
                                            });

                                            // Store the first selected tour ID and cart ID
                                            if (!selectedTourId) {
                                                selectedTourId = tourId;
                                                selectedCartId = cartId;
                                            }
                                        }
                                    });

                                    // Check if at least one item is selected
                                    if (selectedItems.length < 1) {
                                        alert('Vui lòng chọn ít nhất một tour để thanh toán');
                                        return false;
                                    }

                                    console.log('Selected items:', selectedItems);
                                    console.log('Selected tour ID for form:', selectedTourId);

                                    // Get selected discount
                                    const selectedRadio = document.querySelector('input[name="discount"]:checked');
                                    const discountId = selectedRadio ? selectedRadio.value : '';

                                    if (selectedItems.length > 1) {
                                        // For multiple items, save to cookie and redirect to booking servlet with multiple flag
                                        saveSelectedItemsToCookie(selectedItems, discountId);
                                        window.location.href = 'booking?action=inputTour&multiple=true';
                                    } else if (selectedTourId) {
                                        // For single item, use the standard booking flow
                                        document.getElementById('selectedTourId').value = selectedTourId;
                                        console.log('Form tourId value set to:', document.getElementById('selectedTourId').value);

                                        // Submit the form properly
                                        document.getElementById('checkoutForm').submit();
                                    } else {
                                        alert('Không thể xác định ID của tour. Vui lòng thử lại.');
                                    }

                                    return false;
                                });
                            });
        </script>
    </body>

</html>