var controllers = angular.module('employeeApp.controllers');

controllers.controller('LoginCtrl', function($scope, $location, $http, $window) {
        
    $scope.login = function() {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=login&usr=" + $scope.usr + "&pwd=" + $scope.pwd,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            if(data[0] == 3) {//regular user
                console.log("Usr/pwd okay, redirecting to index.jsp.");
                $window.location = "index.jsp";
            }
            else if(data[0] == 2 || data[0] == 1) {//admin/superadmin
                console.log("Usr/pwd okay for admin, redirecting to admin.jsp.");
                    $window.location = "index.jsp";//admin.jsp
            }else {
                console.log("Usr/pwd don't match.");
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=login");
        });
    }
            
});
