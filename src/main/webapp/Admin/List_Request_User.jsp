<%@ page import="DAO.RequestDAO" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    RequestDAO dao = new RequestDAO();
    List<Map<String, String>> requests = dao.getAllRequests();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Danh sách yêu cầu tour</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <h2 class="text-center mb-4">Yêu cầu tour của người dùng</h2>
            <table class="table table-bordered table-hover table-striped align-middle">
                <thead class="table-success">
                    <tr>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>Tour</th>
                        <th>Hành trình</th>
                        <th>Ngày gửi</th>
                        <th>Trạng thái</th>
                        <th class="text-center">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, String> r : requests) {%>
                    <tr>
                        <td><%= r.get("id")%></td>
                        <td><%= r.get("fullname")%></td>
                        <td><%= r.get("email")%></td>
                        <td><%= r.get("tourType")%></td>
                        <td><%= r.get("departure")%> → <%= r.get("destination")%></td>
                        <td><%= r.get("createdAt")%></td>
                        <td>
                            <span class="badge
                                  <%= "Đã duyệt".equals(r.get("status")) ? "bg-success"
                                          : "Từ chối".equals(r.get("status")) ? "bg-danger"
                                          : "bg-warning text-dark"%>">
                                <%= r.get("status")%>
                            </span>
                        </td>
                        <td class="text-center">
                            <a href="request_detail.jsp?id=<%= r.get("id")%>" class="btn btn-info btn-sm mb-1">
                                Xem chi tiết
                            </a>
                            <br/>
                            <% if ("Chờ xử lý".equals(r.get("status"))) {%>
                            <form action="admin-request-action" method="post" class="d-inline">
                                <input type="hidden" name="requestId" value="<%= r.get("id")%>">
                                <input type="hidden" name="action" value="approve">
                                <button type="submit" class="btn btn-success btn-sm"
                                        onclick="return confirm('Bạn có chắc chắn muốn PHÊ DUYỆT yêu cầu này?')">
                                    Phê duyệt
                                </button>
                            </form>
                            <form action="admin-request-action" method="post" class="d-inline">
                                <input type="hidden" name="requestId" value="<%= r.get("id")%>">
                                <input type="hidden" name="action" value="reject">
                                <button type="submit" class="btn btn-danger btn-sm"
                                        onclick="return confirm('Bạn có chắc chắn muốn TỪ CHỐI yêu cầu này?')">
                                    Từ chối
                                </button>
                            </form>
                            <% } else { %>
                            <em>Đã xử lý</em>
                            <% } %>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>

            <!-- Nút quay lại -->
            <form action="adminlist" method="get" class="text-center mt-3">
                <button type="submit" class="btn btn-secondary">
                    ← Quay lại
                </button>
            </form>

        </div>
    </body>
</html>
