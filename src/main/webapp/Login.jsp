<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <!-- Các thẻ meta và CSS giữ nguyên -->
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>BookingTour - Đăng nhập</title>
            <meta property="og:type" content="login" />
            <meta property="og:title" content="Saigontourist - Đăng nhập" />
            <meta property="og:description" content="Saigontourist - Đăng nhập" />

            <!-- Bootstrap CSS (nếu bạn chưa thêm) -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <!-- css -->
            <!-- PLUGINS CSS STYLE -->
            <link href="./assets/plugins/jquery-ui/jquery-ui.css" rel="stylesheet">
            <link href="./assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link href="./assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet">
            <link href="./assets/vendors/bootstrap/dist/css/bootstrap-social.css" rel="stylesheet">
            <link href="./assets/vendors/select2/dist/css/select2.css" rel="stylesheet">

            <link href="./assets/slick/slick.css" rel="stylesheet">
            <link href="./assets/slick/slick-theme.css" rel="stylesheet">

            <link rel="stylesheet" type="text/css" href="./assets/plugins/rs-plugin/css/settings.css" media="screen">
            <link rel="stylesheet" type="text/css" href="./assets/plugins/selectbox/select_option1.css">
            <link rel="stylesheet" type="text/css" href="./assets/plugins/datepicker/datepicker.css">
            <link rel="stylesheet" type="text/css" href="./assets/plugins/isotope/jquery.fancybox.css">
            <link rel="stylesheet" type="text/css" href="./assets/plugins/isotope/isotope.css">

            <link
                href="https://fonts.googleapis.com/css?family=Montserrat:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&amp;subset=vietnamese"
                rel="stylesheet">

            <!-- Custom CSS -->
            <style>
                /* Member Wrapper */
                .member-wrapper {
                    position: relative;
                    min-height: 100vh;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background: url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80') no-repeat center center fixed;
                    background-size: cover;
                }

                .member-wrapper .background {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.5);
                    /* Lớp phủ mờ để làm nổi bật form */
                    z-index: 1;
                }

                .member-content {
                    position: relative;
                    z-index: 2;
                    max-width: 400px;
                    /* Kích thước form hợp lý */
                    width: 100%;
                    background: #ffffff;
                    padding: 30px;
                    border-radius: 10px;
                    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                    text-align: center;
                }

                /* Login Form */
                .login .title {
                    font-size: 28px;
                    font-weight: 700;
                    color: #333;
                    margin-bottom: 25px;
                    font-family: 'Montserrat', sans-serif;
                }

                .login-form {
                    width: 100%;
                }

                .form-group {
                    margin-bottom: 20px;
                    text-align: left;
                }

                .form-group label {
                    font-size: 14px;
                    font-weight: 500;
                    color: #444;
                    margin-bottom: 8px;
                    display: block;
                }

                .form-control {
                    width: 100%;
                    padding: 12px 15px;
                    font-size: 14px;
                    border: 1px solid #ced4da;
                    border-radius: 5px;
                    transition: border-color 0.3s ease, box-shadow 0.3s ease;
                }

                .form-control:focus {
                    border-color: #007bff;
                    box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
                    outline: none;
                }

                .form-control::placeholder {
                    color: #999;
                }

                /* Alert */
                .alert-danger {
                    font-size: 14px;
                    padding: 10px;
                    margin-bottom: 20px;
                    border-radius: 5px;
                }

                .text-danger {
                    font-size: 12px;
                    color: #dc3545;
                    margin-top: 5px;
                    display: block;
                }

                /* Buttons */
                .btn-primary {
                    background-color: #007bff;
                    border: none;
                    padding: 12px;
                    font-size: 16px;
                    font-weight: 500;
                    border-radius: 5px;
                    width: 100%;
                    transition: background-color 0.3s ease;
                }

                .btn-primary:hover {
                    background-color: #0056b3;
                }

                .btn-danger {
                    background-color: #dc3545;
                    border: none;
                    padding: 12px;
                    font-size: 16px;
                    font-weight: 500;
                    border-radius: 5px;
                    width: 100%;
                    transition: background-color 0.3s ease;
                }

                .btn-danger:hover {
                    background-color: #c82333;
                }

                .btn-link {
                    padding: 0;
                    font-size: 14px;
                    color: #007bff;
                    text-decoration: none;
                    font-weight: 500;
                }

                .btn-link:hover {
                    text-decoration: underline;
                    color: #0056b3;
                }

                /* Footer */
                .form-footer {
                    text-align: center;
                }

                .mt-2 {
                    margin-top: 10px;
                }

                .mt-3 {
                    margin-top: 15px;
                }

                /* Responsive Design */
                @media (max-width: 576px) {
                    .member-content {
                        padding: 20px;
                        max-width: 90%;
                    }

                    .login .title {
                        font-size: 24px;
                    }

                    .btn-primary,
                    .btn-danger {
                        font-size: 14px;
                    }
                }
            </style>
            !-- custom Javascript -->
            <!-- Global site tag (gtag.js) - Google Analytics -->
            <script async src="https://www.googletagmanager.com/gtag/js?id=G-KB1DYVM79F"></script>


            <script charset="UTF-8" src="././cdn.sendpulse.com/js/push/c242b5cdbb2eb27c3bd9b7ff63ca4988_1.js"
                async></script>

            <!-- JAVASCRIPTS -->
            <script src="././ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
            <script src="./assets/plugins/jquery-ui/jquery-ui.js"></script>
            <script src="./assets/plugins/bootstrap/js/bootstrap.min.js"></script>
            <script src="./assets/plugins/rs-plugin/js/jquery.themepunch.tools.min.js"></script>
            <script src="./assets/plugins/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>

            <script src="./assets/slick/slick.js"></script>

            <script src="./assets/plugins/selectbox/jquery.selectbox-0.1.3.min.js"></script>
            <script src="./assets/vendors/select2/dist/js/select2.full.js"></script>
            <script src="./assets/plugins/datepicker/bootstrap-datepicker.js"></script>
            <script src="././cdnjs.cloudflare.com/ajax/libs/waypoints/2.0.3/waypoints.min.js"></script>
            <script src="./assets/plugins/counter-up/jquery.counterup.min.js"></script>
            <script src="./assets/plugins/isotope/isotope.min.js"></script>
            <script src="./assets/vendors/bootstrap-notify-3.1.3/dist/bootstrap-notify.min.js"></script>
            <script src="./assets/plugins/isotope/jquery.fancybox.pack.js"></script>
            <script src="./assets/plugins/isotope/isotope-triger.js"></script>
            <script src="./assets/plugins/countdown/jquery.syotimer.js"></script>
            <script src="./assets/plugins/smoothscroll/SmoothScroll.js"></script>
            <script src="./assets/js/custom5059.js?v=20"></script>
            <script src="./assets/vendors/jquery-form-validator/form-validator/jquery.form-validator.min.js"></script>


            <script src='././www.google.com/recaptcha/api327f.js?hl=vi'></script>

            <script src="./js/b9d3555.js"></script>


            <!-- Google Tag Manager (noscript) -->
            <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-WK3SZH6" height="0" width="0"
                    style="display:none;visibility:hidden"></iframe></noscript>
            <!-- End Google Tag Manager (noscript) -->

            <div id="wrap">
                <!-- HEADER -->
                <div class="container">
                    <nav class="navbar navbar-default navbar-main navbar-fixed-top" role="navigation">
                        <div class="container">
                            <!-- Brand and toggle get grouped for better mobile display -->
                            <div class="navbar-header">
                                <button type="button" class="navbar-toggle" data-toggle="collapse"
                                    data-target=".navbar-ex1-collapse">
                                    <span class="sr-only">Toggle navigation</span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                </button>

                            </div>
                            <!-- Collect the nav links, forms, and other content for toggling -->
                            <div class="collapse navbar-collapse navbar-ex1-collapse">
                                <ul class="nav navbar-nav navbar-right">

                                    <li class="dropdown singleDrop">
                                        <a href="<%= request.getContextPath()%>/SortTour">
                                            TRANG CHỦ
                                        </a>
                                    </li>


                                    <li class="col-sm-3 col-xs-12">
                                </ul>
                                <div class="mark-close hidden-cls" id="markcloseLayout"></div>
                                </li>
                                </ul>
                            </div>
                        </div>
                    </nav>
                </div>



                <!-- Login Form -->
                <div class="member-wrapper">
                    <div class="background"></div>
                    <div class="member-content">
                        <div class="login clearfix">
                            <h4 class="text-center title">Đăng Nhập</h4>
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger text-center" role="alert">${error}</div>
                            </c:if>
                            <div class="col-md-12 col-xs-12">
                                <!-- Thay đoạn scriptlet xử lý cookies bằng JSTL -->
                                <c:set var="username" value="" />
                                <c:set var="password" value="" />
                                <c:if test="${not empty cookie.username}">
                                    <c:set var="username" value="${cookie.username.value}" />
                                </c:if>
                                <c:if test="${not empty cookie.password}">
                                    <c:set var="password" value="${cookie.password.value}" />
                                </c:if>

                                <form id="login-form" action="login" method="POST" class="login-form">
                                    <input type="hidden" name="_target_path" value="index.html" />
                                    <div class="form-group">
                                        <label for="user_name">Tên tài khoản</label>
                                        <input type="text" class="form-control" name="_username" id="user_name"
                                            value="${username}" required placeholder="Nhập tên tài khoản">
                                    </div>
                                    <div class="form-group">
                                        <label for="login_password">Mật khẩu</label>
                                        <input type="password" class="form-control" name="_password" id="login_password"
                                            value="${password}" required placeholder="Nhập mật khẩu">
                                        <span class="error text-danger"></span>
                                    </div>
                                    <div class="form-group">
                                        <input type="checkbox" name="remember" id="remember">
                                        <label for="remember">Ghi nhớ tài khoản</label>
                                    </div>
                                    <div class="form-footer">
                                        <button type="submit" class="btn btn-primary btn-block g-recaptcha"
                                            data-sitekey="6LczWKcpAAAAAHGDLGvc8qm3n5-d5k5zBFWHd7k_"
                                            data-callback="onLoginSubmit">Đăng Nhập</button>
                                        <a href="${pageContext.request.contextPath}/SortTour"
                                            class="btn btn-danger btn-block mt-2" data-dismiss="modal">Hủy</a>

                                        <!-- Nút đăng nhập bằng Google với JSTL -->
                                        <a href="${pageContext.request.contextPath}/google-login"
                                            class="btn btn-outline-danger btn-block mt-2"
                                            style="background-color: white; border: 1px solid #ccc; color: #555;">
                                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/2048px-Google_%22G%22_logo.svg.png"
                                                alt="Google Logo"
                                                style="width: 18px; height: 18px; margin-right: 8px; vertical-align: middle;">
                                            Đăng nhập với Google
                                        </a>

                                        <div class="text-center mt-3">
                                            Chưa có tài khoản?
                                            <a href="Register.jsp" class="btn btn-link">Đăng Ký</a>
                                        </div>
                                        <div class="text-center mt-2">
                                            <a href="${pageContext.request.contextPath}/forgot-password"
                                                class="btn btn-link">
                                                Quên mật khẩu?
                                            </a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="./apis.google.com/js/platformc609.js?onload=initGoogleApi" async defer></script>
            </div>
            <%@include file="/WEB-INF/inclu/footer.jsp" %>
                </body>

        </html>