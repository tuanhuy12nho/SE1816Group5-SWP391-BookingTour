<%-- 
    Document   : checkLogin.jsp
    Created on : Feb 25, 2025, 10:43:52 PM
    Author     : amin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Boolean loggedIn = (Boolean) session.getAttribute("logged");
    if (loggedIn == null || !loggedIn) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>