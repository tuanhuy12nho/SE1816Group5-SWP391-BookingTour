<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Tour" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tour Finder</title>
        <style>
            * {
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', sans-serif;
                background-color: #f4f8fb;
                margin: 0;
                padding: 30px;
            }

            h2, h3 {
                color: #333;
            }

            .tour-container {
                display: grid;
                grid-template-columns: repeat(5, 1fr); /* luôn có 5 cột */
                gap: 20px;
                justify-content: center;
            }

            .tour-card {
                background-color: #ffffff;
                border-radius: 12px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                overflow: hidden;
                transition: transform 0.2s;
                height: 100%; /* giữ card không giãn */
                display: flex;
                text-decoration: none;
                flex-direction: column;
            }

            .tour-card:hover {
                transform: translateY(-4px);
            }

            .tour-card img {
                width: 100%;
                height: 180px;
                object-fit: cover;
            }

            .tour-info {
                padding: 15px;
            }

            .tour-info h4 {
                margin: 0 0 10px;
                color: #2c3e50;
            }

            .tour-info p {
                margin: 5px 0;
                color: #555;
            }
            .filter-form {
                max-width: 1000px;
                margin: 40px auto;
                padding: 24px;
                background-color: #f8f9fa;
                border-radius: 12px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                font-family: 'Segoe UI', sans-serif;
            }

            .form-wrapper {
                display: flex;
                flex-direction: column;
                gap: 20px;
            }

            .form-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                gap: 20px;
            }

            .form-grid div {
                display: flex;
                flex-direction: column;
            }

            .form-grid label {
                font-weight: 600;
                margin-bottom: 6px;
            }

            .form-grid input,
            .form-grid select {
                padding: 8px 12px;
                border-radius: 6px;
                border: 1px solid #ccc;
                font-size: 14px;
            }

            .form-submit {
                text-align: center;
            }

            .form-submit button {
                padding: 10px 24px;
                font-size: 16px;
                border: none;
                background-color: #007bff;
                color: white;
                border-radius: 6px;
                cursor: pointer;
                transition: background-color 0.2s ease;
            }

            .form-submit button:hover {
                background-color: #0056b3;
            }
            .btn-reset {
                display: inline-block;
                height: 40px;
                line-height: 40px;
                margin-top: auto;
                color: #fff;
                text-align: center;
                background-color: #007bff;
                text-decoration: none;
                border-radius: 6px;
                font-size: 14px;
                font-weight: bold;
                border: 1px solid #ccc;
                transition: all 0.2s ease;
            }

            .btn-reset:hover {
                background-color: red;
                color: #ffff;
                border-color: #ffff;
            }
            .page-btn {
                display: inline-block;
                padding: 8px 14px;
                background-color: #e9ecef;
                color: #333;
                text-decoration: none;
                border-radius: 4px;
                transition: background 0.2s;
                font-weight: bold;
            }

            .page-btn:hover {
                background-color: #007bff;
                color: #fff;
            }

            .page-btn.active {
                background-color: #007bff;
                color: #fff;
                pointer-events: none;
            }
            @media (max-width: 1200px) {
                .tour-container {
                    grid-template-columns: repeat(3, 1fr);
                }
            }
            @media (max-width: 768px) {
                .tour-container {
                    grid-template-columns: repeat(2, 1fr);
                }
            }
            @media (max-width: 480px) {
                .tour-container {
                    grid-template-columns: repeat(1, 1fr);
                }
            }
        </style>
         <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    </head>
    <body>
        <h2>🔍 Bộ lọc tìm kiếm Tour</h2>

        <form class="filter-form" method="get" action="tourFilter">
            <div class="form-wrapper">
                <div class="form-grid">

                    <!-- Ngày bắt đầu -->
                    <div>
                        <label>Ngày bắt đầu</label>
                        <input type="date" name="startDate" value="${startDate}">
                    </div>

                    <!-- Ngày kết thúc -->
                    <div>
                        <label>Ngày kết thúc</label>
                        <input type="date" name="endDate" value="${endDate}">
                    </div>

                    <!-- Phương tiện -->
                    <div>
                        <label>Phương tiện</label>
                        <select name="transport">
                            <option value="">--Tất cả--</option>
                            <option value="Bus" ${transport == 'Bus' ? 'selected' : ''}>Xe Bus</option>
                            <option value="May bay" ${transport == 'May bay' ? 'selected' : ''}>Máy bay</option>
                            <option value="Tau hoa" ${transport == 'Tau hoa' ? 'selected' : ''}>Tàu hỏa</option>
                            <option value="Xe du lich" ${transport == 'Xe du lich' ? 'selected' : ''}>Xe du lich</option>
                            <option value="Xe khach" ${transport == 'Xe khach' ? 'selected' : ''}>Xe khách</option>

                        </select>
                    </div>

                    <!-- Loại tour -->
                    <div>
                        <label>Loại tour</label>
                        <select name="type">
                            <option value="">--Tất cả--</option>
                            <option value="Domestic" ${type == 'Domestic' ? 'selected' : ''}>Trong nước</option>
                            <option value="International" ${type == 'International' ? 'selected' : ''}>Ngoài nước</option>
                        </select>
                    </div>

                    <!-- Giá từ -->
                    <div>
                        <label>Giá từ (VNĐ)</label>
                        <input type="number" name="minPrice" placeholder="Ví dụ: 1000000" value="${minPrice}">
                    </div>

                    <!-- Giá đến -->
                    <div>
                        <label>Giá đến (VNĐ)</label>
                        <input type="number" name="maxPrice" placeholder="Ví dụ: 5000000" value="${maxPrice}">
                    </div>

                    <!-- Sắp xếp -->
                    <div>
                        <label>Sắp xếp theo</label>
                        <select name="sort">
                            <option value="">--Mặc định--</option>
                            <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                            <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                            <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="oldest" ${sort == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                        </select>
                    </div>
                    <a class="btn-reset" href="tourFilter">Reset</a>
                </div>

                <!-- Nút Tìm kiếm -->
                <div class="form-submit">
                    <button type="submit">🔍 Tìm kiếm Tour</button>
                </div>
            </div>
        </form>

        <h3>🌍 Kết quả tìm kiếm (${totalRecord} tour mẫu):</h3>

        <div class="tour-container">
            <c:forEach var="tour" items="${tours}">
                <div class="tour-card-wrapper">
                    <div class="tour-card" >
                        <a href="booking?action=viewTour&id=${tour.id}">
                                <img src="${tour.imageUrl}" alt="Tour Image">
                        </a>
                        <div class="tour-info">
                            <h4>${tour.name}</h4>
                            <p>📍 ${tour.location}</p>
                            <p>💸 ${tour.price}</p>
                            <p>🚦 ${tour.transport}</p>
                            <p>🌐 ${tour.type}</p>
                            <p>⏰ ${tour.startDate} - ${tour.endDate}</p>
                        </div>
                    <c:if test="${not empty favoriteTourIds}">
                        <c:choose>
                            <c:when test="${favoriteTourIds.contains(tour.id)}">
                                <!-- Tour đã được yêu thích -->
                                <button class="btn-favorite" data-tourid="${tour.id}" data-favorited="true"
                                        style="background-color: transparent; border: none;">
                                    <i class="fa-solid fa-heart"
                                       style="color: red; font-size: 20px; border: 1px solid red; padding: 5px; border-radius: 50%;">
                                    </i>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <!-- Tour chưa được yêu thích -->
                                <button class="btn-favorite" data-tourid="${tour.id}" data-favorited="false"
                                        style="background-color: transparent; border: none;">
                                    <i class="fa-solid fa-heart"
                                       style="color: gray; font-size: 20px; border: 1px solid gray; padding: 5px; border-radius: 50%;">
                                    </i>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    </div>
                  
                </div>
            </c:forEach>

        </div>
        <div>
            <c:if test="${totalPages > 1}">
                <div style="text-align:center; margin-top: 30px;">
                    <nav>
                        <ul style="list-style: none; display: inline-flex; padding: 0; gap: 5px;">

                            <!-- Prev -->
                            <c:if test="${currentPage > 1}">
                                <li><a href="tourFilter?page=${currentPage - 1}" class="page-btn">⏪</a></li>
                                </c:if>

                            <!-- Pages (hiển thị tối đa 5) -->
                            <c:forEach var="i" begin="${(currentPage-2 > 0) ? currentPage-2 : 1}"
                                       end="${(currentPage+2 < totalPages) ? currentPage+2 : totalPages}">
                                <li>
                                    <a href="tourFilter?page=${i}" class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                                </li>
                            </c:forEach>

                            <!-- Next -->
                            <c:if test="${currentPage < totalPages}">
                                <li><a href="tourFilter?page=${currentPage + 1}" class="page-btn">⏩</a></li>
                                </c:if>
                        </ul>
                    </nav>
                </div>
            </c:if>

        </div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    document.querySelectorAll(".btn-favorite").forEach(button => {
        button.addEventListener("click", function () {
            const tourId = this.getAttribute("data-tourid");
            const isFavorited = this.getAttribute("data-favorited") === "true";
            const icon = this.querySelector("i");
            if (!tourId) return;

            const sendRequest = (action) => {
                fetch("<%= request.getContextPath()%>/favorite", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: 'action='+action+'&tourId='+tourId
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
