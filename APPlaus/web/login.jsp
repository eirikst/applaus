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
    <body style="margin-top: 61px">
        <div class="navbar navbar-default navbar-fixed-top navbar-color" role="navigation" ng-controller="HeaderCtrl">
            <div style="margin:10px" class="text-center">
                <h3>Velkommen til APPlaus!</h3>
            </div>
        </div>
        <div ng-controller="LoginCtrl" class="container">
            <form name="loginForm">
                <h4>Logg inn</h4>
                    
            <p>
                <input ng-model="usr" name="usr" class="form-control" type="text" placeholder="username" ng-class="{red:loginForm.usr.$error.required && loginForm.usr.$dirty, green:!loginForm.usr.$error.required && loginForm.usr.$dirty}" required="true">
            </p>
            <p>
                <input ng-model="pwd" name="pwd" class="form-control" type="password" placeholder="password" ng-class="{red:loginForm.pwd.$error.required && loginForm.pwd.$dirty, green:!loginForm.pwd.$error.required && loginForm.pwd.$dirty}" required="true">
            </p>
            <p>
                <button class="form-control" ng-click="login(usr, pwd)"  ng-disabled="loginForm.$invalid">Log in</button>
            </p>
            </form>
            <div class="red">
                {{loginErr}}
            </div>
            <p>
                <a href="registerUser.jsp">Registrer deg</a>
            </p>
            <p>
                <a href="newPassword.jsp">Glemt passord</a>
            </p>
        </div>
        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
        <script src="js/app/controllers/headerController.js"></script>
        <script src="js/app/controllers/loginController.js"></script>
        <script src="js/app/services/loginServices.js"></script>
    </body>
</html>