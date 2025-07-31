<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Cart, model.User, java.util.List, model.Tour, DAO.TourDAO, java.math.BigDecimal, model.Discount, java.util.Map" %>
<%
    List<Cart> carts = (List<Cart>) request.getAttribute("carts");
    User user = (User) session.getAttribute("user");
    List<Discount> savedDiscounts = (List<Discount>) request.getAttribute("savedDiscounts");
    Map<Integer, Tour> tourMap = (Map<Integer, Tour>) request.getAttribute("tourMap");
    Map<Integer, BigDecimal> adultPrices = (Map<Integer, BigDecimal>) request.getAttribute("adultPrices");
    Map<Integer, BigDecimal> childPrices = (Map<Integer, BigDecimal>) request.getAttribute("childPrices");
    Map<Integer, BigDecimal> cartTotals = (Map<Integer, BigDecimal>) request.getAttribute("cartTotals");
    BigDecimal totalAmount = (BigDecimal) request.getAttribute("totalAmount");
    BigDecimal totalAdultPrice = (BigDecimal) request.getAttribute("totalAdultPrice");
    BigDecimal totalChildPrice = (BigDecimal) request.getAttribute("totalChildPrice");
    Discount selectedDiscount = (Discount) request.getAttribute("selectedDiscount");
    BigDecimal discountAmount = (BigDecimal) request.getAttribute("discountAmount");
    BigDecimal finalAmount = (BigDecimal) request.getAttribute("finalAmount");
    List<Integer> selectedItemIds = (List<Integer>) request.getAttribute("selectedItemIds");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán Nhiều Tour</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .checkout-container {
            padding: 20px;
        }
        .tour-summary {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f8f9fa;
        }
        .discount-section {
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .discount-card {
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #eee;
            border-radius: 5px;
            cursor: pointer;
        }
        .discount-card:hover {
            background-color: #f1f1f1;
        }
        .discount-card.selected {
            background-color: #e7f4e4;
            border: 1px solid #28a745;
        }
        .price-summary {
            margin-top: 20px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .child-discount-badge {
            background-color: #ffcc80;
            color: #7d5700;
            padding: 4px 8px;
            border-radius: 4px;
            display: inline-block;
            margin-top: 5px;
            font-size: 12px;
        }
        .discount-note {
            font-style: italic;
            color: #6c757d;
            font-size: 0.9rem;
            margin-top: 5px;
        }
        .discount-info {
            background-color: #e8f4ff;
            border: 1px dashed #b8daff;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .discount-applied {
            color: #28a745;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container checkout-container">
        <!-- Nút Quay lại -->
        <a href="cart" class="btn btn-secondary mb-3">
            <i class="fas fa-arrow-left"></i> Quay lại giỏ hàng
        </a>
        <h2 class="mb-4">Thanh toán các tour đã chọn</h2>

        <form action="checkout" method="post">
            <input type="hidden" name="multipleItems" value="true">
            
            <!-- Thông tin mã giảm giá đã áp dụng -->
            <% if (selectedDiscount != null) { %>
                <div class="discount-info mb-4">
                    <div class="row">
                        <div class="col-md-8">
                            <h5>Mã giảm giá đã áp dụng: <span class="discount-applied"><%= selectedDiscount.getCode() %></span></h5>
                            <p class="mb-0"><%= selectedDiscount.getDescription() %></p>
                            <p class="mb-0">Giảm <%= selectedDiscount.getDiscount_percent() %>% cho giá vé người lớn</p>
                            <p class="discount-note mb-0">Giảm giá chỉ áp dụng cho vé người lớn</p>
                        </div>
                        <div class="col-md-4 text-end">
                            <p class="mb-0">Tiết kiệm: <span class="text-success"><%= discountAmount %> VNĐ</span></p>
                        </div>
                    </div>
                </div>
            <% } %>
            
            <!-- Danh sách tour đã chọn -->
            <% if (carts != null && !carts.isEmpty()) { %>
                <% for (Cart cart : carts) { 
                    Tour tour = tourMap.get(cart.getTourId());
                    BigDecimal adultPrice = adultPrices.get(cart.getId());
                    BigDecimal childPrice = childPrices.get(cart.getId());
                    BigDecimal cartTotal = cartTotals.get(cart.getId());
                %>
                    <div class="tour-summary">
                        <div class="row">
                            <div class="col-md-3">
                                <img src="<%= tour.getImageUrl() %>" alt="<%= tour.getName() %>" class="img-fluid" />
                                <input type="hidden" name="selectedCarts" value="<%= cart.getId() %>">
                            </div>
                            <div class="col-md-6">
                                <h4><%= tour.getName() %></h4>
                                <p><%= tour.getDescription() %></p>
                                <p>Địa điểm: <%= tour.getLocation() %></p>
                                <p>Phương tiện: <%= tour.getTransport() %></p>
                                <p>Ngày bắt đầu: <%= tour.getStartDate() %></p>
                                <p>Ngày kết thúc: <%= tour.getEndDate() %></p>
                                <p>Người lớn: <%= cart.getNumberOfPeople() %> (đơn giá: <%= tour.getPrice() %> VNĐ)</p>
                                <% if (cart.getNumberOfChild() > 0) { %>
                                    <p>Trẻ em: <%= cart.getNumberOfChild() %> <span class="child-discount-badge">Giảm 50%</span></p>
                                <% } %>
                            </div>
                            <div class="col-md-3">
                                <div class="price-details">
                                    <p>Người lớn: <%= adultPrice %> VNĐ</p>
                                    <% if (cart.getNumberOfChild() > 0) { %>
                                        <p>Trẻ em: <%= childPrice %> VNĐ</p>
                                    <% } %>
                                    <h5>Tổng: <%= cartTotal %> VNĐ</h5>
                                </div>
                            </div>
                        </div>
                    </div>
                <% } %>

                <!-- Tổng giá trị thanh toán -->
                <div class="price-summary">
                    <div class="row">
                        <div class="col-md-8">
                            <div class="row mb-3">
                                <div class="col-md-6">Tổng giá vé người lớn:</div>
                                <div class="col-md-6 text-end"><%= totalAdultPrice %> VNĐ</div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">Tổng giá vé trẻ em (đã giảm 50%):</div>
                                <div class="col-md-6 text-end"><%= totalChildPrice %> VNĐ</div>
                            </div>
                            <% if (selectedDiscount != null) { %>
                                <div class="row mb-3">
                                    <div class="col-md-6">Giảm giá (mã <%= selectedDiscount.getCode() %>):</div>
                                    <div class="col-md-6 text-end text-success">-<%= discountAmount %> VNĐ</div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6"><strong>Tổng thanh toán:</strong></div>
                                    <div class="col-md-6 text-end"><strong><%= finalAmount %> VNĐ</strong></div>
                                </div>
                            <% } else { %>
                                <div class="row mb-3">
                                    <div class="col-md-6"><strong>Tổng thanh toán:</strong></div>
                                    <div class="col-md-6 text-end"><strong><%= totalAmount %> VNĐ</strong></div>
                                </div>
                            <% } %>
                            
                            <div class="mt-4">
                                <h5>Chọn phương thức thanh toán</h5>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="paymentCash" value="cash" checked>
                                    <label class="form-check-label" for="paymentCash">
                                        Thanh toán khi đến văn phòng
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="paymentVnpay" value="vnpay">
                                    <label class="form-check-label" for="paymentVnpay">
                                        Thanh toán qua VnPay
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 d-flex align-items-center justify-content-center">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-check"></i> Tiến hành đặt tour
                            </button>
                        </div>
                    </div>
                </div>
            <% } else { %>
                <div class="alert alert-warning">
                    <p>Không có tour nào để thanh toán.</p>
                    <a href="cart" class="btn btn-primary mt-2">Quay lại giỏ hàng</a>
                </div>
            <% } %>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 