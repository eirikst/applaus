var controllers = angular.module('employeeApp.controllers');

controllers.controller('TestCtrl', function($scope, $location) {
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
});