<%-- 
    Document   : index
    Created on : Feb 19, 2014, 3:11:31 PM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    if(session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
}
        %>
<html ng-app="employeeApp">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">

        <link rel="stylesheet" href="css/bootstrap.css"/>
        <link rel="stylesheet" href="css/bootstrap-extensions.css"/>
        <link rel="stylesheet" href="css/custom.css"/>

        <script src="js/libs/jquery.js"></script>
        <script src="js/libs/angular.js"></script>
        <script src="js/libs/angular-route.js"></script>
        <script src="js/libs/angular-touch.js"></script>
        <script src="js/libs/angular-animate.js"></script>
        <script src="js/libs/bootstrap.js"></script>
        <script src="js/libs/ui-bootstrap-tpls-0.10.0.js"></script>


        <title>APPlaus</title>
    </head> 
    <body style="margin-top: 61px">
        
        <!-- Static navbar -->
        <div class="navbar navbar-default navbar-fixed-top navbar-color" role="navigation" ng-controller="HeaderCtrl">
            <div style="margin:10px" class="text-center">
                
                
                <a href=href="#profile"><img class="index-icons" src="img/Profile.png"></a>
                <a href="#registerAssignment"><img class="index-icons" src="img/Assignment.png"></a>
                <a href="#userFromt"><img class="index-icons" src="img/Home.png"></a>
                <a href="#contests"><img class="index-icons" src="img/Contest.png"></a>
                <a href="#settings"><img class="index-icons" src="img/Settings.png"></a>
            </div>
        </div>
        <div>
            <div class="my-slide-container">
                <div ng-view="" class="reveal-animation"></div>
            </div>
            <form action="logout.jsp">
            <button onclick="submit()">Log out</button>
            </form>
        </div>

        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
        <script src="js/app/controllers/homeController.js"></script>
        <script src="js/app/controllers/headerController.js"></script>
        <script src="js/app/controllers/loginController.js"></script>
        <script src="js/app/controllers/assignController.js"></script>
        <script src="js/app/controllers/contController.js"></script>       
        <script src="js/app/services/homeServices.js"></script>
        <script src="js/app/services/assignmentServices.js"></script>
        <script src="js/app/services/contestServices.js"></script>
    </body>
</html>