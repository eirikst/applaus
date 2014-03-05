var controllers = angular.module('employeeApp.controllers');

controllers.controller('AdminCtrl', function($scope, $location, $http, $window, AdminService) {
        
    $scope.adminList = new Array();    
        
    AdminService.getAdminList()
            .success(function(data, status, headers, config) {
            $scope.adminList = data;            
        });

    $scope.setRole = function(username, role_id) {
        AdminService.setRole(username, role_id)
        .success(function(data, status, headers, config) {
              $scope.adminList = data;  
            }).error(function(data, status, headers, config) {
                //error
            });
        }
});