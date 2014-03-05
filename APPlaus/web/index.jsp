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
    <body style="margin-top: 61px; background-repeat:no-repeat;background-attachment:fixed;" background="img/bw.jpg"
          >
        
        <!-- Static navbar -->
        <div class="navbar navbar-default navbar-fixed-top navbar-color" role="navigation" ng-controller="HeaderCtrl">
            <div style="margin:10px" class="text-center">

                        <a href="#profile"><img class="index-icon" src="img/Profile.png"></a>
                        <a href="#registerAssignment"><img class="index-icon" src="img/Assignment.png"></a>
                        <a href="#home"><img class="index-icon" src="img/Home.png"></a>
                        <a href="#contests"><img class="index-icon" src="img/Contest.png"></a>
                        <a href="#settings"><img class="index-icon" src="img/Settings.png"></a>
                        
                        <!--<ul class="nav">
            <li id="fat-menu" class="dropdown">
              <a href="#" id="drop3" role="button" class="dropdown-toggle" data-toggle="dropdown">Mer<b class="caret"></b></a>
              <ul class="dropdown-menu" role="menu" aria-labelledby="drop3">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="logout.jsp">Logout</a></li>
              </ul>
            </li>
          </ul>-->
                        
                <ul class="nav pull-right">
                  <li class="dropdown">
                      <a class="dropdown-toggle" ng-class="glyphicon-chevron-down" data-toggle="dropdown">
                      Mer
                      <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu pull-right">
                      <li><a href="logout.jsp">Logout</a></li>
                      <li><a type="button" href="#adminList">Admin features</a></li>
                    </ul>
                  </li>
                </ul>








            </div>
        </div>
        <div>
            <div class="my-slide-container">
                <div ng-view="" class="reveal-animation"></div>
            </div>
        </div>

        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
        <script src="js/app/controllers/homeController.js"></script>
        <script src="js/app/controllers/headerController.js"></script>
        <script src="js/app/controllers/loginController.js"></script>
        <script src="js/app/controllers/assignController.js"></script>
        <script src="js/app/controllers/contController.js"></script>       
        <script src="js/app/controllers/adminController.js"></script>       
        <script src="js/app/services/homeServices.js"></script>
        <script src="js/app/services/assignmentServices.js"></script>
        <script src="js/app/services/contestServices.js"></script>
        <script src="js/app/services/adminServices.js"></script>
        <script src="js/app/directives/directives.js"></script>
    </body>
</html>