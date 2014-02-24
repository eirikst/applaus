var controllers = angular.module('employeeApp.controllers');

controllers.controller('AssignCtrl', function($scope, $location, $http) {
    
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
    // new assignment
    $scope.createAssignment = function(){
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=createAssignment&title=" + $scope.title + "&points=" + $scope.points
            + "&desc=" + $scope.desc,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            return [];
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=createAssignment");
        });        
    };
    
    // register assignment
    $scope.registerAssignment = function(){
        var d = new Date($scope.date);
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=registerAssignment&id=" + $scope.selectedOption._id.$oid + "&date=" + d.getTime()
            + "&comment=" + $scope.comment,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            return [];
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=registerAssignment");
        });        
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