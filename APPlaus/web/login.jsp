<%-- 
    Document   : login
    Created on : Feb 19, 2014, 11:38:18 AM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") != null) {
        response.sendRedirect("index.jsp");
    }
%>

<!DOCTYPE html>
<html ng-app="loginApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>APPlause</title>

        <!--css-->
        <link rel="stylesheet" href="css/bootstrap.css"/>
        <link rel="stylesheet" href="css/bootstrap-extensions.css"/>
        <link rel="stylesheet" href="css/custom.css"/>
        <!--end css-->

        <!--lib js scripts-->
        <script src="js/libs/jquery.js"></script>
        <script src="js/libs/angular.js"></script>
        <script src="js/libs/angular-route.js"></script>
        <script src="js/libs/angular-animate.js"></script>
        <script src="js/libs/bootstrap.js"></script>
        <!--end lib js scripts-->

    </head>

    <body style="background-color:white">

        <!--static navbar-->
        <div class="navigation" role="navigation">
            <div class="navigationInner">
                <h4>Welcome to APPlause!</h4>
            </div>
        </div>
        <!--end static navbar-->

        <!--content-->
        <div class="content">

            <!--partial-->
            <div ng-view="" class="reveal-animation"></div>
            <!--end partial-->

        </div>
        <!--end content-->

        <!--angular scripts-->
        <script src="js/app/loginApp.js"></script>
        <script src="js/app/controllers/loginController.js"></script>
        <script src="js/app/services/loginServices.js"></script>
        <!--end angular scripts-->

    </body>
</html>