var controllers = angular.module('employeeApp.controllers');

// Assignment controller
controllers.controller('AssignCtrl', function($scope, $location, $http) {
    
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
    // creating a new type of assignment
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
    
    // user registers a completed assignment
    $scope.registerAssignment = function(){
        var d = new Date($scope.date_done);
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=registerAssignment&id=" + $scope.selectedOption._id.$oid + "&date_done=" + d.getTime()
            + "&comment=" + $scope.comment,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            return [];
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=registerAssignment");
        });        
    };    
    
    //Gets a list of all the types of assignments
    $scope.selectedOptions = new Array();
    
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAssignmentsTypes",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.selectedOptions = data;//sets assignment table with info from DB
        $scope.selectedOption = $scope.selectedOptions[0];
    }).error(function(data, status, headers, config) {
        console.log("Failed http action=getAssignmentsTypes");
    });    
    
    $scope.allAssignments = new Array();
  
    //Gettting the logged-in user assignment list
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAllAssignmentsUser",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.allAssignments = data;//sets assignment table with info from DB
    }).error(function(data, status, headers, config) {
        console.log("Failed http action=getAllAssignmentsUser");
    });
})