<%-- 
    Document   : index
    Created on : Feb 19, 2014, 3:11:31 PM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
    }
%>


<html ng-app='employeeApp'>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">

        <!--css-->
        <link rel="stylesheet" href="css/bootstrap.css"/>
        <link rel="stylesheet" href="css/bootstrap-extensions.css"/>
        <link rel="stylesheet" href="css/custom.css"/>
        <!--end css-->

        <!--lib js scripts-->
        <script src="js/libs/jquery.js"></script>
        <script src="js/libs/angular.js"></script>
        <script src="js/libs/angular-route.js"></script>
        <script src="js/libs/angular-touch.js"></script>
        <script src="js/libs/angular-animate.js"></script>
        <script src="js/libs/angular-cookies.js"></script>
        <script src="js/libs/angular-resource.js"></script>
        <script src="js/libs/bootstrap.js"></script>
        <script src="js/libs/ui-bootstrap-tpls-0.10.0.js"></script>
        <!--end lib js scripts-->

        <title>APPlaus</title>
    </head>

    <body background="img/bw.jpg">

        <!--static navbar-->
        <div class="navbar navbar-default navbar-fixed-top navbar-color" role="navigation" ng-controller="HeaderCtrl">
            <div class="navbar-content-index text-center">

                <!--nav icons-->
                <a href="#profile"><img class="index-icon" src="img/Profile.png"></a>
                <a href="#registerAssignment"><img class="index-icon" src="img/Assignment.png"></a>
                <a href="#home"><img class="index-icon" src="img/Home.png"></a>
                <a href="#contests"><img class="index-icon" src="img/Contest.png"></a>
                <a href="#ideabank"><img class="index-icon" src="img/Settings.png"></a>
                <!--end nav icons-->

                <!--nav dropmenu-->
                <ul class="nav pull-right">
                    <li class="dropdown">
                        <a class="dropdown-toggle" ng-class="glyphicon - chevron - down" data-toggle="dropdown">
                            Mer
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu pull-right">
                            <li><a type="button" href="#adminList" ng-show="roleCookie == 1">Admin features</a></li>
                            <li><a href="logout.jsp">Logout</a></li>
                        </ul>
                    </li>
                </ul>
                <!--nav dropmenu end-->

            </div>
        </div>
        <!--static navbar end-->

        <!--content-->
        <div class="content">

            <!--partial-->
            <div ng-view="" class="reveal-animation"></div>
            <!--end partial-->

        </div>
        <!--end content-->

        <!--angular scripts-->
        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
        <script src="js/app/controllers/homeController.js"></script>
        <script src="js/app/controllers/headerController.js"></script>
        <script src="js/app/controllers/assignController.js"></script>
        <script src="js/app/controllers/contController.js"></script>       
        <script src="js/app/controllers/adminController.js"></script>       
        <script src="js/app/controllers/ideaController.js"></script>       
        <script src="js/app/services/homeServices.js"></script>
        <script src="js/app/services/assignmentServices.js"></script>
        <script src="js/app/services/contestServices.js"></script>
        <script src="js/app/services/adminServices.js"></script>
        <script src="js/app/services/ideaServices.js"></script>
        <script src="js/app/directives/directives.js"></script>
        <!--end angular scripts-->
    </body>
</html>