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
                $scope.loginErr = "En uventet feil skjedde. Vennligst prøv igjen";
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
                } else if (data == -8) {
                    $scope.errMsg = "Passord stemmer ikke overens.";
                    console.log("Invalid password");
                } else {
                    $scope.errMsg = "An error occured. Please try again.";
                }
            }).error(function(data, status, headers, config) {
                $scope.errMsg = "En uforutsett feil oppsto. Vennligst prøv igjen.";
                console.log("Failed http action=registerUser");
            });
        }
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
    
    
    //init
    $scope.sections = new Array();
    
    
    getSections();
    
});
