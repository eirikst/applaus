<%-- 
    Document   : login
    Created on : Feb 19, 2014, 11:38:18 AM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="employeeApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
        <title>APPlaus</title>
        
        <link rel="stylesheet" href="css/bootstrap.css"/>
        
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
    </head>
    <body>
        <div ng-controller="LoginCtrl">
            <h3>Velkommen til APPlaus!</h3>
            <form>
            <p>
                <input ng-model="usr" name="usr" class="form-control" type="text" placeholder="username">
            </p>
            <p>
                <input ng-model="pwd" name="pwd" class="form-control" type="password" placeholder="password">
            </p>
            <p>
                <button class="form-control" ng-click="login()">Log in</button>
            </p>
            </form>
        </div>
        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
    </body>
</html>