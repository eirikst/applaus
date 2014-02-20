angular.module('employeeApp.controllers', [])

/*
 * Trengs ikke per nå, mulig senere
 */
.controller('HeaderCtrl', function($scope, $location) {
    $scope.isActive = function(viewLocation) {
        return viewLocation === $location.path();
    };
})

.controller('UserFrontCtrl', function($scope, $location, $http) {
    //Carousel related
    $scope.weekPoints = 5;
    $scope.monthPoints = 10;
    $scope.yearPoints = 60;
    
    $scope.slideInterval = 5000;
    var slides = $scope.slides = [];
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne uka', points:$scope.weekPoints});
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet denne måned', points:$scope.monthPoints});
    slides.push({image:'http://placehold.it/800x320', text:'Antall poeng samlet i år', points:$scope.yearPoints});
    

    
    
    //Goal related
    
    $scope.goal;
    
    
    //Ideabank related
    
    
    
    
    //News related
    $scope.news = [{title:'Konkurranse ferdig!', text:'Konkurransen "APPlaus 2.0" er ferdig. Dette er dummytekst laget av Eirik.'},
    {title:'Hvordan går det?', text:'Bruker du APPlaus nok? Snart kommer det en ny konkurranse som vil motivere til bruk av APPlaus blablabla.'},
    {title:'Heia', text:'Dette er dummytekst laget av Eirik.'},
    {title:'Registrer mål for uka!', text:'Dette er dummytekst laget av Eirik.'}];


    $scope.changeView = function(view) {
        $scope.weekPoints = 599;
        $location.path(view); // path not hash
    };

    $scope.dropdown = false;
    $scope.drop = function() {
        $scope.dropdown = !$scope.dropdown;
    };
    
    
    $scope.returnValue = "";
    
})



.controller('TestCtrl', function($scope, $location) {
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
})


.controller('AssignCtrl', function($scope, $location, $http) {
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    //Assignment related
    $scope.selectedOptions = new Array();
    $scope.selectedOption = $scope.selectedOptions[0];
    
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAssignments",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.selectedOptions = data;//sets assignment table with info from DB
        $scope.selectedOption = $scope.selectedOptions[0];
    }).error(function(data, status, headers, config) {
        return [];
    });    
    
    $scope.allAssignments = new Array();
  
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getAllAssignments",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.allAssignments = data;//sets assignment table with info from DB
    }).error(function(data, status, headers, config) {
        return [];
    });
})




//ContestCtrl
.controller('CompCtrl', function($scope, $location, $http) {
    //init values
    $scope.inactiveCont = new Array();
    $scope.partCont = new Array();
    $scope.skipNext = 0;
    
    
    
    
    //getting active connections
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=getActiveContests",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
        $scope.activeCont = data;//sets assignment table with info from DB
    }).error(function(data, status, headers, config) {
        console.log("Failed http action=getActiveContests");
    });
    
    //getting list of active contests user is participating in
    $http({
      url: 'MongoConnection',
      method: "POST",
      data: "action=userActiveContList",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data, status, headers, config) {
                $scope.partCont = data;//sets assignment table with info from DB
    }).error(function(data, status, headers, config) {
        console.log("Failed http action=userActiveContList");
    });
    
    //getting inactive contests
    $scope.getInactiveCont = function(skip) {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=getInactiveContests&skip=" + skip,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            for(var i = 0; i < data.length; i++) {
                $scope.inactiveCont.push(data[i]);//sets assignment table with info from DB
            }
            $scope.skipNext += 7;
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=getInactiveContests");
        });
    };

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
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=participate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            $scope.partCont.push(contest._id.$oid);
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=participate");
        });
    };
    
    //Registers not to participate
    $scope.dontParticipate = function(contest) {
        $http({
            url: 'MongoConnection',
            method: "POST",
            data: "action=dontParticipate&contestId=" + contest._id.$oid,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            for(var i = 0; i < $scope.partCont.length; i++) {
                if(contest._id.$oid == $scope.partCont[i]) {
                    $scope.partCont.splice(i, 1);
                }
            }
        }).error(function(data, status, headers, config) {
            console.log("Failed http action=dontParticipate");
        });
    };

    //I en service eller noe vel
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };
    
    
    $scope.getInactiveCont(0);
})


.controller('LoginCtrl', function($scope, $location, $http, $window) {
        
        $scope.login = function() {
            $http({
                url: 'MongoConnection',
                method: "POST",
                data: "action=login&usr=" + $scope.usr + "&pwd=" + $scope.pwd,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function(data, status, headers, config) {
                if(data[0] > 0) { // MÅ ENDRES MED FLERE ROLLER
                    console.log("Usr/pwd okay, redirecting.");
                        $window.location = "index.jsp";
                    }else {
                    console.log("Usr/pwd don't match.");
                }
            }).error(function(data, status, headers, config) {
                console.log("Failed http action=login");
            });
           /*
            $http({
                url: 'MongoConnection',
                method: "POST",
                data: "action=logsin&usr=" + $scope.usr + "&pwd=" + $scope.pwd,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function(data, status, headers, config) {
                console.log("success");
            }).error(function(data, status, headers, config) {
                console.log("fail");
            });
            */

        }
            
});