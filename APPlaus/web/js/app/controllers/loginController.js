var controllers = angular.module('loginApp.controllers');

controllers.controller('LoginCtrl', function($scope, $location, $http, $window, LoginService) {
        
    $scope.login = function(usr, pwd) {
        LoginService.login(usr, pwd)
                .success(function(data, status, headers, config) {
            if(data[0] === 3) {//regular user
                console.log("Usr/pwd okay, redirecting to index.jsp.");
                $window.location = "index.jsp";
            }
            else if(data[0] === 2 || data[0] === 1) {//admin/superadmin
                console.log("Usr/pwd okay for admin, redirecting to index.jsp.");
                    $window.location = "index.jsp";//admin.jsp
            }else {
                console.log("Usr/pwd don't match.");
                $scope.loginErr = "Could not find username password match. " + 
                        "Please try again.";
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=login");
        });
    }
           
    
    
    $scope.registerUser = function(user) {
        LoginService.registerUser(user)
        .success(function(data, status, headers, config) {
            console.log("User registered");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=registerUser");
        });
    }
    
    $scope.newPassword = function(userInfo) {
        LoginService.newPassword(userInfo)
        .success(function(data, status, headers, config) {
            console.log("New password registered");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    }
    
});
