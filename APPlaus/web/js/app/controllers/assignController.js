var controllers = angular.module('employeeApp.controllers');

// Assignment controller
controllers.controller('AssignCtrl', function($scope, $location, $route, $cookies, AssignService) {

    // creating a new type of assignment
    $scope.createAssignment = function(assignment) {
        AssignService.createAssignment(assignment)
                .success(function(data, status, headers, config) {
                    console.log("createAssignment success");
                    assignment._id = data._id;
                    $scope.selectedOptions.push(assignment);
                }).error(function(data, status, headers, config) {
            $scope.createErrMsg = "En feil skjedde. Vennligst prøv igjen.";
            console.log("Failed http action=createAssignment");
        });
    };

    // user registers a completed assignment
    $scope.registerAssignment = function(id, assignment) {
        var d = new Date(assignment.date_done.$date);
        assignment.time = d.getTime();
        AssignService.registerAssignment(id, assignment)
                .success(function(data, status, headers, config) {
                    if(data == 1) {
                        //$route.reload();
                    mergeOneUserAssign(assignment, id);
                    $scope.allAssignments.push(assignment);
                    $scope.skip ++;
                    console.log("registerAssignment success");
                    }
                    else if(data == -1) {
                        $scope.regErrMsg = "Feil ved dato angitt.";
                        console.log("Date not this week.");
                    }
                }).error(function(data, status, headers, config) {
            $scope.regErrMsg = "En feil skjedde. Vennligst prøv igjen.";
            console.log("Failed http action=registerAssignment");
        });
    };

    //gets the logged-in user assignment list
    $scope.getAllAssignments = function(skip) {
        AssignService.getAllAssignments(skip)
                .success(function(data, status, headers, config) {
                    $scope.skip += 7;
                    for (var i = 0; i < data.length; i++) {
                        $scope.allAssignments.push(data[i].assignments);//sets assignment table with info from DB
                    }
                    mergeUserAssign(skip);
                    console.log("getAllAssignments success");
                }).error(function(data, status, headers, config) {
            $scope.fetchErrMsg = "Det skjedde en feil under henting av oppgaver. Vennligst prøv igjen.";
            console.log("Failed http action=getAllAssignmentsUser");
        });
    };

    //gets the different types of assignments from the system
    getAssignmentTypes = function() {
        AssignService.getAssignmentTypes()
                .success(function(data, status, headers, config) {
                    $scope.selectedOptions = data;//sets assignment table with info from DB
                    $scope.selectedOption = $scope.selectedOptions[0];//first one is selected
                    console.log("getAssignmentTypes success");
                    $scope.getAllAssignments($scope.skip);//WARNING:should use promise
                }).error(function(data, status, headers, config) {
            $scope.fetchErrMsg = "Det skjedde en feil under henting av oppgaver. Vennligst prøv igjen.";
            console.log("Failed http action=getAssignmentsTypes");
        });
    };
    
    //puts the user assignment info together with given assignment info
    mergeUserAssign = function(skip) {
        for (var i = skip; i < $scope.allAssignments.length; i++) {
            for (var a = 0; a < $scope.selectedOptions.length; a++) {
                if ($scope.allAssignments[i].id == $scope.selectedOptions[a]._id.$oid) {
                    $scope.allAssignments[i].title = $scope.selectedOptions[a].title;
                    $scope.allAssignments[i].points = $scope.selectedOptions[a].points;
                }
            }
        }
    }
    
    //puts the user assignment info from one instance together with given assignment info
    mergeOneUserAssign = function(assignment, id) {
        for (var i = 0; i < $scope.selectedOptions.length; i++) {
            if (id == $scope.selectedOptions[i]._id.$oid) {
                assignment.title = $scope.selectedOptions[i].title;
                assignment.points = $scope.selectedOptions[i].points;
            }
        }
    }

    //init values
    $scope.skip = 0;
    $scope.allAssignments = new Array();
    $scope.selectedOptions = new Array();
    
    //start calls
    getAssignmentTypes();
    //$scope.getAllAssignments($scope.skip);
    
    //get user role
    $scope.roleCookie = $cookies.role;





    ////////////////////////////////////////////////////////////////////////////



    //bort??
    $scope.changeView = function(view) {
        $location.path(view); // path not hash
    };

});