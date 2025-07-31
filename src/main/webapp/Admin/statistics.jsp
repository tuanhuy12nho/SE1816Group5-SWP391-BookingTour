<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ page import="java.util.List, model.AdminBooking" %>
        <%@ page import="java.text.NumberFormat, java.util.Locale, java.text.SimpleDateFormat" %>
            <% NumberFormat currencyFormat=NumberFormat.getCurrencyInstance(new Locale("vi", "VN" )); SimpleDateFormat
                dateFormat=new SimpleDateFormat("dd/MM/yyyy"); String formattedRevenue="0 VND" ; Object
                totalRevenueObj=request.getAttribute("totalRevenue"); if (totalRevenueObj !=null) { try { double
                totalRevenue=Double.valueOf(totalRevenueObj.toString());
                formattedRevenue=currencyFormat.format(totalRevenue); } catch (Exception e) {
                formattedRevenue="Không hợp lệ" ; } } String selectedMonth=request.getParameter("month"); String
                selectedYear=request.getParameter("year"); String selectedTourType=(String)
                request.getAttribute("tourType"); if (selectedTourType==null) selectedTourType="Domestic" ; Integer
                domesticCount=(Integer) request.getAttribute("domesticCount"); Integer internationalCount=(Integer)
                request.getAttribute("internationalCount"); Double domesticRevenue=(Double)
                request.getAttribute("domesticRevenue"); Double internationalRevenue=(Double)
                request.getAttribute("internationalRevenue"); List<AdminBooking> revenueDetails = (List<AdminBooking>)
                    request.getAttribute("revenueDetails");
                    %>
                    <!DOCTYPE html>
                    <html>

                    <head>
                        <title>Revenue Report</title>
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                            rel="stylesheet">
                        <link rel="stylesheet"
                            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">


                        <style>
                            /* Tổng thể */
                            body {
                                background: linear-gradient(135deg, #74ebd5, #acb6e5);
                                font-family: 'Poppins', sans-serif;
                                color: #333;
                            }

                            /* Container chính */
                            .container {
                                background: rgba(255, 255, 255, 0.9);
                                padding: 30px;
                                border-radius: 12px;
                                margin-top: 40px;
                                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
                                backdrop-filter: blur(10px);
                                transition: all 0.3s ease-in-out;
                            }

                            .container:hover {
                                transform: translateY(-5px);
                                box-shadow: 0 12px 25px rgba(0, 0, 0, 0.2);
                            }

                            /* Tiêu đề */
                            h2 {
                                font-weight: bold;
                                color: #2c3e50;
                                text-transform: uppercase;
                                letter-spacing: 1px;
                            }

                            /* Bảng dữ liệu */
                            .table {
                                border-radius: 8px;
                                overflow: hidden;
                            }

                            .table th {
                                text-align: center;
                                background: linear-gradient(135deg, #007bff, #0056b3);
                                color: white;
                                padding: 14px;
                                font-size: 16px;
                            }

                            .table td {
                                text-align: center;
                                vertical-align: middle;
                                padding: 12px;
                                font-size: 14px;
                                font-weight: 500;
                            }

                            .table tbody tr {
                                transition: all 0.3s ease-in-out;
                            }

                            .table tbody tr:hover {
                                background-color: rgba(0, 123, 255, 0.1);
                                transform: scale(1.02);
                            }

                            /* Ô input và select */
                            .form-control,
                            .form-select {
                                border-radius: 8px;
                                height: 45px;
                                border: 2px solid #ced4da;
                                transition: all 0.3s ease-in-out;
                            }

                            .form-control:focus,
                            .form-select:focus {
                                border-color: #007bff;
                                box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
                            }

                            /* Nút bấm */
                            .btn-primary {
                                width: 100%;
                                font-weight: bold;
                                text-transform: uppercase;
                                background: linear-gradient(135deg, #007bff, #004085);
                                border: none;
                                padding: 12px;
                                border-radius: 8px;
                                transition: all 0.3s ease-in-out;
                                box-shadow: 0 4px 10px rgba(0, 123, 255, 0.3);
                            }

                            .btn-primary:hover {
                                background: linear-gradient(135deg, #0056b3, #003366);
                                transform: scale(1.05);
                            }

                            /* Nút "Tất cả" */
                            .btn-secondary {
                                background: linear-gradient(135deg, #6c757d, #343a40);
                                border: none;
                                padding: 12px;
                                border-radius: 8px;
                                font-weight: bold;
                                color: white;
                                transition: all 0.3s ease-in-out;
                                box-shadow: 0 4px 10px rgba(108, 117, 125, 0.3);
                            }

                            .btn-secondary:hover {
                                background: linear-gradient(135deg, #495057, #212529);
                                transform: scale(1.05);
                                box-shadow: 0 6px 15px rgba(108, 117, 125, 0.5);
                            }

                            /* Nút "Back" */
                            /* Nút "Back" đặt ở góc trái dưới */
                            .btn-back {
                                position: fixed;
                                bottom: 20px;
                                left: 20px;
                                background: linear-gradient(135deg, #ff7e5f, #ff3f34);
                                border: none;
                                padding: 12px 20px;
                                border-radius: 8px;
                                font-weight: bold;
                                color: white;
                                transition: all 0.3s ease-in-out;
                                box-shadow: 0 4px 10px rgba(255, 63, 52, 0.3);
                                text-transform: uppercase;
                                z-index: 1000;
                            }

                            .btn-back:hover {
                                background: linear-gradient(135deg, #e63946, #c0392b);
                                transform: scale(1.1);
                                box-shadow: 0 6px 15px rgba(255, 63, 52, 0.5);
                            }

                            /* Hiệu ứng Responsive */
                            @media (max-width: 768px) {
                                .container {
                                    padding: 20px;
                                }

                                .btn-primary,
                                .btn-secondary {
                                    font-size: 14px;
                                    padding: 10px;
                                }

                                .form-control,
                                .form-select {
                                    height: 40px;
                                }
                            }

                            /* Additional styles for statistics cards */
                            .card {
                                transition: transform 0.3s ease-in-out;
                                margin-bottom: 20px;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            }

                            .card:hover {
                                transform: translateY(-5px);
                            }

                            .card-header {
                                font-weight: bold;
                                text-transform: uppercase;
                                letter-spacing: 1px;
                            }

                            /* Table styling enhancements */
                            .table th {
                                cursor: pointer;
                                user-select: none;
                            }

                            .table th:hover {
                                background: linear-gradient(135deg, #0056b3, #003366);
                            }

                            .table th i {
                                margin-left: 5px;
                                opacity: 0.5;
                            }

                            .table th.sorted i {
                                opacity: 1;
                            }

                            .badge {
                                font-size: 0.9em;
                                padding: 8px 12px;
                            }

                            /* Animation for the data loading */
                            .table-loading {
                                position: relative;
                                opacity: 0.5;
                            }

                            .table-loading:after {
                                content: "Loading...";
                                position: absolute;
                                top: 50%;
                                left: 50%;
                                transform: translate(-50%, -50%);
                                font-size: 1.2em;
                                color: #007bff;
                            }

                            /* Responsive enhancements */
                            @media (max-width: 768px) {
                                .card {
                                    margin-bottom: 15px;
                                }

                                .table {
                                    font-size: 0.9em;
                                }

                                .badge {
                                    font-size: 0.8em;
                                    padding: 6px 8px;
                                }
                            }
                        </style>
                    </head>

                    <body>
                        <div class="container">
                            <h2 class="mb-4 text-center">Doanh Thu</h2>

                            <%-- Show error message if exists --%>
                                <% if (request.getAttribute("error") !=null) { %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <%= request.getAttribute("error") %>
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <% } %>

                                        <%-- Show info message if exists --%>
                                            <% if (request.getAttribute("message") !=null) { %>
                                                <div class="alert alert-info alert-dismissible fade show" role="alert">
                                                    <%= request.getAttribute("message") %>
                                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                            aria-label="Close"></button>
                                                </div>
                                                <% } %>

                                                    <!-- Revenue filter form -->
                                                    <form action="<%=request.getContextPath()%>/AdminStatisticsServlet"
                                                        method="GET" class="row g-3">
                                                        <input type="hidden" name="type" value="revenueDetails">


                                                        <div class="col-md-3">
                                                            <label for="month" class="form-label">Tháng:</label>
                                                            <select id="month" name="month" class="form-select"
                                                                required>
                                                                <option value="">Chọn tháng</option>
                                                                <option value="1" <%="1"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 1</option>
                                                                <option value="2" <%="2"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 2</option>
                                                                <option value="3" <%="3"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 3</option>
                                                                <option value="4" <%="4"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 4</option>
                                                                <option value="5" <%="5"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 5</option>
                                                                <option value="6" <%="6"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 6</option>
                                                                <option value="7" <%="7"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 7</option>
                                                                <option value="8" <%="8"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 8</option>
                                                                <option value="9" <%="9"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 9</option>
                                                                <option value="10" <%="10"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 10</option>
                                                                <option value="11" <%="11"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 11</option>
                                                                <option value="12" <%="12"
                                                                    .equals(request.getParameter("month")) ? "selected"
                                                                    : "" %>>Tháng 12</option>
                                                            </select>
                                                        </div>

                                                        <div class="col-md-3">
                                                            <label for="year" class="form-label">Năm:</label>
                                                            <select id="year" name="year" class="form-select" required>
                                                                <option value="">Chọn năm</option>
                                                                <% int currentYear=java.time.Year.now().getValue();
                                                                    for(int i=currentYear - 5; i <=currentYear + 5; i++)
                                                                    { String
                                                                    selected=String.valueOf(i).equals(request.getParameter("year"))
                                                                    ? "selected" : "" ; %>
                                                                    <option value="<%= i %>" <%=selected %>><%= i %>
                                                                    </option>
                                                                    <% } %>
                                                            </select>
                                                        </div>

                                                        <div class="col-md-3">
                                                            <label for="tourType" class="form-label">Loại:</label>
                                                            <select id="tourType" name="tourType" class="form-select">
                                                                <option value="Domestic" <%="Domestic"
                                                                    .equals(request.getAttribute("tourType"))
                                                                    ? "selected" : "" %>>Trong nước</option>
                                                                <option value="International" <%="International"
                                                                    .equals(request.getAttribute("tourType"))
                                                                    ? "selected" : "" %>>Ngoài nước</option>
                                                            </select>

                                                        </div>

                                                        <div class="col-md-3 d-flex align-items-end">
                                                            <button type="submit" class="btn btn-primary">Lọc</button>
                                                            <a href="<%=request.getContextPath()%>/AdminStatisticsServlet?type=revenueAll"
                                                                class="btn btn-secondary ms-2">All</a>
                                                        </div>
                                                    </form>

                                                    <!-- Statistics Summary Cards -->
                                                    <% if (domesticCount !=null && internationalCount !=null) { %>
                                                        <div class="row mt-4 mb-4">
                                                            <!-- Domestic Statistics Card -->
                                                            <div class="col-md-6">
                                                                <div class="card border-primary">
                                                                    <div class="card-header bg-primary text-white">
                                                                        <h5 class="mb-0">Tour Trong Nước</h5>
                                                                    </div>
                                                                    <div class="card-body">
                                                                        <h6>Số lượng đặt: <%= domesticCount %>
                                                                        </h6>
                                                                        <h6>Doanh thu: <%= domesticRevenue !=null ?
                                                                                currencyFormat.format(domesticRevenue)
                                                                                : "0" %> VND</h6>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <!-- International Statistics Card -->
                                                            <div class="col-md-6">
                                                                <div class="card border-success">
                                                                    <div class="card-header bg-success text-white">
                                                                        <h5 class="mb-0">Tour Ngoài Nước</h5>
                                                                    </div>
                                                                    <div class="card-body">
                                                                        <h6>Số lượng đặt: <%= internationalCount %>
                                                                        </h6>
                                                                        <h6>Doanh thu: <%= internationalRevenue !=null ?
                                                                                currencyFormat.format(internationalRevenue)
                                                                                : "0" %> VND</h6>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <% } %>

                                                            <!-- Revenue table -->
                                                            <table class="table table-bordered mt-4">
                                                                <thead class="table-primary">
                                                                    <tr>
                                                                        <th>ID <i class="fas fa-sort"></i></th>
                                                                        <th>Khách hàng <i class="fas fa-sort"></i></th>
                                                                        <th>Số điện thoại</th>
                                                                        <th>Email</th>
                                                                        <th>Ngày khởi hành <i class="fas fa-sort"></i>
                                                                        </th>
                                                                        <th>Địa điểm <i class="fas fa-sort"></i></th>
                                                                        <th>Giá <i class="fas fa-sort"></i></th>
                                                                        <th>Tổng tiền <i class="fas fa-sort"></i></th>
                                                                        <th>Trạng thái <i class="fas fa-sort"></i></th>
                                                                        <th>Loại tour</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <% if (revenueDetails !=null &&
                                                                        !revenueDetails.isEmpty()) { for (AdminBooking
                                                                        booking : revenueDetails) { %>
                                                                        <tr>
                                                                            <td>
                                                                                <%= booking.getId() %>
                                                                            </td>
                                                                            <td>
                                                                                <%= booking.getCustomerName() %>
                                                                            </td>
                                                                            <td>
                                                                                <%= booking.getPhone() !=null ?
                                                                                    booking.getPhone() : "N/A" %>
                                                                            </td>
                                                                            <td>
                                                                                <%= booking.getEmail() !=null ?
                                                                                    booking.getEmail() : "N/A" %>
                                                                            </td>
                                                                            <td>
                                                                                <%= booking.getDeparture() !=null ?
                                                                                    dateFormat.format(booking.getDeparture())
                                                                                    : "N/A" %>
                                                                            </td>
                                                                            <td>
                                                                                <%= booking.getDestination() %>
                                                                            </td>
                                                                            <td class="text-end">
                                                                                <%= currencyFormat.format(booking.getPrice())
                                                                                    %>
                                                                            </td>
                                                                            <td class="text-end">
                                                                                <%= currencyFormat.format(booking.getTotalAmount())
                                                                                    %>
                                                                            </td>
                                                                          <td>
    <span class="badge <%= "Confirmed".equals(booking.getPaymentStatus()) ? "bg-success" : "bg-warning" %>">
        <%= booking.getPaymentStatus() %>
    </span>
</td>
<td>
    <span class="badge <%= "Domestic".equals(booking.getTourType()) ? "bg-primary" : "bg-info" %>">
        <%= "Domestic".equals(booking.getTourType()) ? "Trong nước" : "Ngoài nước" %>
    </span>
</td>

                                                                        </tr>
                                                                        <% } } else { %>
                                                                            <tr>
                                                                                <td colspan="10" class="text-center">
                                                                                    <div
                                                                                        class="alert alert-warning mb-0">
                                                                                        <i
                                                                                            class="fas fa-exclamation-triangle"></i>
                                                                                        Không có dữ liệu doanh thu
                                                                                        cho
                                                                                        <%= request.getAttribute("month")
                                                                                            !=null ? "tháng " +
                                                                                            request.getAttribute("month")
                                                                                            : "" %>
                                                                                            <%= request.getAttribute("year")
                                                                                                !=null ? "năm " +
                                                                                                request.getAttribute("year")
                                                                                                : "" %>
                                                                                    </div>
                                                                                </td>
                                                                            </tr>
                                                                            <% } %>
                                                                </tbody>

                                                            </table>
                                                            <!-- Show total revenue below table -->
                                                            <% if (totalRevenueObj !=null) {%>
                                                                <div class="alert alert-info text-center mt-3">
                                                                    <strong>Tổng doanh thu tháng <%=
                                                                            request.getAttribute("month")%>
                                                                            năm <%= request.getAttribute("year")%>
                                                                                :</strong>
                                                                    <%= formattedRevenue%>
                                                                </div>
                                                                <% }%>
                        </div>
                        <a href="<%= request.getContextPath()%>/Admin/admin.jsp" class="btn btn-back">
                            <i class="fas fa-arrow-left"></i>
                        </a>

                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                        <script>
                            // Table sorting functionality
                            document.addEventListener('DOMContentLoaded', function () {
                                const table = document.querySelector('table');
                                const headers = table.querySelectorAll('th');
                                const tableBody = table.querySelector('tbody');
                                const rows = tableBody.querySelectorAll('tr');

                                // Sort direction for each column
                                const directions = Array.from(headers).map(() => '');

                                // Convert cell value for sorting
                                const transform = function (index, content) {
                                    // Get the content of the cell
                                    const type = headers[index].textContent.trim().toLowerCase();

                                    // Strip the currency symbol and convert to number for price columns
                                    if (type.includes('giá') || type.includes('tổng')) {
                                        return parseFloat(content.replace(/[^\d.-]/g, ''));
                                    }

                                    // Convert date strings to Date objects
                                    if (type.includes('ngày')) {
                                        const [day, month, year] = content.split('/');
                                        return new Date(year, month - 1, day);
                                    }

                                    // Default string comparison
                                    return content;
                                };

                                const sortTable = function (index) {
                                    const newRows = Array.from(rows);
                                    const direction = directions[index] || 'asc';
                                    const multiplier = (direction === 'asc') ? 1 : -1;

                                    newRows.sort(function (rowA, rowB) {
                                        const cellA = rowA.querySelectorAll('td')[index].textContent.trim();
                                        const cellB = rowB.querySelectorAll('td')[index].textContent.trim();

                                        const a = transform(index, cellA);
                                        const b = transform(index, cellB);

                                        if (a < b) return -1 * multiplier;
                                        if (a > b) return 1 * multiplier;
                                        return 0;
                                    });

                                    // Remove old rows
                                    [].forEach.call(rows, function (row) {
                                        tableBody.removeChild(row);
                                    });

                                    // Update sort direction
                                    directions[index] = direction === 'asc' ? 'desc' : 'asc';

                                    // Update header classes
                                    headers.forEach(header => header.classList.remove('sorted'));
                                    headers[index].classList.add('sorted');

                                    // Append new rows
                                    newRows.forEach(function (newRow) {
                                        tableBody.appendChild(newRow);
                                    });
                                };

                                // Add click listeners to headers
                                headers.forEach(function (header, index) {
                                    if (header.querySelector('i.fa-sort')) {  // Only add listener if column is sortable
                                        header.addEventListener('click', function () {
                                            sortTable(index);
                                        });
                                    }
                                });

                                // Auto-hide alerts after 5 seconds
                                const alerts = document.querySelectorAll('.alert');
                                alerts.forEach(function (alert) {
                                    setTimeout(function () {
                                        if (alert.querySelector('.btn-close')) {
                                            alert.querySelector('.btn-close').click();
                                        }
                                    }, 5000);
                                });
                            });
                        </script>
                    </body>

                    </html>