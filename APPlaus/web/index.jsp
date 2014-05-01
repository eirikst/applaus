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

        <title>APPlause</title>
    </head>

    <body style="background-color:white">

        <!--static navbar-->
        <div class="navigation" ng-controller="HeaderCtrl">
            <table class="navigationInner">
                <tr>
                    <td class="navigationSide"></td><!--on each side to center main content-->
                    <td class="navigationCenter text-center">
                        <span>
                            <!--nav icons-->
                            <a href="#stats" ng-show="!isActive('/stats')"><img class="index-icon" src="img/Profile.png"></a>
                            <a href="#stats" ng-show="isActive('/stats')"><img class="index-icon" src="img/Profile_selected.png"></a>
                            <a href="#assignments" ng-show="!isActive('/assignments')"><img class="index-icon" src="img/Assignment.png"></a>
                            <a href="#assignments" ng-show="isActive('/assignments')"><img class="index-icon" src="img/Assignment_selected.png"></a>
                            <a href="#home" ng-show="!isActive('/home')"><img class="index-icon" src="img/Home.png"></a>
                            <a href="#home" ng-show="isActive('/home')"><img class="index-icon" src="img/Home_selected.png"></a>
                            <a href="#contests" ng-show="!isActive('/contests')"><img class="index-icon" src="img/Contest.png"></a>
                            <a href="#contests" ng-show="isActive('/contests')"><img class="index-icon" src="img/Contest_selected.png"></a>
                            <a href="#ideabank" ng-show="!isActive('/ideabank')"><img class="index-icon" src="img/Idea.png"></a>
                            <a href="#ideabank" ng-show="isActive('/ideabank')"><img class="index-icon" src="img/Idea_selected.png"></a>
                            <!--end nav icons-->
                        </span>
                    </td>
                    <td class="navigationSide">
                        <ul class="nav pull-right">
                            <li class="dropdown">
                                <a style="color:white" class="dropdown-toggle" ng-class="glyphicon - chevron - down" data-toggle="dropdown">
                                    More
                                    <b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu pull-right">
                                    <li><a type="button" href="#adminList" ng-show="roleCookie == 1">Admin features</a></li>
                                    <li><a type="button" href="#assignmentTypeList" ng-show="roleCookie == 1">Assignment Type List</a></li>
                                    <li><a href="logout.jsp">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </td>
                </tr>
            </table>
        </div>
        <!--static navbar end-->

        <!--content-->
        <div class="content">

            <!--partial-->
            <!--div ng-view="" class="container container-1024 reveal-animation"></div-->
            <!--end partial-->
            
        <div class="view-animate-container container container-1024" ng-class="direction">
          <div ng-view class="view-animate"></div>
        </div>
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
        <script src="js/app/controllers/statisticsController.js"></script>       
        <script src="js/app/services/services.js"></script>
        <script src="js/app/services/homeServices.js"></script>
        <script src="js/app/services/assignmentServices.js"></script>
        <script src="js/app/services/contestServices.js"></script>
        <script src="js/app/services/adminServices.js"></script>
        <script src="js/app/services/ideaServices.js"></script>
        <script src="js/app/services/statisticsServices.js"></script>
        <script src="js/app/directives/directives.js"></script>
        <script src="js/app/filters/filters.js"></script>
        <!--end angular scripts-->
    </body>
</html>