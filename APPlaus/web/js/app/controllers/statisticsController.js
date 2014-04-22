var controllers = angular.module('employeeApp.controllers');

controllers.controller('StatisticsCtrl', function($scope, $location, StatisticsService) {
        
    $scope.getTopFive = function(period) {
        var month = 30;
        var quarter = 90;
        var halfyear = 180;
        StatisticsService.getTopFive(month)
                .success(function(data, status, headers, config) {
                    $scope.topFiveMonthPoints = data;
                    /*topFiveMonthUsernames = Object.keys(topFiveMonthPoints);
                    console.log(topFiveMonthPoints);
                    console.log(topFiveMonthUsernames);
                    
                    for(var k in data) {
                        console.log(k);
                    }
                    for(var i  = 0; i < topFiveMonthUsernames.length; i++) {
                        $scope.topFiveMonth[i].usernames = topFiveMonthUsernames[i];
                        $scope.topFiveMonth[i].points = topFiveMonthPoints[i];
                    }*/
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(quarter)
                .success(function(data, status, headers, config) {
                    $scope.topFiveQuarter = data;
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(halfyear)
                .success(function(data, status, headers, config) {
                    $scope.topFiveHalfYear = data;
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
    };
    
    //init calls
    $scope.getTopFive();
});
