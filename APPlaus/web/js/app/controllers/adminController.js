var controllers = angular.module('employeeApp.controllers');

controllers.controller('AdminCtrl', function($scope, AdminService) {

    $scope.getAdminList = function() {
        AdminService.getAdminList()
                .success(function(data, status, headers, config) {
                    $scope.adminList = data;
                    console.log("getAdminList success");
                })
                .error(function(data, status, headers, config) {
                    $scope.adminListErr = "En feil skjedde under lasting. Vennligst"
                            + "prøv igjen.";
                    console.log("http error getAdminList");
                });
    };

    $scope.setRole = function(username, role_id) {
        AdminService.setRole(username, role_id)
                .success(function(data, status, headers, config) {
                    $scope.adminList = data;
                    console.log("http error setRole");
                })
                .error(function(data, status, headers, config) {
                    $scope.adminListErr = "En feil skjedde. Vennligst prøv igjen.";
                    console.log("http error setRole");
                });
    };

    //init
    $scope.adminList = new Array();
    
    //init calls
    $scope.getAdminList();
});