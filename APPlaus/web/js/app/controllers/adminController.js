var controllers = angular.module('employeeApp.controllers');

controllers.controller('AdminCtrl', function($scope, $location, $http, $window) {
        
    $scope.adminList = new Array();    
        
    $scope.getAdminList = function() {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getAdminList",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            $scope.adminList = data;
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getAdminList");
        });
    }
            
});