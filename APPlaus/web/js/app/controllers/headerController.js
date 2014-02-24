var controllers = angular.module('employeeApp.controllers');

/*
 * Trengs ikke per nå, mulig senere
 */
controllers.controller('HeaderCtrl', function($scope, $location) {
    $scope.isActive = function(viewLocation) {
        return viewLocation === $location.path();
    };
})
