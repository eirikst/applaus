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
    
    //gets the different types of assignments from the system
    $scope.getAssignmentTypes = function() {
        AdminService.getAssignmentTypes()
                .success(function(data, status, headers, config) {
                    $scope.assignmentTypeList = data;//sets assignment table with info from DB
                    console.log("getAssignmentTypes success");
                }).error(function(data, status, headers, config) {
                    $scope.assignmentTypeListErr = "En feil skjedde under lasting. Vennligst"
                            + "prøv igjen.";
            console.log("Failed http action=getAssignmentsTypes");
        });
    };
    
    //Edit assignment
    $scope.editAssignment = function(assignment) {
        AdminService.editAssignment(assignment)
                .success(function(data, status, headers, config) {
                    $scope.activeMsg = "Oppgave er endret!";
            console.log("editAssignment success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=editAssignment");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };

    //Delete assignment
    $scope.deleteAssignment = function(assignment) {
        AdminService.deleteAssignment(assignment)
                .success(function(data, status, headers, config) { 
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteAssignment");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    $scope.copyAssignment = function(assignment) {
        var copied = angular.copy(assignment);
        return copied;
    };

    //init
    $scope.adminList = new Array();
    $scope.assignmentTypeList = new Array();
    
    //init calls
    $scope.getAdminList();
    $scope.getAssignmentTypes();
    
});