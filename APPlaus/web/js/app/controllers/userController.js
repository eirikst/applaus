var controllers = angular.module('employeeApp.controllers');

controllers.controller('UserCtrl', function($scope, $location, $http, $window) {
        
    $scope.registerUser = function() {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=registerUser&usr=" + $scope.usr + "&pwd=" + $scope.pwd + "&pwdRepeat=" +  $scope.pwdRepeat
                                       + "&fname=" + $scope.fname + "&lname=" +  $scope.lname + "&email=" +  $scope.email,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            console.log("User registered");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=registerUser");
        });
    }
    
    $scope.newPassword = function() {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=newPassword&email=" + $scope.email + "&pwd=" + $scope.pwd + "&pwdRepeat=" +  $scope.pwdRepeat,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            console.log("New password registered");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=newPassword");
        });
    }
});
