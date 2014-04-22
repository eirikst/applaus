var controllers = angular.module('employeeApp.controllers');

controllers.controller('StatisticsCtrl', function($scope, $location, StatisticsService) {
        
    $scope.getTopFive = function(period) {
        var month = 30;
        var quarter = 90;
        var halfyear = 180;
        StatisticsService.getTopFive(month)
                .success(function(data, status, headers, config) {
                    $scope.topFiveMonth = [new Object(), new Object(), 
                            new Object(), new Object(), new Object()];
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveMonth[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.points.length; i++) {
                        $scope.topFiveMonth[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(quarter)
                .success(function(data, status, headers, config) {
                    $scope.topFiveQuarter = [new Object(), new Object(), 
                            new Object(), new Object(), new Object()];
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveQuarter[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.points.length; i++) {
                        $scope.topFiveQuarter[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(halfyear)
                .success(function(data, status, headers, config) {
                    $scope.topFiveHalfYear = [new Object(), new Object(), 
                            new Object(), new Object(), new Object()];
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveHalfYear[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.points.length; i++) {
                        $scope.topFiveHalfYear[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
    };
    
    //init calls
    $scope.getTopFive();
});
