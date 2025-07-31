<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Yêu cầu đặt tour</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">

        <div class="container mt-5 mb-5">
            <div class="card shadow-lg">
                <div class="card-header bg-success text-white text-center">
                    <h4>Yêu cầu đặt tour</h4>
                </div>
                <div class="card-body">
                    <form action="RequestController" method="post">
                        <div class="mb-3">
                            <label class="form-label">Họ và tên</label>
                            <input type="text" name="fullname" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Quốc tịch</label>
                            <input type="text" name="nationality" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Loại tour</label>
                            <select name="tourType" class="form-select" required>
                                <option value="">-- Chọn loại tour --</option>
                                <option value="Trong nước">Trong nước</option>
                                <option value="Ngoài nước">Ngoài nước</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Địa điểm xuất phát</label>
                            <input type="text" name="departure" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Địa điểm đến</label>
                            <input type="text" name="destination" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Ngày đi</label>
                            <input type="date" name="startDate" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Ngày kết thúc</label>
                            <input type="date" name="endDate" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Số lượng người</label>
                            <input type="number" name="numPeople" class="form-control" required min="1">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Ghi chú</label>
                            <textarea name="message" class="form-control" rows="3"></textarea>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-success px-5">Gửi yêu cầu</button>
                             <a href="<%= request.getContextPath() %>/SortTour" class="btn btn-secondary px-4 ms-2">Hủy</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
