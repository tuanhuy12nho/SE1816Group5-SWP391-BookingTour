<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.Cart, model.User, java.util.List, model.Tour, DAO.TourDAO, java.math.BigDecimal" %>
        <% List<Cart> carts = (List<Cart>) request.getAttribute("carts");
                User user = (User) session.getAttribute("user");
                TourDAO tourDAO = new TourDAO();
                String cartMessage = (String) session.getAttribute("cartMessage");
                if (cartMessage != null) {
                session.removeAttribute("cartMessage");
                }
                %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Giỏ Hàng</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
                    <style>
                        .cart-container {
                            padding: 20px;
                        }

                        .cart-item {
                            margin-bottom: 20px;
                            padding: 15px;
                            border: 1px solid #ddd;
                            border-radius: 5px;
                        }

                        .cart-item img {
                            max-width: 200px;
                            height: auto;
                        }

                        .quantity-control {
                            display: flex;
                            align-items: center;
                            gap: 10px;
                        }

                        .total-section {
                            margin-top: 20px;
                            padding: 15px;
                            background-color: #f8f9fa;
                            border-radius: 5px;
                        }
                    </style>
                </head>

                <body>
                    <div class="container cart-container">

                        <!-- Nút Quay lại -->
                        <a href="List.jsp" class="btn btn-secondary mb-3">
                            <i class="fas fa-arrow-left"></i> Quay lại
                        </a>
                        <h2 class="mb-4">Giỏ hàng của bạn</h2>

                        <% if (cartMessage !=null) { %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <%= cartMessage %>
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                            </div>
                            <% } %>

                                <% if (carts !=null && !carts.isEmpty()) { %>
                                    <% BigDecimal cartTotal=BigDecimal.ZERO; %>
                                        <% for (Cart cart : carts) { Tour tour=null; try {
                                            tour=tourDAO.getTourById(cart.getTourId()); } catch (Exception e) {
                                            System.out.println("Error getting tour with ID: " + cart.getTourId() + " - " + e.getMessage());
                }
                String imgSrc = (tour != null && tour.getImageUrl() != null) ? tour.getImageUrl() : " images/" +
                                            cart.getTourImage(); %>
                                            <div class="cart-item">
                                                <div class="row">
                                                    <div class="col-md-3">
                                                        <img src="<%= imgSrc %>"
                                                            alt="<%= (tour != null) ? tour.getName() : " Tour Image" %>"
                                                        class="img-fluid" />
                                                    </div>
                                                    <div class="col-md-6">
                                                        <h4>
                                                            <%= (tour !=null) ? tour.getName() : "Tour #" +
                                                                cart.getTourId() %>
                                                        </h4>
                                                        <p>
                                                            <%= (tour !=null) ? tour.getDescription()
                                                                : "Mô tả tour không có sẵn" %>
                                                        </p>
                                                        <p>Đơn giá: <%= (tour !=null) ? tour.getPrice() :
                                                                cart.getTotalPrice().divide(new
                                                                BigDecimal(cart.getNumberOfPeople())) %> VNĐ</p>
                                                        <form action="cart" method="post" class="quantity-control">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="cartId"
                                                                value="<%= cart.getId() %>">
                                                            <input type="number" name="numberOfPeople"
                                                                value="<%= cart.getNumberOfPeople() %>" min="1"
                                                                class="form-control" style="width: 80px;">
                                                            <button type="submit" class="btn btn-primary">Cập
                                                                nhật</button>
                                                        </form>
                                                    </div>
                                                    <div class="col-md-3">
                                                        <p>Tổng: <%= cart.getTotalPrice() %> VNĐ</p>
                                                        <a href="cart?action=remove&id=<%= cart.getId() %>"
                                                            class="btn btn-danger"
                                                            onclick="return confirm('Bạn có chắc chắn muốn xóa tour này khỏi giỏ hàng?')">
                                                            <i class="fas fa-trash"></i> Xóa
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            <% cartTotal=cartTotal.add(cart.getTotalPrice()); %>
                                                <% } %>

                                                    <div class="total-section">
                                                        <div class="row">
                                                            <div class="col-md-9">
                                                                <h4>Tổng giá trị giỏ hàng: <%= cartTotal %> VNĐ</h4>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <a href="checkout" class="btn btn-success btn-lg">
                                                                    <i class="fas fa-shopping-cart"></i> Thanh toán
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <% } else { %>
                                                        <div class="alert alert-info">
                                                            <% if (request.getAttribute("cartEmpty") !=null) { %>
                                                                <%= request.getAttribute("cartEmpty") %>
                                                                    <% } else { %>
                                                                        Giỏ hàng của bạn đang trống.
                                                                        <% } %>
                                                                            <a href="List.jsp"
                                                                                class="btn btn-primary mt-2">Tiếp tục
                                                                                mua sắm</a>
                                                        </div>
                                                        <% } %>
                    </div>

                    <script
                        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
                </body>

                </html>