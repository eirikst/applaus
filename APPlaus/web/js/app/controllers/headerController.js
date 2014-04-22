var controllers = angular.module('employeeApp.controllers');

/*
 * Trengs ikke per nå, mulig senere
 */
controllers.controller('HeaderCtrl', function($scope, $location, $cookies) {
    $scope.isActive = function(viewLocation) {
        return viewLocation === $location.path();
    };
    
    $scope.logMe = function(direction) {
        console.log(direction);
    }
    
    //gets role id
    $scope.roleCookie = $cookies.role;
});
