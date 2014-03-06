var controllers = angular.module('employeeApp.controllers');

controllers.controller('AdminCtrl', function($scope, $location, $http, $window, AdminService) {
          
    $scope.getAdminList = function() {
        AdminService.getAdminList()
            .success(function(data, status, headers, config) {
            $scope.adminList = data;            
        })
            .error(function(data, status, headers, config) {
                $scope.adminListErr = "En feil skjedde under lasting. Vennligst"
        + "prøv igjen.";
        });
    };

    $scope.setRole = function(username, role_id) {
        AdminService.setRole(username, role_id)
        .success(function(data, status, headers, config) {
              $scope.adminList = data;  
        })
        .error(function(data, status, headers, config) {
            $scope.adminListErr = "En feil skjedde. Vennligst prøv igjen.";
        });
    };
        
    //init
    $scope.adminList = new Array();  
    $scope.getAdminList();
});