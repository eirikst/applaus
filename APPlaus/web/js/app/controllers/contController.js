var controllers = angular.module('employeeApp.controllers');


//ContestCtrl
controllers.controller('ContCtrl', function($scope, $location, $http, ContestService) {
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
        });
    };
    
    //Registers not to participate
    $scope.dontParticipate = function(contest) {
        ContestService.dontParticipate(contest)
                .success(function(data, status, headers, config) {
            for(var i = 0; i < $scope.partCont.length; i++) {
                if(contest._id.$oid == $scope.partCont[i]) {
                    $scope.partCont.splice(i, 1);
                }
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=dontParticipate");
        });
    };

    // wwoottt?
    $scope.changeView = function(view) {
        $scope.getInactiveContests(3); // path not hash
    };
    
    
    //init values
    $scope.inactiveCont = new Array();
    $scope.partCont = new Array();
    $scope.skipNext = 0;
    
    //calls to get data
    $scope.getActiveContests();
    $scope.getUsersActiveContests();
    $scope.getInactiveContests($scope.skipNext);
})
