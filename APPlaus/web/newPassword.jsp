
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
        <div ng-controller="UserCtrl">
            <form>
                <h4>Nytt passord</h4>
                    
            <p>
                <input ng-model="email" name="email" class="form-control" type="text" placeholder="e-post">
            </p>
            <p>
                <input ng-model="pwd" name="pwd" class="form-control" type="password" placeholder="nytt passord">
            </p>
            <p>
                <input ng-model="pwdRepeat" name="pwdRepeat" class="form-control" type="password" placeholder="gjenta nytt passord">
            </p>
            <p>
                <button class="form-control" ng-click="newPassword()">Registrer nytt passord</button>
            </p>
            </form>
            <p>
                <a href="login.jsp">Tilbake til logg inn</a>
            </p>
        </div>
        <script src="js/app/empApp.js"></script>
        <script src="js/app/controllers/controllers.js"></script>
        <script src="js/app/controllers/headerController.js"></script>
        <script src="js/app/controllers/userController.js"></script>
    </body>
</html>
