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

                    <div style="max-height: 150px; overflow-y: auto; border: 1px solid #ddd; padding: 10px; background: #fff; margin: 10px">
                        <b>Điều khoản này là sự thoả thuận đồng ý của quý khách khi sử dụng dịch vụ thanh toán trên trang web BOOKINGTOUR của GROUP 5 và những trang web của bên thứ ba. Việc quý khách đánh dấu vào ô “Đồng ý” và nhấp chuột vào thanh “Chấp nhận” nghĩa là quý khách đồng ý tất cả các điều khoản thỏa thuận trong các trang web này.</b>
                        <br>
                        <b>Điều khoản: là những điều quy định giữa GROUP 5 và quý khách</b>
                        <br>
                        Vé điện tử: là những thông tin và hành trình của quý khách cho chuyến đi được thể hiện trên một trang giấy mà quý khách có thể in ra được                        <br>
                        <!-- Thêm nội dung điều khoản ở đây -->
                        Khi đăng ký thanh toán qua mạng, quý khách sẽ được yêu cầu cung cấp một số thông tin cá nhân và thông tin tài khoản.

                        Đối với thông tin cá nhân: Những thông tin này chỉ để phục vụ cho nhu cầu xác nhận sự mua dịch vụ của quý khách và sẽ hiển thị những nội dung cần thiết trên vé điện tử. GROUP 5 cũng sẽ sử dụng những thông tin liên lạc này để gửi đến quý khách những sự kiện, những tin tức khuyến mãi và những ưu đãi đặc biệt nếu quý khách đồng ý. Những thông tin này của quý khách sẽ được GROUP 5 bảo mật và không tiết lộ cho bên thứ ba biết ngoại trừ sự đồng ý của quý khách hoặc là phải tiết lộ theo sự tuân thủ luật pháp quy định.

                        Đối với thông tin tài khoản: Những thông tin này sẽ được GROUP 5 và bên thứ ba áp dụng những biện pháp bảo mật cao nhất do các hệ thống thanh toán nổi tiếng trên thế giới như Visa và MasterCard cung cấp nhằm đảm bảo sự an toàn tuyệt đối của thông tin tài khoản quý khách.
                    </div>
                    <div class="form-check mb-4" >
                        <input type="checkbox" class="form-check-input" id="termsCheckbox" style="margin: 8px;">
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
                    <div class="mb-4" >
                        <label for="country" class="form-label"><i class="fas fa-globe"></i> Quốc Gia</label>
                        <div style="background: #fff; border: 1px solid #ced4da; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); padding: 16px;">
                            <input type="text" id="countrySearch" class="form-control mb-2" placeholder="Tìm kiếm quốc gia..." onkeyup="filterCountryOptions()" style="border-radius: 6px; border: 1px solid #ced4da;">
                            <select class="form-control" id="country" name="country" required size="6" style="border-radius: 6px; border: 1px solid #ced4da;">
                                <option value="Việt Nam">Việt Nam</option>
                                <option value="Afghanistan">Afghanistan</option>
                                <option value="Albania">Albani</option>
                                <option value="Algeria">Algeria</option>
                                <option value="Andorra">Andorra</option>
                                <option value="Angola">Angola</option>
                                <option value="Argentina">Argentina</option>
                                <option value="Armenia">Armenia</option>
                                <option value="Australia">Úc</option>
                                <option value="Austria">Áo</option>
                                <option value="Azerbaijan">Azerbaijan</option>
                                <option value="Bahamas">Bahamas</option>
                                <option value="Bahrain">Bahrain</option>
                                <option value="Bangladesh">Bangladesh</option>
                                <option value="Belarus">Belarus</option>
                                <option value="Belgium">Bỉ</option>
                                <option value="Belize">Belize</option>
                                <option value="Benin">Benin</option>
                                <option value="Bhutan">Bhutan</option>
                                <option value="Bolivia">Bolivia</option>
                                <option value="Bosnia and Herzegovina">Bosnia và Herzegovina</option>
                                <option value="Botswana">Botswana</option>
                                <option value="Brazil">Brazil</option>
                                <option value="Brunei">Brunei</option>
                                <option value="Bulgaria">Bulgaria</option>
                                <option value="Burkina Faso">Burkina Faso</option>
                                <option value="Burundi">Burundi</option>
                                <option value="Cambodia">Campuchia</option>
                                <option value="Cameroon">Cameroon</option>
                                <option value="Canada">Canada</option>
                                <option value="Cape Verde">Cape Verde</option>
                                <option value="Central African Republic">Cộng hòa Trung Phi</option>
                                <option value="Chad">Tchad</option>
                                <option value="Chile">Chile</option>
                                <option value="China">Trung Quốc</option>
                                <option value="Colombia">Colombia</option>
                                <option value="Comoros">Comoros</option>
                                <option value="Congo">Congo</option>
                                <option value="Costa Rica">Costa Rica</option>
                                <option value="Croatia">Croatia</option>
                                <option value="Cuba">Cuba</option>
                                <option value="Cyprus">Síp</option>
                                <option value="Czech Republic">Séc</option>
                                <option value="Denmark">Đan Mạch</option>
                                <option value="Djibouti">Djibouti</option>
                                <option value="Dominica">Dominica</option>
                                <option value="Dominican Republic">Cộng hòa Dominica</option>
                                <option value="East Timor">Đông Timor</option>
                                <option value="Ecuador">Ecuador</option>
                                <option value="Egypt">Ai Cập</option>
                                <option value="El Salvador">El Salvador</option>
                                <option value="Equatorial Guinea">Guinea Xích Đạo</option>
                                <option value="Eritrea">Eritrea</option>
                                <option value="Estonia">Estonia</option>
                                <option value="Ethiopia">Ethiopia</option>
                                <option value="Fiji">Fiji</option>
                                <option value="Finland">Phần Lan</option>
                                <option value="France">Pháp</option>
                                <option value="Gabon">Gabon</option>
                                <option value="Gambia">Gambia</option>
                                <option value="Georgia">Georgia</option>
                                <option value="Germany">Đức</option>
                                <option value="Ghana">Ghana</option>
                                <option value="Greece">Hy Lạp</option>
                                <option value="Grenada">Grenada</option>
                                <option value="Guatemala">Guatemala</option>
                                <option value="Guinea">Guinea</option>
                                <option value="Guinea-Bissau">Guinea-Bissau</option>
                                <option value="Guyana">Guyana</option>
                                <option value="Haiti">Haiti</option>
                                <option value="Honduras">Honduras</option>
                                <option value="Hungary">Hungary</option>
                                <option value="Iceland">Iceland</option>
                                <option value="India">Ấn Độ</option>
                                <option value="Indonesia">Indonesia</option>
                                <option value="Iran">Iran</option>
                                <option value="Iraq">Iraq</option>
                                <option value="Ireland">Ireland</option>
                                <option value="Israel">Israel</option>
                                <option value="Italy">Ý</option>
                                <option value="Jamaica">Jamaica</option>
                                <option value="Japan">Nhật Bản</option>
                                <option value="Jordan">Jordan</option>
                                <option value="Kazakhstan">Kazakhstan</option>
                                <option value="Kenya">Kenya</option>
                                <option value="Kiribati">Kiribati</option>
                                <option value="Korea, North">Triều Tiên</option>
                                <option value="Korea, South">Hàn Quốc</option>
                                <option value="Kuwait">Kuwait</option>
                                <option value="Kyrgyzstan">Kyrgyzstan</option>
                                <option value="Laos">Lào</option>
                                <option value="Latvia">Latvia</option>
                                <option value="Lebanon">Liban</option>
                                <option value="Lesotho">Lesotho</option>
                                <option value="Liberia">Liberia</option>
                                <option value="Libya">Libya</option>
                                <option value="Liechtenstein">Liechtenstein</option>
                                <option value="Lithuania">Litva</option>
                                <option value="Luxembourg">Luxembourg</option>
                                <option value="Madagascar">Madagascar</option>
                                <option value="Malawi">Malawi</option>
                                <option value="Malaysia">Malaysia</option>
                                <option value="Maldives">Maldives</option>
                                <option value="Mali">Mali</option>
                                <option value="Malta">Malta</option>
                                <option value="Marshall Islands">Quần đảo Marshall</option>
                                <option value="Mauritania">Mauritania</option>
                                <option value="Mauritius">Mauritius</option>
                                <option value="Mexico">Mexico</option>
                                <option value="Micronesia">Micronesia</option>
                                <option value="Moldova">Moldova</option>
                                <option value="Monaco">Monaco</option>
                                <option value="Mongolia">Mông Cổ</option>
                                <option value="Montenegro">Montenegro</option>
                                <option value="Morocco">Ma-rốc</option>
                                <option value="Mozambique">Mozambique</option>
                                <option value="Myanmar">Myanmar</option>
                                <option value="Namibia">Namibia</option>
                                <option value="Nauru">Nauru</option>
                                <option value="Nepal">Nepal</option>
                                <option value="Netherlands">Hà Lan</option>
                                <option value="New Zealand">New Zealand</option>
                                <option value="Nicaragua">Nicaragua</option>
                                <option value="Niger">Niger</option>
                                <option value="Nigeria">Nigeria</option>
                                <option value="Norway">Na Uy</option>
                                <option value="Oman">Oman</option>
                                <option value="Pakistan">Pakistan</option>
                                <option value="Palau">Palau</option>
                                <option value="Panama">Panama</option>
                                <option value="Papua New Guinea">Papua New Guinea</option>
                                <option value="Paraguay">Paraguay</option>
                                <option value="Peru">Peru</option>
                                <option value="Philippines">Philippines</option>
                                <option value="Poland">Ba Lan</option>
                                <option value="Portugal">Bồ Đào Nha</option>
                                <option value="Qatar">Qatar</option>
                                <option value="Romania">Romania</option>
                                <option value="Russia">Nga</option>
                                <option value="Rwanda">Rwanda</option>
                                <option value="Saint Kitts and Nevis">Saint Kitts và Nevis</option>
                                <option value="Saint Lucia">Saint Lucia</option>
                                <option value="Saint Vincent and the Grenadines">Saint Vincent và Grenadines</option>
                                <option value="Samoa">Samoa</option>
                                <option value="San Marino">San Marino</option>
                                <option value="Sao Tome and Principe">Sao Tome và Principe</option>
                                <option value="Saudi Arabia">Ả Rập Xê Út</option>
                                <option value="Senegal">Senegal</option>
                                <option value="Serbia">Serbia</option>
                                <option value="Seychelles">Seychelles</option>
                                <option value="Sierra Leone">Sierra Leone</option>
                                <option value="Singapore">Singapore</option>
                                <option value="Slovakia">Slovakia</option>
                                <option value="Slovenia">Slovenia</option>
                                <option value="Solomon Islands">Quần đảo Solomon</option>
                                <option value="Somalia">Somalia</option>
                                <option value="South Africa">Nam Phi</option>
                                <option value="Spain">Tây Ban Nha</option>
                                <option value="Sri Lanka">Sri Lanka</option>
                                <option value="Sudan">Sudan</option>
                                <option value="Suriname">Suriname</option>
                                <option value="Swaziland">Swaziland</option>
                                <option value="Sweden">Thụy Điển</option>
                                <option value="Switzerland">Thụy Sĩ</option>
                                <option value="Syria">Syria</option>
                                <option value="Taiwan">Đài Loan</option>
                                <option value="Tajikistan">Tajikistan</option>
                                <option value="Tanzania">Tanzania</option>
                                <option value="Thailand">Thái Lan</option>
                                <option value="Togo">Togo</option>
                                <option value="Tonga">Tonga</option>
                                <option value="Trinidad and Tobago">Trinidad và Tobago</option>
                                <option value="Tunisia">Tunisia</option>
                                <option value="Turkey">Thổ Nhĩ Kỳ</option>
                                <option value="Turkmenistan">Turkmenistan</option>
                                <option value="Tuvalu">Tuvalu</option>
                                <option value="Uganda">Uganda</option>
                                <option value="Ukraine">Ukraina</option>
                                <option value="United Arab Emirates">Các Tiểu vương quốc Ả Rập Thống nhất</option>
                                <option value="United Kingdom">Anh</option>
                                <option value="United States">Mỹ</option>
                                <option value="Uruguay">Uruguay</option>
                                <option value="Uzbekistan">Uzbekistan</option>
                                <option value="Vanuatu">Vanuatu</option>
                                <option value="Vatican City">Vatican</option>
                                <option value="Venezuela">Venezuela</option>
                                <option value="Yemen">Yemen</option>
                                <option value="Zambia">Zambia</option>
                                <option value="Zimbabwe">Zimbabwe</option>
                            </select>
                                                    </div>

                            <script>
                                function filterCountryOptions() {
                                    var input = document.getElementById('countrySearch');
                                    var filter = input.value.toLowerCase();
                                    var select = document.getElementById('country');
                                    var found = false;
                                    for (var i = 0; i < select.options.length; i++) {
                                        var txt = select.options[i].text.toLowerCase();
                                        // Luôn hiển thị Việt Nam
                                        if (select.options[i].value === 'Việt Nam') {
                                            select.options[i].hidden = false;
                                            continue;
                                        }
                                        if (txt.indexOf(filter) > -1) {
                                            select.options[i].hidden = false;
                                            found = true;
                                        } else {
                                            select.options[i].hidden = true;
                                        }
                                    }
                                    // Nếu không tìm thấy quốc gia nào, có thể thêm option "Không tìm thấy quốc gia"
                                }
                            </script>                    </div>
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