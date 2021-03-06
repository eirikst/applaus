var controllers = angular.module('loginApp.controllers');

controllers.controller('LoginCtrl', function($scope, $location, $window, $timeout, LoginService, $http) {
        
    $scope.login = function(usr, pwd) {
        LoginService.login(usr, pwd)
                .success(function(data, status, headers, config) {
            if(data[0] == 3) {//regular user
                console.log("Usr/pwd okay for employee user, redirecting to index.jsp.");
                $window.location = "index.jsp";
            }
            else if(data[0] == 2) {//admin
                console.log("Usr/pwd okay for admin, redirecting to index.jsp.");
                    $window.location = "index.jsp";//index.jsp
            }
            else if(data[0] == 1) {//superadmin
                console.log("Usr/pwd okay for superadmin, redirecting to index.jsp.");
                    $window.location = "index.jsp";//index.jsp
            }
            else {
                console.log("Usr/pwd don't match.");
                $scope.loginErr = "Could not find combination of username"
                + " and password. Please try again.";
            }
        }).error(function(data, status, headers, config) {
                $scope.loginErr = "An error occurred. Please try again.";
                console.log("Failed http action=login");
        });
    };
           
    
    
    $scope.registerUser = function(user, id) {
        if(user.pwd != user.pwdRepeat) {
            console.log("Passwords not matching.");
            $scope.errMsg = "Passwords are not matching.";
        }
        else {
            user.id = id;
            LoginService.registerUser(user)
            .success(function(data, status, headers, config) {
                console.log(data);
                if(data == 1) {
                    $scope.msg = "New user registered. Redirecting to login page.";
                    console.log("Register successful");
                    $timeout(function(){//redirect after timeout
                        $location.path('login');
                    }, 3000);
                }
                else if (data == -1) {
                    $scope.errMsg = "Username is already in use. Please fill-in a new username.";
                    console.log("Username exists");
                } else if (data == -2) {
                    $scope.errMsg = "Email is already in use. Please fill-in a new email.";
                    console.log("Email exists");
                } else if (data == -8) {
                    $scope.errMsg = "Passwords do not match.";
                    console.log("Invalid password");
                } else {
                    $scope.errMsg = "An error occured. Please try again.";
                }
            }).error(function(data, status, headers, config) {
                $scope.errMsg = "An unexpected error occurred. Please try again.";
                console.log("Failed http action=registerUser");
            });
        }
    };
    
    $scope.newPassword = function(email) {
        LoginService.newPassword(email)
        .success(function(data, status, headers, config) {
            if(data == 1) {
                $scope.msg = "New password is sent to your email.";
                console.log("New password sent by email");
            }
            else {
                $scope.errMsg = "Could not find your email in the system. Please try again.";
                console.log("Invalid email");
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    };
    
    $scope.regUserFb = function(user) {
        LoginService.regUserFb(user)
                .success(function(data, status, headers, config) {
            console.log("Successul http call action=newPassword");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    };
    
    getSections = function() {
        LoginService.getSections()
        .success(function(data, status, headers, config) {
            $scope.sections = data;
            $scope.selSection = $scope.sections[0];
            console.log("getSections success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getSections");
        });
    }
    
    $scope.updateFaceUri = function() {
        var uri = "http://www.facebook.com/dialog/oauth?client_id=294235794072909&redirect_uri=";
        uri += encodeURI("http://localhost:8080/APPlaus/APPlausServlet?action=fbReg&usr=gege&sectionId=533a7502bee45e22ec667aee");
        uri+= "&scope=email";
        console.log(uri);
        $scope.faceUri = uri;
    }
    
    $scope.regFace = function() {
        var uri = "http://www.facebook.com/dialog/oauth?client_id=294235794072909&redirect_uri=";
        uri += encodeURI("http://localhost:8080/APPlaus/APPlausServlet?action=fbReg&usr=gege&sectionId=533a7502bee45e22ec667aee");
        uri+= "&scope=email";
        console.log(uri);
        $scope.faceUri = uri;

        var promise = $http({
            url: 'http://www.facebook.com/dialog/oauth',
            method: "POST",
            data: uri,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });    }
    
    //init
    $scope.sections = new Array();
    
    
    getSections();
    
});
