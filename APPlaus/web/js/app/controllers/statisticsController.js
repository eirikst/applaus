var controllers = angular.module('employeeApp.controllers');

controllers.controller('StatisticsCtrl', function($scope, $location, StatisticsService) {
        
    getTopFive = function(period) {
        var month = 30;
        var quarter = 90;
        var halfyear = 180;
        StatisticsService.getTopFive(month)
                .success(function(data, status, headers, config) {
                    $scope.topFiveMonth = new Array();
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveMonth.push(new Object());
                        $scope.topFiveMonth[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.usernames.length && i < data.points
                            .length; i++) {
                        $scope.topFiveMonth[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(quarter)
                .success(function(data, status, headers, config) {
                    $scope.topFiveQuarter = new Array();
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveQuarter.push(new Object());
                        $scope.topFiveQuarter[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.usernames.length && i < data.points
                            .length; i++) {
                        $scope.topFiveQuarter[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
        StatisticsService.getTopFive(halfyear)
                .success(function(data, status, headers, config) {
                    $scope.topFiveHalfYear = new Array();
                    for(var i = 0; i < data.usernames.length; i++) {
                        $scope.topFiveHalfYear.push(new Object());
                        $scope.topFiveHalfYear[i].username = data.usernames[i];
                    }
                    for(var i = 0; i < data.usernames.length && i < data.points
                            .length; i++) {
                        $scope.topFiveHalfYear[i].points = data.points[i];
                    }
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "An unexpected error ocurred. Please try again.";
                console.log("Failed http action=getTopFive");
        });
    };
    
    getTotalPointsSeparated = function() {
        StatisticsService.getTotalPointsSeparated()
                .success(function(data, status, headers, config) {
                    $scope.assignPoints = data.assignPoints;
                    $scope.contPoints = data.contPoints;
                    $scope.ideaPoints = data.ideaPoints;
                    $scope.likesPoints = data.likesPoints;
                    $scope.totalPoints = data.totalPoints;
                }).error(function(data, status, headers, config) {
                $scope.genErr = "Error fetching data.";
                console.log("Failed http action=getTotalPoints");
        });
    }
    
    getCommentLikeStats = function() {
        StatisticsService.getCommentLikeStats()
                .success(function(data, status, headers, config) {
                    $scope.userLikes = data.likesInfo;
                    $scope.userMostCom = data.commentInfo;
                        }).error(function(data, status, headers, config) {
                $scope.genErr = "Error fetching data.";
                console.log("Failed http action=getCommentLikeStats");
        });
    }
    
    getSectionStats = function() {
        var month = 30;
        var quarter = 90;
        var halfyear = 180;
        StatisticsService.getSectionStats(month)
                .success(function(data, status, headers, config) {
                    $scope.sectionMonth = data;
                    for(var i = 0; i < $scope.sectionMonth.length; i++) {
                        if($scope.sectionMonth[i].nrOfUsers !== 0) {
                            $scope.sectionMonth[i].average = $scope.sectionMonth[i]
                                    .points / $scope.sectionMonth[i].nrOfUsers;
                        }
                        else {
                            $scope.sectionMonth[i].average = 0;
                        }
                    }
                }).error(function(data, status, headers, config) {
                $scope.genErr = "Error fetching data.";
                console.log("Failed http action=getSectionStats");
        });
        StatisticsService.getSectionStats(quarter)
                .success(function(data, status, headers, config) {
                    $scope.sectionQuarter = data;
                    for(var i = 0; i < $scope.sectionQuarter.length; i++) {
                        if($scope.sectionQuarter[i].nrOfUsers !== 0) {
                            $scope.sectionQuarter[i].average = $scope.sectionQuarter[i]
                                    .points / $scope.sectionQuarter[i].nrOfUsers;
                        }
                        else {
                            $scope.sectionQuarter[i].average = 0;
                        }
                    }
                }).error(function(data, status, headers, config) {
                $scope.genErr = "Error fetching data.";
                console.log("Failed http action=getSectionStats");
        });
        StatisticsService.getSectionStats(halfyear)
                .success(function(data, status, headers, config) {
                    $scope.sectionHalfYear = data;
                    for(var i = 0; i < $scope.sectionHalfYear.length; i++) {
                        if($scope.sectionHalfYear[i].nrOfUsers !== 0) {
                            $scope.sectionHalfYear[i].average = $scope.sectionHalfYear[i]
                                    .points / $scope.sectionHalfYear[i].nrOfUsers;
                        }
                        else {
                            $scope.sectionHalfYear[i].average = 0;
                        }
                    }
                }).error(function(data, status, headers, config) {
                $scope.genErr = "Error fetching data.";
                console.log("Failed http action=getSectionStats");
        });
    }
    
    
    $scope.changeView = function(view) {
        if(navigator.userAgent.match(/Android|BlackBerry|iPhone|iPad|iPod|Opera Mini|IEMobile/i)) {
            $location.path(view); // path not hash
        }
    }
    
    //init calls
    getTopFive();
    getTotalPointsSeparated();
    getCommentLikeStats();
    getSectionStats();
});
