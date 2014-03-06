<%-- 
    Document   : login
    Created on : Feb 19, 2014, 11:38:18 AM
    Author     : eirikstadheim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="loginApp">
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
    <body style="margin-top: 61px; background-repeat:no-repeat;background-attachment:fixed;background-size: cover;height:" background="img/bw.jpg">
        <div class="navbar navbar-default navbar-fixed-top navbar-color" role="navigation">
            <div style="margin:15px" class="text-center">
                <h4>Velkommen til APPlaus!</h4>
            </div>
        </div>
        
        <div ng-view></div>
        
        <script src="js/app/loginApp.js"></script>
        <script src="js/app/controllers/loginController.js"></script>
        <script src="js/app/services/loginServices.js"></script>
    </body>
</html>