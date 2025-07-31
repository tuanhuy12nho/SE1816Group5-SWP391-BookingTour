<%@ page import="DAO.RequestDAO" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String idParam = request.getParameter("id");
    if (idParam == null) {
        response.sendRedirect("admin_request_list.jsp");
        return;
    }

    int requestId = Integer.parseInt(idParam);
    RequestDAO dao = new RequestDAO();
    Map<String, String> r = dao.getRequestById(requestId);

    if (r == null || r.isEmpty()) {
%>
<h3 class="text-center text-danger">Không tìm thấy yêu cầu với ID = <%= idParam%></h3>
<%
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Chi tiết yêu cầu tour</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Chi tiết yêu cầu tour</h2>
            <ul class="list-group">
                <li class="list-group-item"><strong>Họ tên:</strong> <%= r.get("fullname")%></li>
                <li class="list-group-item"><strong>Email:</strong> <%= r.get("email")%></li>
                <li class="list-group-item"><strong>Tour:</strong> <%= r.get("tourType")%></li>
                <li class="list-group-item"><strong>Hành trình:</strong> <%= r.get("departure")%> → <%= r.get("destination")%></li>
                <li class="list-group-item"><strong>Ngày đi:</strong> <%= r.get("startDate")%> - <%= r.get("endDate")%></li>
                <li class="list-group-item"><strong>Trạng thái:</strong> <%= r.get("status")%></li>
                <li class="list-group-item"><strong>Ghi chú:</strong> <%= r.get("message")%></li>
            </ul>
            <form action="admin-request-list" method="get" class="mt-4 text-center">
                <button type="submit" class="btn btn-secondary">
                    ← Quay lại
                </button>
            </form>        </div>
    </body>
</html>
