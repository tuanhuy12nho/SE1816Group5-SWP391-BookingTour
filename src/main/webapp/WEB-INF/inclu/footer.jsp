<%@page contentType="text/html" pageEncoding="UTF-8"%>

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