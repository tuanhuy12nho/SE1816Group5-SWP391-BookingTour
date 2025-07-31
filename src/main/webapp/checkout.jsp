<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Cart, model.User, java.util.List, model.Tour, DAO.TourDAO, java.math.BigDecimal, model.Discount, java.util.Map" %>
<%
    List<Cart> carts = (List<Cart>) request.getAttribute("carts");
    User user = (User) session.getAttribute("user");
    TourDAO tourDAO = new TourDAO();
    List<Discount> savedDiscounts = (List<Discount>) request.getAttribute("savedDiscounts");
    Map<Integer, Tour> tourMap = (Map<Integer, Tour>) request.getAttribute("tourMap");
    
    // Get the first cart for backward compatibility with existing code
    Cart selectedCart = (Cart) request.getAttribute("selectedCart");
    Tour selectedTour = tourMap.get(selectedCart.getTourId());
    
    // Get prices for the selected cart
    BigDecimal adultPrice = (BigDecimal) request.getAttribute("adultPrice");
    BigDecimal childPrice = (BigDecimal) request.getAttribute("childPrice");
    BigDecimal originalTotal = (BigDecimal) request.getAttribute("originalTotal");
    
    // Get total amounts for all selected carts
    BigDecimal totalAmount = (BigDecimal) request.getAttribute("totalAmount");
    Discount selectedDiscount = (Discount) request.getAttribute("selectedDiscount");
    BigDecimal discountAmount = (BigDecimal) request.getAttribute("discountAmount");
    BigDecimal finalAmount = (BigDecimal) request.getAttribute("finalAmount");
    
    boolean isMultipleItems = (carts != null && carts.size() > 1);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán</title>
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
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
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
        .multiple-items-badge {
            background-color: #4caf50;
            color: white;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 14px;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="container checkout-container">
        <!-- Nút Quay lại -->
        <a href="cart" class="btn btn-secondary mb-3">
            <i class="fas fa-arrow-left"></i> Quay lại giỏ hàng
        </a>
        <h2 class="mb-4">
            Thanh toán 
            <% if (isMultipleItems) { %>
                <span class="multiple-items-badge"><%= carts.size() %> tours đã chọn</span>
            <% } %>
        </h2>

        <!-- Thông tin tour được chọn -->
        <div class="tour-summary">
            <div class="row">
                <div class="col-md-3">
                    <img src="<%= selectedTour.getImageUrl() %>" alt="<%= selectedTour.getName() %>" class="img-fluid" />
                </div>
                <div class="col-md-6">
                    <h4><%= selectedTour.getName() %></h4>
                    <p><%= selectedTour.getDescription() %></p>
                    <p>Địa điểm: <%= selectedTour.getLocation() %></p>
                    <p>Phương tiện: <%= selectedTour.getTransport() %></p>
                    <p>Ngày bắt đầu: <%= selectedTour.getStartDate() %></p>
                    <p>Ngày kết thúc: <%= selectedTour.getEndDate() %></p>
                    <p>Số lượng người lớn: <%= selectedCart.getNumberOfPeople() %></p>
                    <% if (selectedCart.getNumberOfChild() > 0) { %>
                        <p>Số lượng trẻ em: <%= selectedCart.getNumberOfChild() %> <span class="child-discount-badge">Giảm 50%</span></p>
                    <% } %>
                    <% if (isMultipleItems) { %>
                        <div class="alert alert-info mt-3">
                            <p>Đây là một trong <%= carts.size() %> tour được chọn. Tổng tiền sẽ bao gồm tất cả các tour đã chọn.</p>
                        </div>
                    <% } %>
                </div>
                <div class="col-md-3">
                    <p>Đơn giá: <%= selectedTour.getPrice() %> VNĐ</p>
                    <p>Người lớn: <%= adultPrice %> VNĐ</p>
                    <% if (selectedCart.getNumberOfChild() > 0) { %>
                        <p>Trẻ em: <%= childPrice %> VNĐ</p>
                    <% } %>
                    <p>Tổng: <%= originalTotal %> VNĐ</p>
                </div>
            </div>
        </div>

        <!-- Phần chọn mã giảm giá -->
        <form action="checkout" method="post">
            <% if (isMultipleItems) { %>
                <!-- For multiple items, we use the session data -->
                <input type="hidden" name="multipleItems" value="true">
            <% } else { %>
                <!-- For single item, we pass the cart ID -->
                <input type="hidden" name="cartId" value="<%= selectedCart.getId() %>">
            <% } %>
            
            <div class="discount-section">
                <h4>Chọn mã giảm giá cho người lớn</h4>
                <% if (savedDiscounts != null && !savedDiscounts.isEmpty()) { %>
                    <% for (Discount discount : savedDiscounts) { %>
                        <div class="discount-card" onclick="selectDiscount(this, '<%= discount.getId() %>')">
                            <div class="row">
                                <div class="col-md-9">
                                    <h5><%= discount.getCode() %></h5>
                                    <p><%= discount.getDescription() %></p>
                                    <p>Giảm giá: <%= discount.getDiscount_percent() %>%</p>
                                    <p>Hạn sử dụng: <%= discount.getEnd_date() %></p>
                                </div>
                                <div class="col-md-3 text-center">
                                    <input type="radio" name="discountId" value="<%= discount.getId() %>" style="transform: scale(1.5);" class="mt-3"
                                        <%= (selectedDiscount != null && discount.getId() == selectedDiscount.getId()) ? "checked" : "" %>>
                                </div>
                            </div>
                        </div>
                    <% } %>
                <% } else { %>
                    <p>Bạn chưa lưu mã giảm giá nào.</p>
                <% } %>
                
                <div class="discount-card mt-3" onclick="selectDiscount(this, '')">
                    <div class="row">
                        <div class="col-md-9">
                            <h5>Không sử dụng mã giảm giá</h5>
                        </div>
                        <div class="col-md-3 text-center">
                            <input type="radio" name="discountId" value="" style="transform: scale(1.5);" class="mt-3" 
                                <%= (selectedDiscount == null) ? "checked" : "" %>>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Phương thức thanh toán -->
            <div class="payment-method-section mt-4">
                <h4>Chọn phương thức thanh toán</h4>
                <div class="row mt-3">
                    <div class="col-md-6">
                        <div class="payment-card p-3 border rounded mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="cashPayment" value="Tiền mặt" checked="checked">
                                <label class="form-check-label" for="cashPayment">
                                    <strong>Tiền mặt</strong>
                                </label>
                                <p class="text-muted small mt-1">Thanh toán khi tham gia tour</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="payment-card p-3 border rounded mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="vnpayPayment" value="vnpay">
                                <label class="form-check-label" for="vnpayPayment">
                                    <strong>Thẻ ngân hàng</strong>
                                </label>
                                <p class="text-muted small mt-1">Thanh toán an toàn qua VnPay</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tổng giá trị thanh toán -->
            <div class="price-summary">
                <div class="row">
                    <div class="col-md-8">
                        <% if (isMultipleItems) { %>
                            <h4>Tổng tiền (<%= carts.size() %> tours): <span id="totalPrice"><%= totalAmount %></span> VNĐ</h4>
                            <% if (selectedDiscount != null) { %>
                                <p>Mã giảm giá: <%= selectedDiscount.getCode() %> (-<%= selectedDiscount.getDiscount_percent() %>%)</p>
                                <p>Giảm giá: <span id="discountAmount"><%= discountAmount %></span> VNĐ</p>
                                <h5>Thành tiền: <span id="finalPrice"><%= finalAmount %></span> VNĐ</h5>
                            <% } %>
                        <% } else { %>
                            <h4>Tổng tiền: <span id="totalPrice"><%= originalTotal %></span> VNĐ</h4>
                            <p id="discountInfo" style="display: <%= (selectedDiscount != null) ? "block" : "none" %>;">
                                Giảm giá: <span id="discountAmount"><%= (selectedDiscount != null) ? discountAmount : "0" %></span> VNĐ
                            </p>
                            <h5 id="finalPriceInfo" style="display: <%= (selectedDiscount != null) ? "block" : "none" %>;">
                                Thành tiền: <span id="finalPrice"><%= (selectedDiscount != null) ? finalAmount : originalTotal %></span> VNĐ
                            </h5>
                        <% } %>
                        <p class="text-muted mt-3">Lưu ý: Giảm giá voucher chỉ áp dụng cho người lớn. Trẻ em luôn được giảm 50%.</p>
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-success btn-lg">
                            <i class="fas fa-check"></i> Tiến hành đặt tour
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script>
        function selectDiscount(element, discountId) {
            // Remove selected class from all discount cards
            document.querySelectorAll('.discount-card').forEach(card => {
                card.classList.remove('selected');
            });
            
            // Add selected class to the clicked element
            element.classList.add('selected');
            
            // Select the corresponding radio button
            element.querySelector('input[type="radio"]').checked = true;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 