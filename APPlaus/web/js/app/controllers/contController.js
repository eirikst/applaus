var controllers = angular.module('employeeApp.controllers');

//ContestCtrl
controllers.controller('ContCtrl', function($scope, $location, $route, $cookies, ContestService) {
    
    //functions
    
    //getting data
    
    //getting active contests
    $scope.getActiveContests = function() {
        ContestService.getActiveContests()
            .success(function(data, status, headers, config) {
                for(var i = 0; i < data.length; i++) {
                    $scope.activeCont.push(data[i]);//sets assignment table with info from DB
                }
            console.log("getActiveContests success");
        }).error(function(data, status, headers, config) {
            $scope.errFetchMsg = "En feil skjedde under lesing av " + 
     "konkurranser.";
            console.log("Failed http action=getActiveContests");
        });
    };
    
    //getting inactive contests
    $scope.getInactiveContests = function(skip) {
        ContestService.getInactiveContests(skip)
            .success(function(data, status, headers, config) {
                for(var i = 0; i < data.length; i++) {
                    $scope.inactiveCont.push(data[i]);//sets assignment table with info from DB
                }
            $scope.skipNext += 7;
            console.log("getInactiveContests success");
        }).error(function(data, status, headers, config) {
            $scope.errFetchMsg = "En feil skjedde under lesing av " + 
     "konkurranser";
            console.log("Failed http action=getInactiveContests");
        });
    };

    //getting list of active contests user is participating in
    $scope.getUsersActiveContests = function() {
        ContestService.getUsersActiveContests()
                .success(function(data, status, headers, config) {
                    $scope.partCont = data;//sets assignment table with info from DB
            console.log("getUsersActiveContests success");
        })
                .error(function(data, status, headers, config) {
            $scope.errFetchMsg = "En feil skjedde under lesing av " + 
     "konkurranser";
            console.log("Failed http action=userActiveContList");
        });
    };
    
    //setting data
        
    //Registers to participate
    $scope.participate = function(contest) {
        ContestService.participate(contest)
                .success(function(data, status, headers, config) {
            $scope.partCont.push(contest._id.$oid);
    //adds contest id to participating contests
            console.log("participate success");
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
            //removes contest id from participating contests
            console.log("dontParticipate success");
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
            //removes given contest from active contests
            console.log("deleteContest success");
                }
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=deleteContest");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };

    //Create contest
    $scope.createContest = function(contest) {
        contest.dateSec = (new Date(contest.date_end.$date)).getTime();//long format
        
        formDate = function(date) {
            date -= 3600000;
            return date;
        };
        
        contest.dateSec = formDate(contest.dateSec);

        
        console.log(new Date(contest.dateSec));
        
        ContestService.createContest(contest)
                .success(function(data, status, headers, config) {
                    contest._id = data;//obj id returned
                    $scope.activeCont.unshift(contest);
                    $scope.skipNext ++;
                    //route reload
                    $scope.createSuccessMsg = "Konkurranse er lagt til!";
            console.log("createContest success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=createContest");
            $scope.createErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    //Edit contest
    $scope.editContest = function(contest) {
        contest.dateSec = (new Date(contest.date_end.$date)).getTime();//long format
        console.log(contest);
        ContestService.editContest(contest)
                .success(function(data, status, headers, config) {
                    $scope.activeMsg = "Konkurranse er endret!";
            console.log("editContest success");
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=editContest");
            $scope.activeErrMsg = "En feil oppsto. Vennligst prøv igjen";
        });
    };
    
    //checks if contestId matches any in participating array
    $scope.isParticipating = function(contestId) {
        for(var i = 0; i < $scope.partCont.length; i++) {
            if(contestId === $scope.partCont[i]) {
                return true;
            }
        }
    };
    
    //support method to the view
    //copies a contest and return the copy
    $scope.copyContest = function(contest) {
        var copied = angular.copy(contest);
        return copied;
    };
    
    //support method to clear msgs in view
    $scope.clearMsg = function() {
        $scope.activeMsg = null;
        $scope.activeErrMsg = null;
        $scope.createErrMsg = null;
        $scope.createSuccessMsg = null;
        $scope.errFetchMsg = null;
    };
    
    //init values
    $scope.activeCont = new Array();
    $scope.inactiveCont = new Array();
    $scope.partCont = new Array();
    $scope.skipNext = 0;
    
    //calls to get data
    $scope.getActiveContests();
    $scope.getUsersActiveContests();
    $scope.getInactiveContests($scope.skipNext);
    
    //gets cookie
    $scope.roleCookie = $cookies.role;//role cookie
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    
    
    
    
    // burde bort? sjekk dette
    $scope.changeView = function(view) {
        $location.path(view);
    };

    
    
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
});
