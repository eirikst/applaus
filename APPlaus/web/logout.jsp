<%-- 
    Document   : logout
    Created on : Feb 19, 2014, 4:39:14 PM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
   session.invalidate();
   response.sendRedirect("login.jsp");
    %>