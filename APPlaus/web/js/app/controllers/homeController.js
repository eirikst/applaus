var controllers = angular.module('employeeApp.controllers');

controllers.controller('HomeCtrl', function($scope, $location, $route, $cookies, HomeService, IdeaService, AssignService, DeviceService) {
    //carousel!
    /*$scope.slideInterval = 5000;
     var slides = $scope.slides = [];
     slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne uka', points:$scope.week});
     slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne måned', points:$scope.month});
     slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet i år', points:$scope.year});
     */

    //Goal and points related

    //gets points and goals, progress...
    $scope.getPoints = function() {
        HomeService.getPoints()
                .success(function(data, status, headers, config) {
                    console.log("getPoints success");
                    $scope.week = data[0];//sets assignment table with info from DB
                    $scope.lastWeek = data[1];//sets assignment table with info from DB
                    $scope.month = data[2];//sets assignment table with info from DB
                    $scope.year = data[3];//sets assignment table with info from DB
                    
                    getGoals();//WARNING:should prob use promises
                }).error(function(data, status, headers, config) {
            $scope.genError = "Det skjedde en feil ved oppdatering av " +
                    "poeng. Vennligst prøv igjen senere.";
            console.log("failed http getPointsHome");
        });
    };

    //sets a goal according to parameter
    $scope.getStats = function() {
        var month = 30;
        var quarter = 90;
        var halfyear = 180;
        HomeService.getStats(month)
                .success(function(data, status, headers, config) {
                    $scope.pointsMonth = data.points;
                    $scope.myRankMonth = data.highest;
                    $scope.nrOfUsers = data.total;
                    $scope.lowestThisMonth = data.lowest;
                    $scope.userAboveMeMonth = data.aboveUser;
                    $scope.pointsAboveMeMonth = data.abovePoints;
                    $scope.userBelowMeMonth = data.belowUser;
                    $scope.pointsBelowMeMonth = data.belowPoints;
                    $scope.percentageMonth = $scope.myRankMonth / $scope.nrOfUsers;
                    console.log($scope.percentageMonth);
                    console.log("getStats success");
                }).error(function(data, status, headers, config) {
                    console.log("Failed http action=getHomeStats");
        });
        HomeService.getStats(quarter)
                .success(function(data, status, headers, config) {
                    $scope.pointsQuarter = data.points;
                    $scope.myRankQuarter = data.highest;
                    $scope.lowestThisQuarter = data.lowest;
                    $scope.userAboveMeQuarter = data.aboveUser;
                    $scope.pointsAboveMeQuarter = data.abovePoints;
                    $scope.userBelowMeQuarter = data.belowUser;
                    $scope.pointsBelowMeQuarter = data.belowPoints;
                    $scope.percentageQuarter = $scope.myRankQuarter / $scope.nrOfUsers;
                    console.log(data);  
                    console.log("getStats success");
                }).error(function(data, status, headers, config) {
                    console.log("Failed http action=getHomeStats");
        });
        HomeService.getStats(halfyear)
                .success(function(data, status, headers, config) {
                    $scope.pointsHalfYear = data.points;
                    $scope.myRankHalfYear = data.highest;
                    $scope.lowestThisHalfYear = data.lowest;
                    $scope.userAboveMeHalfYear = data.aboveUser;
                    $scope.pointsAboveMeHalfYear = data.abovePoints;
                    $scope.userBelowMeHalfYear = data.belowUser;
                    $scope.pointsBelowMeHalfYear = data.belowPoints;
                    $scope.percentageHalfYear = $scope.myRankHalfYear / $scope.nrOfUsers;
                    console.log(data);  
                    console.log("getStats success");
                }).error(function(data, status, headers, config) {
                    console.log("Failed http action=getHomeStats");
        });
    };

    //sets a goal according to parameter
    $scope.setGoal = function(goal) {
        HomeService.setGoal(goal)
                .success(function(data, status, headers, config) {
                    console.log("setGoal success");
                    $scope.goal = goal;
                    $scope.progress = ($scope.week / $scope.goal) * 100;
                }).error(function(data, status, headers, config) {
            $scope.setGoalErr = "Det skjedde en feil ved oppdatering av " +
                    "målsetting. Vennligst prøv igjen senere.";
                    console.log("Failed http action=setGoal");
        });
    };
    
    // get goals this and last week
    getGoals = function() {
        HomeService.getGoals()
                 .success(function(data, status, headers, config) {
                     console.log("getPoints success");
                     $scope.goal = data[0];//sets assignment table with info from DB
                     $scope.goalLast = data[1];//sets assignment table with info from DB
                     $scope.progress = ($scope.week / $scope.goal) * 100;
                 }).error(function(data, status, headers, config) {
             $scope.genError = "Det skjedde en feil ved henting av " +
                     "mål. Vennligst prøv igjen senere.";
             console.log("failed http getWeekGoal");
         });
     }

    //News related    
    $scope.getNews = function() {
        HomeService.getNews(skip)
                .success(function(data, status, headers, config) {
                    skip += 10;
                    for (var i = 0; i < data.length; i++) {
                        $scope.news.push(data[i]);//sets news table with info from DB
                    }
                    console.log("getNews success");
                }).error(function(data, status, headers, config) {
            $scope.genError = "Det skjedde en feil under henting av nyheter. "
                    + "Vennligst prøv igjen senere";
            console.log("Failed http action=getNews");
        });
    };

    //to add news everyone will see
    $scope.addNewsAll = function(story) {
        HomeService.addNewsAll(story)
                .success(function(data, status, headers, config) {
                    story._id = data._id;
                    story.date = data.date;
                    story.writer = data.writer;
                    $scope.news.unshift(story);
                    skip ++;
                    $scope.addNews = "Du la til en nyhet!";
                    console.log("addNewsAll success");
                }).error(function(data, status, headers, config) {
            $scope.addNewsErr = "Det skjedde en feil. "
                    + "Vennligst prøv igjen senere";
            console.log("Failed http action=addNewsAll");
        });
    };

    $scope.deleteNews = function(article) {
        HomeService.deleteNews(article)
                .success(function(data, status, headers, config) {
                    
                    $scope.createMsg = "Nyhet slettet";
                    console.log("deleteNews success");

            console.log("deleteNews success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteNews");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    

    //idea related

    //adds ideas
    $scope.addIdea = function(idea) {
        IdeaService.addIdea(idea)
                .success(function(data, status, headers, config) {
                $scope.ideaMsg = "Ide lagt til i idebank!";
                $scope.week += 20;//IDEA POINTS!
                $scope.month += 20;//IDEA POINTS!
                $scope.year += 20;//IDEA POINTS!
                $scope.progress = ($scope.week / $scope.goal) * 100;
            console.log("addIdea success");
                }).error(function(data, status, headers, config) {
            $scope.ideaErr = "Det skjedde en feil. "
                    + "Vennligst prøv igjen senere";
            console.log("Failed http action=addIdea");
        });
    };


    //assignment related

    // user registers a completed assignment
    $scope.registerAssignment = function(type, assignment) {
        var d = new Date(assignment.date_done);//gets date
        assignment.time = d.getTime();//date as long time
        AssignService.registerAssignment(type._id.$oid, assignment)
                .success(function(data, status, headers, config) {
                    if(data == 1) {
                        $scope.assignMsg = "Oppgave lagt til!";
                        $scope.week += type.points;
                        $scope.month += type.points;
                        $scope.year += type.points;
                        $scope.progress = ($scope.week / $scope.goal) * 100;
                    }
                    else if(data == -1) {
                        $scope.assignErrMsg = "Du kan bare registrere for dager tidligere denne uka eller idag";
                    }
            console.log("registerAssignment success");
        }).error(function(data, status, headers, config) {
            $scope.assignErrMsg = "Det skjedde en feil. Vennligst prøv igjen.";
            console.log("Failed http action=registerAssignment");
        });
    };

    //gets the different types of assignments from the system
    getAssignmentTypes = function() {
        AssignService.getAssignmentTypes().success(function(data, status, headers, config) {
            $scope.selectedOptions = data;//sets assignment table with info from DB
            $scope.selectedOption = $scope.selectedOptions[0];//first one is selected
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getAssignmentsTypes");
        });
    };


    //clears error messages
    $scope.clearMsg = function() {
        $scope.assignMsg = null;
        $scope.assignErrMsg = null;
        $scope.ideaMsg = null;
        $scope.ideaErr = null;
        $scope.addNews = null;
        $scope.addNewsErr = null;
    };



    //init + calls
    $scope.news = new Array();
    $scope.selectedOptions = new Array();
    getAssignmentTypes();
    var skip = 0;
    $scope.getPoints();
    $scope.getStats();
    $scope.getNews(skip);
    $scope.roleCookie = $cookies.role;//role cookie
    $scope.isMobile = DeviceService.isMobile();

    ////////////////////////////////////////////////////////////////////////////

    //BOORT?
    $scope.changeView = function(view) {
        if(navigator.userAgent.match(/Android|BlackBerry|iPhone|iPad|iPod|Opera Mini|IEMobile/i)) {
            $location.path(view); // path not hash
        }
    };
});