<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Tour"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="user" value="${sessionScope.user}"/>

<% User user = (User) session.getAttribute("user"); %>
<% Tour tour = (Tour) request.getAttribute("tour");%>

<script>
    function showStep(step) {
        let steps = ["selectPeople", "userInfo", "confirm"];
        if (!steps.includes(step))
            return;

        steps.forEach(s => {
            let element = document.getElementById(s);
            if (element) {
                element.style.display = (s === step) ? "block" : "none";
                if (s === step) {
                    element.classList.add('fade-in');
                } else {
                    element.classList.remove('fade-in');
                }
            }
        });

        if (step === "confirm") {
            saveBookingData();
        }
    }

    function saveBookingData() {
        let tourId = "<%= tour != null ? tour.getId() : ""%>";
        let userId = "${user.id}";
        let peopleCount = document.getElementById("peopleCount").value;

        console.log("People Count:", peopleCount);

        document.getElementById("selectedTourId").value = tourId;
        document.getElementById("userId").value = userId;
        document.getElementById("selectedPeopleCount").value = peopleCount;
    }

    function validateAndProceed() {
        let peopleCount = document.getElementById("peopleCount").value;
        let termsCheckbox = document.getElementById("termsCheckbox");
        let paymentCheckbox = document.getElementById("paymentCheckbox");

        if (!peopleCount || peopleCount < 1) {
            alert("Vui lòng nhập số lượng người hợp lệ (tối thiểu 1 người)!");
            return;
        }

        if (!termsCheckbox.checked) {
            alert("Bạn cần đồng ý với các điều khoản trước khi tiếp tục!");
            return;
        }



        saveBookingData();
        showStep('userInfo');
    }

    window.onload = function () {
        showStep('selectPeople');
    };
</script>

<%@include file="/WEB-INF/inclu/headbook.jsp" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Chi Tiết Tour</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<style>
    /* Giữ nguyên CSS cũ */
    .header-main {
        background-color: #003087;
        padding: 10px 0;
        width: 100%;
        z-index: 1000;
        position: relative;
    }
    .header-main .container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 15px;
    }
    .header-main .logo img {
        height: 40px;
        vertical-align: middle;
    }
    .header-main .main-menu ul {
        list-style: none;
        padding: 0;
        margin: 0;
        display: flex;
        align-items: center;
        gap: 20px;
    }
    .header-main .main-menu ul li a {
        color: #fff;
        text-decoration: none;
        font-size: 14px;
        font-weight: bold;
        text-transform: uppercase;
        padding: 8px 15px;
        border-radius: 5px;
        transition: background-color 0.3s ease;
    }
    .header-main .main-menu ul li a:hover {
        background-color: #002766;
    }
    .header {
        margin-bottom: 0;
    }
    body, #wrap {
        margin: 0;
        padding: 0;
    }
    .container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px 15px;
        margin-top: 0;
    }
    .card {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    .card-header {
        background-color: #f8f9fa;
        font-size: 1.25rem;
        padding: 15px;
        text-align: center;
    }
    .card-body {
        padding: 25px;
    }
    .text-center {
        display: flex;
        justify-content: center;
        gap: 10px;
    }
    .fade-in {
        animation: fadeIn 0.5s ease-in;
    }
    @keyframes fadeIn {
        from {
            opacity: 0;
        }
        to {
            opacity: 1;
        }
    }

    /* Sửa CSS để căn chỉnh các hàng trong userInfo */
    .user-info-row {
        display: flex;
        align-items: center;
        margin-bottom: 1.5rem;
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
    }
    .user-info-row .form-label {
        width: 150px; /* Chiều rộng cố định cho label */
        margin-right: 15px;
        font-weight: 500;
        color: #333;
    }
    .user-info-row .form-control,
    .user-info-row .form-select {
        flex: 1; /* Chiếm hết không gian còn lại */
        max-width: 100%;
    }

</style>




<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Chi Tiết Tour</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<body>
    <div class="container">
        <!-- Step 1: Select the number of people going -->
        <div id="selectPeople" style="display: none;">
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-users"></i> Chọn Số Người Đi
                </div>
                <div class="card-body">
                    <input type="hidden" name="userId" id="userId" value="${user.id}">
                    <div class="mb-4">
                        <label for="peopleCount" class="form-label"><i class="fas fa-user-plus"></i> Số lượng người:</label>
                        <input type="number" class="form-control" id="peopleCount" name="peopleCount" min="1" required>
                    </div>
                    <div class="form-check mb-4">
                        <input type="checkbox" class="form-check-input" id="termsCheckbox">
                        <label class="form-check-label" for="termsCheckbox">
                            Tôi đồng ý với các <a href="#" class="text-primary">điều khoản và điều kiện</a> <i class="fas fa-check-circle"></i>
                        </label>
                    </div>

                    <div class="text-center">
                        <a class="btn btn-secondary" href="<%= request.getContextPath()%>/booking?action=viewTour&id=<%= tour.getId()%>">
                            <i class="fas fa-arrow-left"></i> Quay lại
                        </a>
                        <button class="btn btn-primary" onclick="validateAndProceed()"><i class="fas fa-arrow-right"></i> Tiếp tục</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Step 2: Enter user information -->
        <div id="userInfo" style="display: none;">
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-user-edit"></i> Thông Tin Cá Nhân
                </div>
                <div class="card-body">
                    <div class="mb-4">
                        <label for="fullName" class="form-label"><i class="fas fa-user"></i> Họ và Tên</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" value="<%= (user != null) ? user.getFullName() : ""%>">
                    </div>
                    <div class="mb-4">
                        <label for="email" class="form-label"><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= (user != null) ? user.getEmail() : ""%>">
                    </div>
                    <div class="mb-4">
                        <label for="phoneNumber" class="form-label"><i class="fas fa-phone"></i> Số Điện Thoại</label>
                        <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" value="<%= (user != null) ? user.getPhoneNumber() : ""%>">
                    </div>
                    <div class="mb-4">
                        <label for="dob" class="form-label"><i class="fas fa-calendar-alt"></i> Ngày Sinh</label>
                        <input type="date" class="form-control" id="dob" name="dob" value="<%= (user != null) ? user.getDate_of_birth() : ""%>">
                    </div>
                    <div class="mb-4">
                        <label for="gender" class="form-label"><i class="fas fa-venus-mars"></i> Giới Tính</label>
                        <select class="form-control" id="gender" name="gender">
                            <option value="Male" <%= user != null && "Male".equals(user.getGender()) ? "selected" : ""%>>Nam</option>
                            <option value="Female" <%= user != null && "Female".equals(user.getGender()) ? "selected" : ""%>>Nữ</option>
                            <option value="Other" <%= user != null && "Other".equals(user.getGender()) ? "selected" : ""%>>Khác</option>
                        </select>
                    </div>
                    <div class="mb-4">
                        <label for="address" class="form-label"><i class="fas fa-map-marker-alt"></i> Địa Chỉ</label>
                        <input type="text" class="form-control" id="address" name="address" value="<%= (user != null) ? user.getAddress() : ""%>">
                    </div>
                    <div class="mb-4">
                        <label for="country" class="form-label"><i class="fas fa-globe"></i> Quốc Gia</label>
                        <input type="text" class="form-control" id="country" name="country" value="<%= (user != null) ? user.getCountry() : ""%>">
                    </div>
                    <div class="text-center">
                        <button class="btn btn-secondary" onclick="showStep('selectPeople')"><i class="fas fa-arrow-left"></i> Quay lại</button>
                        <button class="btn btn-primary" onclick="showStep('confirm')"><i class="fas fa-arrow-right"></i> Tiếp tục</button>
                    </div>
                </div>
            </div>
        </div>


        <!--Step 3: Confirm tour booking -->
        <div id="confirm" style="display: none;">
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-check-circle"></i> Xác Nhận Đặt Tour
                </div>
                <div class="card-body">
                    <form action="booking" method="POST">
                        <input type="hidden" name="action" value="save">
                        <input type="hidden" name="tourId" id="selectedTourId">
                        <input type="hidden" name="peopleCount" id="selectedPeopleCount">
                        <input type="hidden" name="userId" id="userId" value="${user.id}">
                        <input type="hidden" name="status" value="pending">

                        <!-- Hide bookingDate field and assign current date -->
                        <input type="hidden" name="bookingDate" id="bookingDate">

                        <!-- Dropdown to select payment method-->
                        <div class="mb-4">
                            <label for="paymentMethod" class="form-label"><i class="fas fa-money-bill-wave"></i> Phương thức thanh toán:</label>
                            <select class="form-control" id="paymentMethod" name="pay" required>
                                <option value="" disabled selected>Chọn phương thức thanh toán</option>
                                <option value="Tiền mặt">Tiền mặt</option>
                                <option value="Thẻ ngân hàng">Thẻ ngân hàng</option>
                                <option value="Visa">Visa</option>
                            </select>
                        </div>

                        <div class="text-center">
                            <button class="btn btn-secondary" type="button" onclick="showStep('userInfo')"><i class="fas fa-arrow-left"></i> Quay lại</button>
                            <button class="btn btn-success" type="submit"><i class="fas fa-check"></i> Xác nhận đặt tour</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            document.getElementById("bookingDate").setAttribute("min", new Date().toISOString().split("T")[0]);
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<%@include file="/WEB-INF/inclu/footer.jsp" %>