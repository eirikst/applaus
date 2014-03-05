var controllers = angular.module('employeeApp.controllers');


//ContestCtrl
controllers.controller('ContCtrl', function($scope, $location, $route, ContestService) {
    //functions
    
    //getting active contests
    $scope.getActiveContests = function() {
        ContestService.getActiveContests()
            .success(function(data, status, headers, config) {
                    $scope.activeCont = data;
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getActiveContests");
        });
    }
    
    //getting inactive contests
    $scope.getInactiveContests = function(skip) {
        ContestService.getInactiveContests(skip)
            .success(function(data, status, headers, config) {
                for(var i = 0; i < data.length; i++) {
                    $scope.inactiveCont.push(data[i]);//sets assignment table with info from DB
                }
            $scope.skipNext += 7;
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getInactiveContests");
        });
    };

    //getting list of active contests user is participating in
    $scope.getUsersActiveContests = function() {
        ContestService.getUsersActiveContests()
                .success(function(data, status, headers, config) {
                    $scope.partCont = data;//sets assignment table with info from DB
        })
                .error(function(data, status, headers, config) {
            console.log("Failed http action=userActiveContList");
        });
    }
    
    //checks if contestId matches any in participating array
    $scope.isParticipating = function(contestId) {
        for(var i = 0; i < $scope.partCont.length; i++) {
            if(contestId == $scope.partCont[i]) {
                return true;
            }
        }
    };
    
    //Registers to participate
    $scope.participate = function(contest) {
        ContestService.participate(contest)
                .success(function(data, status, headers, config) {
            $scope.partCont.push(contest._id.$oid);
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=participate");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    //Registers not to participate
    $scope.dontParticipate = function(contest) {
        ContestService.dontParticipate(contest)
                .success(function(data, status, headers, config) {
            for(var i = 0; i < $scope.partCont.length; i++) {
                if(contest._id.$oid === $scope.partCont[i]) {
                    $scope.partCont.splice(i, 1);
                }
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=dontParticipate");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    //Delete contest
    $scope.deleteContest = function(contest) {
        ContestService.deleteContest(contest)
                .success(function(data, status, headers, config) {
            for(var i = 0; i < $scope.activeCont.length; i++) {
                if(contest._id.$oid === $scope.activeCont[i]._id.$oid) {
                    $scope.activeCont.splice(i, 1);
                }
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteContest");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };

    //Create contest
    $scope.createContest = function(contest) {
        contest.date_end = (new Date(contest.date_end)).getTime();//long format
        console.log(contest.date_end);
        ContestService.createContest(contest)
                .success(function(data, status, headers, config) {
                    $route.reload();
            console.log("Successfully created a contest");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=createContest");
            $scope.createErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    //Edit contest
    $scope.editContest = function(contest) {
        contest.date_end = (new Date(contest.date_end.$date)).getTime();//long format
        ContestService.editContest(contest)
                .success(function(data, status, headers, config) {
            console.log("Successfully edited a contest");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=editContest");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };


    // wwoottt?
    $scope.changeView = function(view) {
        $location.path(view);
    };
    
    $scope.copyContest = function(contest) {
        var copied = angular.copy(contest);
        console.log("hei:" + copied.date_end.$date);
        return copied;
    }
    //init values
    $scope.inactiveCont = new Array();
    $scope.partCont = new Array();
    $scope.skipNext = 0;
    
    //calls to get data
    $scope.getActiveContests();
    $scope.getUsersActiveContests();
    $scope.getInactiveContests($scope.skipNext);
    
    /*
    var td = new Date();
    $scope.today = td.getFullYear() + "-";
    if(td.getMonth() < 9) {
        $scope.today += "0" + (td.getMonth() + 1) + "-";
    }
    else {
        $scope.today +=(td.getMonth() + 1) + "-";
    }
    if(td.getDate()< 10) {
        $scope.today += "0" + td.getDate();
    }
    else {
        $scope.today += td.getDate();
    }
    console.log($scope.today);*/
})
