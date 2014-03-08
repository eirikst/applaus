var controllers = angular.module('loginApp.controllers');

controllers.controller('LoginCtrl', function($scope, $location, $window, $timeout, LoginService) {
        
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
                $scope.loginErr = "Kunne ikke finne kombinasjonen av brukernavn"
                + " og passord. Vennligst prøv igjen.";
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=login");
        });
    };
           
    
    
    $scope.registerUser = function(user) {
        LoginService.registerUser(user)
        .success(function(data, status, headers, config) {
            if(data == 1) {
                $scope.msg = "Ny bruker er registrert. Du blir sendt videre til logg inn-siden.";
                console.log("Register successful");
                $timeout(function(){//redirect after timeout
                    $location.path('login');
                }, 3000);
            }
            else if (data == -1) {
                $scope.errMsg = "Brukernavnet er allerede i bruk. Vennligst fyll inn et nytt brukernavn.";
                console.log("Username exists");
            } else if (data == -2) {
                $scope.errMsg = "Epost er allerede i bruk. Vennligst fyll inn et nytt epost.";
                console.log("Email exists");
            } else if (data == -3) {
                $scope.errMsg = "Passord stemmer ikke overens.";
                console.log("Invalid password");
            }
        }).error(function(data, status, headers, config) {
            $scope.errMsg = "En feil skjedde. Prøv igjen senere.";
            console.log("Failed http action=registerUser");
        });
    };
    
    $scope.newPassword = function(email) {
        LoginService.newPassword(email)
        .success(function(data, status, headers, config) {
            if(data == 1) {
                $scope.msg = "Nytt passord er sendt til din epost.";
                console.log("New password sent by email");
            }
            else {
                $scope.errMsg = "Fant ikke din epost i systemet. Vennligst prøv igjen.";
                console.log("Invalid email");
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    };
    
});
