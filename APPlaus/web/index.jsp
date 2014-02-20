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
                
                
                <a class="glyphicon glyphicon-headphones glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('')}"></a>
                <a class="glyphicon glyphicon-user glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('')}"></a>
                <a href="#view1" class="glyphicon glyphicon-flag glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('/view1')}"></a>
                <a href="#userFront" class="glyphicon glyphicon-home glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('/userFront')}"></a>
                <a href="#contests" class="glyphicon glyphicon-pencil glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('/contests')}"></a>
                <a class="glyphicon glyphicon-signal glyphicon-indexHeader"  ng-class="{'glyfhicon-chosen': isActive('')}"></a>
                <a class="glyphicon glyphicon-play-circle glyphicon-indexHeader" ng-class="{'glyfhicon-chosen': isActive('')}"></a>
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
    </body>
</html>