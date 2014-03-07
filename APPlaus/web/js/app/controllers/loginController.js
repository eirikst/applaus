var controllers = angular.module('loginApp.controllers');

controllers.controller('LoginCtrl', function($scope, $location, $http, $window, LoginService) {
        
    $scope.login = function(usr, pwd) {
        LoginService.login(usr, pwd)
                .success(function(data, status, headers, config) {
            if(data[0] === 3) {//regular user
                console.log("Usr/pwd okay, redirecting to index.jsp.");
                $window.location = "index.jsp";
            }
            else if(data[0] === 2) {//admin
                console.log("Usr/pwd okay for admin, redirecting to index.jsp.");
                    $window.location = "index.jsp";//index.jsp
            }
            else if(data[0] === 1) {//superadmin
                console.log("Usr/pwd okay for superadmin, redirecting to index.jsp.");
                    $window.location = "index.jsp";//index.jsp
            }
            else {
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
            if(data == 0) {
                $location.path('login');
                console.log("Register successfull");
            }
            else if (data == 1) {
                $scope.errMsg = "Brukernavnet er allerede i bruk, vennligst fyll inn et nytt brukernavn."
                console.log("Invalid username");
            } else if (data == 2) {
                $scope.errMsg = "Passord stemmer ikke overens."
                console.log("Invalid password");
            }
        }).error(function(data, status, headers, config) {
            $scope.errMsg = "En feil skjedde. Prøv igjen senere."
            console.log("Failed http action=registerUser");
        });
    }
    
    $scope.newPassword = function(email) {
        LoginService.newPassword(email)
        .success(function(data, status, headers, config) {
            if(data == 1) {
                $location.path('login');
                console.log("New password send by email");
            }
            else {
                $scope.errMsg = "Fant ikke din epost i systemet. Vennligst prøv igjen."
                console.log("Invalid email");
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    }
    
});
