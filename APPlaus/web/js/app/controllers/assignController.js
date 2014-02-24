var controllers = angular.module('employeeApp.controllers');

controllers.controller('AssignCtrl', function($scope, $location, $http) {
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    //Assignment related
    $scope.selectedOptions = new Array();
    $scope.selectedOption = $scope.selectedOptions[0];
    
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAssignments",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.selectedOptions = data;//sets assignment table with info from DB
        $scope.selectedOption = $scope.selectedOptions[0];
    }).error(function(data, status, headers, config) {
        return [];
    });    
    
    $scope.allAssignments = new Array();
  
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAllAssignments",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.allAssignments = data;//sets assignment table with info from DB
    }).error(function(data, status, headers, config) {
        return [];
    });
})
